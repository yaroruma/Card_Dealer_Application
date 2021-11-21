package com.example.termproject;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

public class One extends AppCompatActivity {

    static final int REQUEST_ENABLE_BT = 10;
    int mPariedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mRemoteDevie; // 블루투스 소켓
    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    String mStrDelimiter = "\n";
    char mCharDelimiter = '\n';


    Thread mWorkerThread = null;
    byte[] readBuffer;
    String lastBuffer="";
    int readBufferPosition;
    int player=0;
    boolean Ispaused = true;
    int cardnum, lastcardnum;
    int draw =0;
    private cardinfo mCardInfo;
    int time = 5;
    public static int changedPat=0;
    int drawsum = 0;
    int leftcard =7;
    int isone =0 ;

    TextView mEditTime, mEditPlayer, mEditCard, mEditDetail;
    Button mButtonStart;
    Button mButtonPause;
    ImageView mCardImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //화면이 바뀔 때 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oneview); //oneview 뷰로 설정

        mButtonPause = (Button) findViewById(R.id.pause);
        mButtonStart = (Button) findViewById(R.id.start);
        mEditCard = (TextView) findViewById(R.id.card);
        mEditPlayer = (TextView) findViewById(R.id.player);
        mEditDetail = (TextView) findViewById(R.id.detail);
        mEditTime = (TextView) findViewById(R.id.timeleft);
        mCardImage = (ImageView) findViewById(R.id.cardim); //뷰 지정(버튼, 텍스트 등)
        mCardInfo = new cardinfo();
        mCardInfo.init(); //카드 인덱스 초기화
        mButtonPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Ispaused){
                    sendData(String.valueOf(MainActivity.npeople)+" 0 0");
                }
                else {
                    Ispaused = true;
                }
            }
        }); // 일시정지 버튼이 눌렸을 때 정지
        mButtonStart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(Ispaused){
                    sendData(String.valueOf(MainActivity.npeople)+" 0 0");
                    Ispaused = false;
                }
                else {

                }
            }
        });; // 시작 버튼이 눌렸을 때 사람 수 전송, 일시정지 상태일 때 다시 시작

        checkBluetooth(); // 블루투스 활성화 시키는 메소드
    }


    BluetoothDevice getDeviceFromBondedList(String name) {     // 블루투스 장치의 이름을 페어링 된 장치 목록에서 검색
        Toast.makeText(getApplicationContext(), "장치를 선택해 주세요.", Toast.LENGTH_LONG).show();
        BluetoothDevice selectedDevice = null;
        for (BluetoothDevice deivce : mDevices) {
            if (name.equals(deivce.getName())) {
                selectedDevice = deivce;
                break;
            }
        }
        return selectedDevice;
    }

    void sendData(String msg) { // 문자열 전송(쓰레드 사용 x)
        msg += mStrDelimiter;  // 문자열 종료표시 (\n)
        try {
			mOutputStream.write(msg.getBytes());  // 문자열을 Byte로 변환하여 전송.
        } catch (Exception e) {  // 문자열 전송 도중 오류 발생 시 오류 메시지 출력
            Toast.makeText(getApplicationContext(), "데이터 전송중 오류가 발생", Toast.LENGTH_LONG).show();
        }
    }

    void connectToSelectedDevice(String selectedDeviceName) {
        mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //uuid: 범용 고유 식별자, IP와 비슷한 개념

        try {
            mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid); // 블루투스 통신용 소켓 생성
            mSocket.connect();
			mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream(); // RX, TX와 비슷한 개념

            // 데이터 수신 준비.
            beginListenForData();

        } catch (Exception e) { // 블루투스 연결 중 오류 발생 시 오류 메시지 출력
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }

    void beginListenForData() { // 블루투스 수신 메소드
        final Handler handler = new Handler();

        readBufferPosition = 0;       
        readBuffer = new byte[1024];  

        
        new Thread(new Runnable() { // 문자열 수신 쓰레드 선언
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        int byteAvailable = mInputStream.available();   
                        if (byteAvailable > 0) {                        
                            byte[] packetBytes = new byte[byteAvailable];
                            mInputStream.read(packetBytes);
                            for (int i = 0; i < byteAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == mCharDelimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(!data.equals(lastBuffer)) // 이전과 다른 값이 수신되면 packet 메소드 실행
                                            packet(data);
                                            lastBuffer = data;
                                        }

                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }

                    } catch (Exception e) {    // 데이터 수신 중 오류 발생 시 오류 메시지 출력
                        Toast.makeText(getApplicationContext(), "데이터 수신 중 오류가 발생 했습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }

        }).start(); // 쓰레드 실행

    }

    void selectDevice() { // 블루투스 페어링 메소드
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPariedDeviceCount = mDevices.size();

        if (mPariedDeviceCount == 0) { // 페어링된 장치가 없는 경우.
            Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");
		List<String> listItems = new ArrayList<String>();
        for (BluetoothDevice device : mDevices) {
            listItems.add(device.getName());
        }
        listItems.add("취소");  // 취소 항목 추가.
		final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
        listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) { // 연결할 장치를 선택한 경우, 선택한 장치와 연결
                if (item == mPariedDeviceCount) { 
                    Toast.makeText(getApplicationContext(), "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                } else { 
                    connectToSelectedDevice(items[item].toString());
                }
            }

        });

        builder.setCancelable(false);  
        AlertDialog alert = builder.create();
        alert.show();
    }


    void checkBluetooth() { //기기가 블루투스를 지원하는지 확인하는 메소드 
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) { 
            Toast.makeText(getApplicationContext(), "기기가 블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
        } else { 
            if (!mBluetoothAdapter.isEnabled()) { // 블루투스 지원하며 비활성 상태인 경우.
                Toast.makeText(getApplicationContext(), "현재 블루투스가 비활성 상태입니다.", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else // 블루투스 지원하며 활성 상태인 경우.
                selectDevice();
        }
    }


    @Override
    protected void onDestroy() {//앱이 종료될 때 데이터 수신 쓰레드 종료
        try {
            mWorkerThread.interrupt(); 
            mInputStream.close();
            mSocket.close();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //intent에서 받아온 값을 처리하는 메소드, requestCode 값에 따라 나눈다.
        switch (requestCode) {
            case REQUEST_ENABLE_BT: // 블루투스가 사용가능 할 때
                if (resultCode == RESULT_OK) { 
                    selectDevice();
                    break;
                }
            case 1:
                if (resultCode == RESULT_OK) { // 7카드를 냈을 때
                    changedPat = data.getIntExtra("pat",0);
                    sendData(String.valueOf(MainActivity.npeople)+" 0 "+String.valueOf(changedPat));
                    break;
                }
            case 2:
                if (resultCode == RESULT_OK) { // 한 플레이어가 원카드 상태일 때
                    int onesuc = data.getIntExtra("suc",0);
                    if(onesuc==1){
                        mEditTime.setText("게임 오버!");
                        mEditDetail.setText("플레이어 "+Integer.toString(player)+"가 승리했습니다.");
                        Ispaused = true;
                        sendData(String.valueOf(MainActivity.npeople)+" 1 0");
                    }
                    else {
                        draw = 1;
                        sendData(String.valueOf(MainActivity.npeople)+" 2 0");
                    }
                    break;
                }
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void packet(String packet){ //받아온 문자열을 처리하는 메소드
            StringTokenizer st = new StringTokenizer(packet); // 문자열을 공백을 기준으로 분리

            player = Integer.parseInt(st.nextToken()); // 현재 플레이어
            draw = Integer.parseInt(st.nextToken()); // 드로우 여부
            cardnum = Integer.parseInt(st.nextToken()); // 낸 카드 번호
            time = Integer.parseInt(st.nextToken()); // 남은 시간
            leftcard = Integer.parseInt(st.nextToken()); // 현재 플레이어가 가진 카드 수
            if(leftcard==1){ // 카드 수가 1장일 때
                Intent intent = new Intent(getApplicationContext(), onepopup.class); //onepopup 팝업창 실행
                intent.putExtra("player", player);
                startActivityForResult(intent, 2); // intent에서 받아온 값을 2번 requestCode경우에서 처리
            }
            else if(leftcard==0){ // 카드를 모두 냈을 때
                mEditTime.setText("게임 오버!");
                mEditDetail.setText(Integer.toString(player)+"번 플레이어가 승리했습니다.");
                mCardImage.setImageResource(mCardInfo.mCard[cardnum][0]); // 게임 오버 출력
            }
            isone=leftcard;
            card(); // 문자열을 나눈 뒤 card 메소드 실행

    }
    void card(){
        String log; // 로그 문자열
        mCardImage.setImageResource(mCardInfo.mCard[cardnum][0]); // 중앙의 이미지를 cardinfo 클래스의 mCard 배열에서 이미지 주소값을 받아 출력
        mEditTime.setText(Integer.toString(time)+" 초"); // 남은 시간 출력
            log="";
            log += Integer.toString(player);
            log += "번 플레이어가 ";
            mEditPlayer.setText(Integer.toString(player) + "번 플레이어");
            if (cardnum > 0) {
                switch (mCardInfo.mCard[cardnum][1]) { // 카드 모양에 따라 로그 변경
                    case 0:
                        mEditCard.setText("스페이드 " + Integer.toString((cardnum - 1) % 13 + 1));
                        log += "스페이드 " + Integer.toString((cardnum - 1) % 13 + 1) + "카드를 냈습니다. ";
                        break;
                    case 1:
                        mEditCard.setText("클로버 " + Integer.toString((cardnum - 1) % 13 + 1));
                        log += "클로버 " + Integer.toString((cardnum - 1) % 13 + 1) + "카드를 냈습니다. ";
                        break;
                    case 2:
                        mEditCard.setText("하트 " + Integer.toString((cardnum - 1) % 13 + 1));
                        log += "하트 " + Integer.toString((cardnum - 1) % 13 + 1) + "카드를 냈습니다. ";
                        break;
                    case 3:
                        mEditCard.setText("다이아 " + Integer.toString((cardnum - 1) % 13 + 1));
                        log += "다이아 " + Integer.toString((cardnum - 1) % 13 + 1) + "카드를 냈습니다. ";
                        break;
                    case 4:
                        mEditCard.setText(cardnum == 53 ? ("블랙 조커") : ("컬러 조커"));
                        log += cardnum == 53 ? ("블랙 조커") : ("컬러 조커") + "카드를 냈습니다. ";
                        break;
                }
                switch (mCardInfo.mCard[cardnum][2]) {	// 특수카드인 경우
                    case 1: // 공격 카드
                        if(cardnum!=lastcardnum)
                            drawsum += mCardInfo.mCard[cardnum][3];
                        log += "누적 드로우 수 : " + Integer.toString(drawsum);
                        break;
                    case 2: // 3
                        if (drawsum > 0) {
                            drawsum = 0;
                            log += "방어!";
                        }
                        break;
                    case 3: // J
                        log += "점프!";
                        break;
                    case 4: // Q
                        log += "백!";
                        break;
                    case 5: // K
                        log += "한 장 더!";
                        break;
                    case 6: // 7
                        if(cardnum!=lastcardnum) {
                            Intent intent = new Intent(getApplicationContext(), popupsev.class); // 팝업창 한 번만 실행
                            startActivityForResult(intent, 1);
                            log += "문양 변경! : ";
                            switch (changedPat) {
                                case 1:
                                    log += "스페이드";
                                    break;
                                case 2:
                                    log += "클로버";
                                    break;
                                case 3:
                                    log += "하트";
                                    break;
                                case 4:
                                    log += "다이아";
                                    break;
                            }
                            break;
                        }
                        else {
                            log += "문양 변경! : ";
                            switch (changedPat) {
                                case 1:
                                    log += "스페이드";
                                    break;
                                case 2:
                                    log += "클로버";
                                    break;
                                case 3:
                                    log += "하트";
                                    break;
                                case 4:
                                    log += "다이아";
                                    break;
                            }
                        }
                }
            } else if (cardnum == 0 && time > 0) { //카드를 내지 못한 경우
                log += "드로우 했습니다. ";
                if (drawsum > 0)
                    log += "누적 드로우 수 : " + Integer.toString(drawsum);
                drawsum = 0;
            } else if (cardnum == 0 && time == 0) { // 시간 초과
                log += "타임 오버! 드로우 하세요. ";
                if (drawsum > 0)
                    log += "누적 드로우 수 : " + Integer.toString(drawsum);
                drawsum = 0;
            } else if(draw == 1 && isone == 1) { // 원카드 선언에 실패한 경우
                log+="선언 실패! 드로우 하세요.";
                isone=0;
            }
            mEditDetail.setText(log); // 로그 출력
        lastcardnum = cardnum;
        log="";
    }

}

package com.example.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class onepopup extends AppCompatActivity{ // 원카드 선언 시 필요한 팝업 화면 클래스
    Activity thisactivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 화면이 시작될 때 실행
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popone); // popone 뷰로 설정

        Intent intent = getIntent(); // intent를 통해 데이터 전달
        int player = intent.getIntExtra("player",0);

        Button other = (Button)findViewById(R.id.otherplay); // 다른 플레이어 버튼이 눌렸을 때
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("suc", 0); // suc 값 0으로 전달
                setResult(RESULT_OK,intent);
                finish(); // 팝업 창 닫음
            }
        });

        Button one = (Button)findViewById(R.id.oneplay); // 원카드 플레이어 버튼이 눌렸을 때
        one.setText("플레이어 "+Integer.toString(player));
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("suc", 1); // suc 값 1로 전달
                setResult(RESULT_OK,intent);
                finish(); // 팝업 창 닫음
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}

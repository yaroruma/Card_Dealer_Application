package com.example.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class popupsev extends AppCompatActivity{ // 7 카드를 냈을 때 필요한 팝업 화면 클래스
    Activity thisactivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // 화면이 시작될 때 실행
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop7); //pop7 뷰로 설정



        Button spade = (Button)findViewById(R.id.spade); // 스페이드 버튼이 눌렸을 때
        spade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("pat", 1); // pat 값 1로 전달
                setResult(RESULT_OK,intent);
                finish(); // 팝업 창 닫음
            }
        });

        Button clover = (Button)findViewById(R.id.clover); // 클로버 버튼이 눌렸을 때
        clover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("pat", 2);
                setResult(RESULT_OK,intent);
                finish(); // 팝업 창 닫음
            }
        });

        Button heart = (Button)findViewById(R.id.heart); // 하트 버튼이 눌렸을 때
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("pat", 3);
                setResult(RESULT_OK,intent);
                finish(); // 팝업 창 닫음
            }
        });

        Button dia = (Button)findViewById(R.id.dia); // 다이아 버튼이 눌렸을 때
        dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("pat", 4);
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

package com.example.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity { // 메인 화면 구성 클래스

    public static int npeople=2;

    TextView mEditPeople;
    Button mButtonPlus;
    Button mButtonMinus;
    Button mButtonOnecard;
    Button mButtonPoker; // 뷰 구성(버튼 텍스트)

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 화면이 시작 됐을 때 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //activity_main 뷰로 설정

        mEditPeople = findViewById(R.id.people);
        mButtonPlus = (Button) findViewById(R.id.peopleup);
        mButtonMinus = (Button) findViewById(R.id.peopledown);
        mButtonPoker = (Button) findViewById(R.id.po);
        mButtonOnecard = (Button) findViewById(R.id.one);

        mButtonPlus.setOnClickListener(new OnClickListener() { // + 버튼이 눌렸을 때

            @Override
            public void onClick(View v) {
                npeople = (npeople<6) ? (npeople+1) : 6; // 최대 6명으로 설정
                mEditPeople.setText(Integer.toString(npeople));
            }
        });
        mButtonMinus.setOnClickListener(new OnClickListener() { // - 버튼이 눌렸을 때

            @Override
            public void onClick(View v) { 
                npeople = (npeople>2) ? (npeople-1) : 2; // 최소 2명으로 설정
                mEditPeople.setText(Integer.toString(npeople));
            }
        });
        mButtonPoker.setOnClickListener(new OnClickListener() { // 포커 버튼이 눌렸을 때

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "미구현 기능입니다. 업데이트를 기대해주세요.", Toast.LENGTH_LONG).show();
            }
        });
        mButtonOnecard.setOnClickListener(new OnClickListener() { // 원카드 버튼이 눌렸을 때

            @Override
            public void onClick(View v) {
                if(npeople>1) {
                    Intent intent = new Intent(getApplicationContext(), One.class); // intent를 통해 One 클래스 실행
                    startActivity(intent);
                }
            }
        });

    }
}

package com.example.termproject;

// 카드 정보를 저장하는 인덱스를 별도의 클래스로 선언하여 구성


import android.support.v7.app.AppCompatActivity;

public class cardinfo extends AppCompatActivity {
    public int mCard[][] = new int[55][4]; // 카드 수 54 + 카드 뒷면 1
    public static int pic=0; // 그림 주소
    public static int pat=1; // 카드 모양
    public static int spc=2; // 특수 카드 종류 
    public static int drw=3; // 공격 카드의 드로우 수

    public void init(){ // 카드 정보 초기화 메소드
        mCard[0][pic]=R.drawable.b1;
        mCard[1][pic]=R.drawable.s1;
        mCard[2][pic]=R.drawable.s2;
        mCard[3][pic]=R.drawable.s3;
        mCard[4][pic]=R.drawable.s4;
        mCard[5][pic]=R.drawable.s5;
        mCard[6][pic]=R.drawable.s6;
        mCard[7][pic]=R.drawable.s7;
        mCard[8][pic]=R.drawable.s8;
        mCard[9][pic]=R.drawable.s9;
        mCard[10][pic]=R.drawable.s10;
        mCard[11][pic]=R.drawable.s11;
        mCard[12][pic]=R.drawable.s12;
        mCard[13][pic]=R.drawable.s13;
        mCard[14][pic]=R.drawable.c1;
        mCard[15][pic]=R.drawable.c2;
        mCard[16][pic]=R.drawable.c3;
        mCard[17][pic]=R.drawable.c4;
        mCard[18][pic]=R.drawable.c5;
        mCard[19][pic]=R.drawable.c6;
        mCard[20][pic]=R.drawable.c7;
        mCard[21][pic]=R.drawable.c8;
        mCard[22][pic]=R.drawable.c9;
        mCard[23][pic]=R.drawable.c10;
        mCard[24][pic]=R.drawable.c11;
        mCard[25][pic]=R.drawable.c12;
        mCard[26][pic]=R.drawable.c13;
        mCard[27][pic]=R.drawable.h1;
        mCard[28][pic]=R.drawable.h2;
        mCard[29][pic]=R.drawable.h3;
        mCard[30][pic]=R.drawable.h4;
        mCard[31][pic]=R.drawable.h5;
        mCard[32][pic]=R.drawable.h6;
        mCard[33][pic]=R.drawable.h7;
        mCard[34][pic]=R.drawable.h8;
        mCard[35][pic]=R.drawable.h9;
        mCard[36][pic]=R.drawable.h10;
        mCard[37][pic]=R.drawable.h11;
        mCard[38][pic]=R.drawable.h12;
        mCard[39][pic]=R.drawable.h13;
        mCard[40][pic]=R.drawable.d1;
        mCard[41][pic]=R.drawable.d2;
        mCard[42][pic]=R.drawable.d3;
        mCard[43][pic]=R.drawable.d4;
        mCard[44][pic]=R.drawable.d5;
        mCard[45][pic]=R.drawable.d6;
        mCard[46][pic]=R.drawable.d7;
        mCard[47][pic]=R.drawable.d8;
        mCard[48][pic]=R.drawable.d9;
        mCard[49][pic]=R.drawable.d10;
        mCard[50][pic]=R.drawable.d11;
        mCard[51][pic]=R.drawable.d12;
        mCard[52][pic]=R.drawable.d13;
        mCard[53][pic]=R.drawable.jb;
        mCard[54][pic]=R.drawable.jc; // 카드 이미지 주소로 설정 (drawable 폴더 내에 이미지 파일 위치)
        for(int i = 0; i<4; i++){ // 카드 모양 설정
            for(int j=0;j<13;j++){
                mCard[13*i+j+1][pat]=i;
            }
        }
        mCard[0][pat]=0;
        mCard[53][pat]=4;
        mCard[54][pat]=4; //조커는 4로 설정
        for(int i=1;i<53;i++){
            if(i%13==1){ // A
                mCard[i][spc]=1;
                mCard[i][drw]=3;
            }
            else if(i%13==2){ // 2
                mCard[i][spc]=1;
                mCard[i][drw]=2;
            }
            else if(i%13==3){ // 3
                mCard[i][spc]=2;
                mCard[i][drw]=0;
            }
            else if(i%13==11){ // J
                mCard[i][spc]=3;
                mCard[i][drw]=0;
            }
            else if(i%13==12){ // Q
                mCard[i][spc]=4;
                mCard[i][drw]=0;
            }
            else if(i%13==0){ // K
                mCard[i][spc]=5;
                mCard[i][drw]=0;
            }
            else if(i%13==7){ // 7
                mCard[i][spc]=6;
                mCard[i][drw]=0;
            }
            else {
                mCard[i][spc]=0;
                mCard[i][drw]=0;
            } // 나머지 카드는 전부 0으로 설정
        }
        mCard[0][spc]=0;
        mCard[0][drw]=0;
        mCard[53][spc]=1;
        mCard[53][drw]=5;
        mCard[54][spc]=1;
        mCard[54][drw]=10; // 조커 카드 설정
    }
}

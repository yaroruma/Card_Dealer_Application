# Card Dealer Application
## Brief
로봇공학입문설계 과목 기말 텀프로젝트 미션 코드입니다. 카드를 이용한 보드게임에서 카드의 셔플과 분배를 위한 로봇을 제작하고 이용을 위한 UI 애플리케이션을 제작했습니다.

## Mission
원카드와 세븐 카드 포커 중 종목을 선택하고 인원수를 설정하면 각 게임의 룰에 맞게 분배해야하는 카드를 로봇과 통신. 원카드의 경우 각 플레이어가 내는 카드에 따른 드로우 카드와 방향 등에 대한 정보도 표시.   
포커의 경우 원카드보다 로직이 간단하여 구현하지 않음.

## Environment
- Android Studio
- minSdkVersion : 15
- targetSdkVersion : 28

## Classes

-  `MainActivity.java`   
메인화면 구성. 인원수와 종목을 선택하고 각 게임하면으로 정보전달.

- `One.java`   
원카드 게임에 필요한 인터페이스 표시. 블루투스 통신.
    ```java:One.java
    void packet(String packet) //로봇에서 블루투스로 보낸 문자열을 파싱
    void card()                //파싱한 데이터를 바탕으로 게임진행상황 업데이트
    void onActivityResult(int requestCode, int resultCode, Intent data)
                               //팝업창 등의 intent에서 받아온 값을 처리
    ```
- `onepopup.java`, `popupsev.java`  
원카드 상황, 7을 낸 상황에서 팝업창을 통해 게임진행.
    ```java:onepopup.java
    //버튼 클릭 시에 intent를 통한 class간 정보 전달
    one.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.putExtra("suc", 1);
            setResult(RESULT_OK,intent);
            finish();
        }
    });
    ```
- `cardinfo.java`   
각 카드의 정보를 2차원 배열로 재정의.
    ```java:cardinfo.java
    public int mCard[][] = new int[55][4]; 
    /* 첫번째 인덱스는 카드 수 54 + 카드 뒷면 1
     * 두번째 인덱스는 0 : 그림 주소
     *                1 : 카드 모양
     *                2 : 특수 카드 종류 
     *                3 : 공격 카드의 드로우 수
     */
    ```

## Packet
 - Application -> Robot   
 `str(int(참가인원) int(특수상황) int(7일 때 바꿀 문양))`

 - Robot -> Application   
 `str(int(현재 플레이어 번호) int(드로우 여부) int(낸 카드 번호) int(남은 시간) int(가진 카드 수))`

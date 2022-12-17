# CodingJavaProject2 - Minesweeper (22.10.28 ~ 22.11.13)  
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">  

![MineSweeper_OnGame](https://user-images.githubusercontent.com/109140000/204074033-ecf0daad-34fb-4190-b01c-27b009e00315.png)  
![MineSweeper_WinGame](https://user-images.githubusercontent.com/109140000/204074044-83eee100-a38e-4797-8c8c-79d0292e1686.png)  
![MineSweeper_EndGame](https://user-images.githubusercontent.com/109140000/204074051-fd565464-b9f9-4121-995b-b79c8ef2ffee.png)  
<div align="right">최초 제작 : 22.10.28 ~ 22.11.13 , 개선 및 보완 : 22.11.22 ~ 22.11.25</div><br><br>  


### 목차  
&nbsp; &nbsp; 개발 동기 - 설계 - 후기 - 개선 사항 - 참고
<hr>  

## 개발 동기
- 지뢰찾기 제작
- 수업시간에 JFrame을 배웠고, 복습할 겸 이 프로젝트를 진행했다.
- 해볼 만하다는 생각이 들었고, 할 수 있을 것 같았고, 해보면 재밌을 것 같다는 생각이 들었다. 그래서 해봤다. 

## 설계
- JFrame 
![MineSweeper_Structure](https://user-images.githubusercontent.com/109140000/202059040-12dd3a37-6f95-4962-8275-bc54b52125a9.png)  <br>
![minesweeper_edited_arch](https://user-images.githubusercontent.com/109140000/204074230-21b70853-88f8-4cb3-babf-18f4ac788dcb.png)  
- Class & Method   
![Minesweeper_code_arch](https://user-images.githubusercontent.com/109140000/204074202-6ddc2190-13af-496f-9ef7-480a7c9ccbb0.png)  

## 후기
맨날 Eclipse 콘솔창에다 글자만 찍다가, 스스로 움직이고 내 동작에 반응하는 프로그램을 만들어내니 재밌었다.
코드가 점차 길어질수록 변수와 메서드가 다양해지고 많아졌는데, 마치 부품들을 조립하는 느낌이 들었다.
코드를 작성하다 막혀서 힘들고 답답할 때도 있었는데 고민하던 부분을 해결해 냈을 때는 그 힘듦을 다 보상받는 느낌이었다.
또한 수업 시간에는 배우지 못한 내용들을 스스로 더 찾아서 배울 수 있는 좋은 경험이었다.  

이 프로젝트를 하면서 어려웠던 점들을 돌이켜 보면 다음과 같다.
(분명 더 있었을 테지만, 이 글을 작성하는 시점(22.11.15 / 최종 수정 : 22.11.26)에서 기억이 나지 않는 걸 보면 그렇게 어렵지는 않았던 것 같다.)

1) stepOnTheLand( ) 메서드 구현 간(초반) 
- 목적 : 버튼(2차원 배열 - 9rows , 9columns) 클릭 시 해당 지점을 기준으로 모든 방향에 대해 한 칸씩 확장(클릭된 것과 같은 결과)
- 계획 : 해당 지점의 row와 column에 각각 -1, +0, +1을 동일하게 적용하여 총 9개(3x3)의 좌표를 생성하고, 이중 for문으로 처리하는 코드 생각함
- 문제 : row나 column의 값이 0일 때 (8일 때의 경우 발생하는 예외에 대해서는 그 처리를 제대로 하여 문제가 없었음)
- 분석 : 엑셀에다가 좌표를 입력하고, 내가 작성한 코드를 따라 하나하나 손으로 다 해봄
- 원인 : row나 column이 0일 때 좌표값 중 하나가 -1이 되는 경우가 생기고, 해당 경우에 초기 작성된 이중 for문에서는 바로 반복문 탈출(코드가 다 실행되지 않음)
- 해결 : 그래서 좌표 중에 0이나 8이 있을 경우, 이중 for문 내의 범위를 별도로 조정하여 적용하도록 코드 작성(row_min_lmt , row_max_lmt , col_min_lmt , col_max_lmt)
2) timeClock( ) 쓰레드 작성 간
- 목적 : 게임 진행 시, 윈도우 상단( time(JLabel) )에 시간이 카운트 되는 것을 표현하고자 했음
- 계획 : 수업 시간에 해왔던 것처럼 Thread를 별도의 클래스로 분리하여 그 안에서 메서드 정의(run)
- 문제 : 근데, 생성자 호출하고 start()를 해도 작동을 안함
- 분석 : 구글링 하다가 Thread를 메서드로서 바로 사용하는 방법 발견
- 원인 : 모르겠음
- 해결 : 해당 방식 적용
3) timeClock( ) 쓰레드 작성 간2
- 목적 : 게임 진행 시, 윈도우 상단( time(JLabel) )에서 시간이 카운트 되는 것을 표현하고자 했음
- 계획 : 무한반복 for문 내부에 1000밀리초에 1씩 증가하도록 코드 작성 ( try/catch문 안에 wait(1000) 작성 )
- 문제 : 시간이 흐르기는 하는데, 1초에 1씩 오르는게 아니라, 수 초만에 십만 단위까지 증가함. 그리고 콘솔 창에 Current thread is not owner 출력
- 분석 : 안했음. 그냥 wait에서 sleep으로 바꾸니 해결됐다.
- 원인 : 선생님께 여쭤봄. wait는 같이 실행되는 다른 코드를 정지시킴, sleep은 자기 자신을 정지시킴  
- 해결 : wait 대신 sleep 사용
4) Minesweeper(Boolean gameResult) 생성자 작성 간
- 목적 : 게임 승리 시, 생성된 윈도우 상단에 소요 시간을 표시
- 계획 : Thread 내에서 currentTimeMillis( ) 활용하여 시간 측정(long) , long 타입의 데이터를 String 타입 변수로 변환/저장 , 생성자에서 setText( )
- 문제 : 안됨
- 분석 : " '게임 종료 시에 생성되는 윈도우'가 '계산된 시간 값 입력'보다 먼저 실행되기 때문이다." 라고 판단(가정)
- 원인 : 분석 내용이 맞는 것 같음
- 해결 : 소요 시간에 대한 계산 결과를 저장하는 변수 timeRc를 static으로 설정
5) Event Listener 생성 간
- 목적 : 마우스 우클릭으로 ▲ 표시
- 계획 : 구글링하여 찾은 정보를 바탕으로 코드를 구현한다.
- 문제 : 안됨 
- 분석 : 안했음. 방법 바꿔가면서 그냥 될 때까지 해봄
- 원인 : actionPerformed()내에서 마우스 이벤트에 대한 내용 작성, getButton과 getSource 혼동
- 해결 : MyActionListener클래스 내에 mousePressed메서드 추가. 제대로 된 조건 하에, 실행할 코드를 작성. 생성자에서 버튼(2차원 배열의 각 칸)에 대해 addMouseListener 추가  
6) retry 버튼 기능 구현 간  
- 목적 : retry 버튼 기능 클릭 시, 기존에 띄워져 있던 게임 '실행'창과 게임 '결과'창은 종료되고 새로운 게임 실행창은 생성  
- 계획 : retry 버튼 클릭시 기존 윈도우들에 dispose( )가 실행되게 하자  
- 문제 : 이전 게임 실행창이 종료가 안됨 (나머지는 정상 작동)  
- 분석 : 구글링, 이클립스 내에서 Declaration 참고  
- 원인 : dispose( )는 해당 시점에 활성화된 윈도우(프레임) 하나에만 작동  
- 해결 : 윈도우 생성 방법 분리 - 생성자(게임 실행창)와 메서드(게임 결과창)  
&nbsp;  contentPane구분 - getContentPane( )(게임 실행창)과 resultPane(게임 결과창)  
&nbsp;  dispose( ) 개별 실행 - dispose( )(게임 실행창)와 resultPane.dispose( )(게임 결과창)  

## 개선 사항 
- Minesweeper
1) 승패에 상관없이 게임 종료 시 윈도우 생성 (11-23-2022, 개선 완료)  
2) 게임 종료 시 생성된 윈도우에 버튼 2개 추가(재시작 , 종료) (11-25-2022, 개선 완료)  
- SiHoonChris
1) 많은 시도와 다양한 접근을 통해 문제 해결에 적극적으로 임하는 것은 좋으나,
새로운 시도에만 초점을 맞추다 보니 발생한 문제의 근본적인 원인을 깊게 분석하지 못하는 경향이 있는 것 같다.
그래서 가끔, 조금만 더 생각하면 수월하게 해결할 수 있는 문제를 빙 돌아서 가는 느낌을 스스로 받고는 했다.
2) 하다가 중간에 막힌 부분도 기록으로 다 남겨두자. 나중에라도 무엇이 문제였는지 확인할 수 있도록

## 참고
- 11-15-2022 부, 별도 관리
- 현재 Repository에서의 기록  
11-15-2022 / JavaStudy에 있던 프로그램 파일 현Repo로 이전  
11-22-2022 / 변수명 수정 및 변수 추가, 게임 결과창 프레임 수정  
11-23-2022 / 게임 결과창 프레임 수정  
11-24-2022 / 게임 결과창 프레임 내 버튼에 기능 구현  
11-25-2022 / 게임 결과창 프레임 내 버튼에 기능 구현(수정)  


- 이전 Repository : JavaStudy
- JavaStudy 내의 Commit 기록들  
(10-28-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/3f10000c97f16d54b0009d95ef761a86606bb4a9  
(10-29-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/ab768cfb4ddc589443ff2faba7303a0b518baff7  
(10-30-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/5738bd269f6efa9eccc03b7c34f9ffe53946198a  
(10-31-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/0c1c7ba7bf0d217e64e0fd5e307c7ef035edbcf4  
(11-01-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/785a696f73d06b8c5f99804e5c39f8e27788fa1e  
(11-02-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/be14eae7ca105f4726c99ad1cc7f5b165eb4bc7f  
(11-03-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/18bc54ce3cd61e900239b9a24d858f034fbb17d8  
(11-04-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/b1ac6e7b8cd9d955647dddbbb4361305aa37ecfe  
(11-05-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/aaa76755c08732faaad51125d4c0b78ada0e966f  
(11-06-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/3cfd3c7ad198d4aba21de37ea0c4e4ced81a0c0f  
(11-07-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/d3191b5e8a86ff645a0a4010887df768249f984e  
(11-08-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/bb4d5eaa1070048fac99920f81b60e4cabea7a7f  
(11-09-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/43fb92455f1236ae3c41a88808739813e181c51b  
(11-10-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/a5d4701ed1f79a2ba086ea92130efefb572cf17b  
(11-11-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/83fe8928d1ad226179fa99aaf9ec9c251aeb57df  
(11-12-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/3159c8c33146f00cb4a61b46c4fb9e23024fa3ca  
(11-13-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/89a8339b67e2977995900098df0d393e2ef96ff1  

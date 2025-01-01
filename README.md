# 싱숭생송
추억사진과 그때 들은 노래를 플레이리스트로 만들고, 감성있는 배경과 같이 하나의 사진으로 추억을 기록할 수 있는 앱


### 개발 기간 
2024.12.26(목) ~ 2025.01.01(수)

### 개발환경 
- Frontend : Kotlin <br>
- IDE : Android Studio

### 팀원 
장희주 : 숙명여자대학교 컴퓨터과학전공 21 <br>
정윤재 : 카이스트 전산학부 19

## Tab별 기능 소개 

### Tab1 : 나의 노래목록 보기, 추가하기 
오른쪽 상단에 있는 + 버튼을 누르면 
![image](https://github.com/user-attachments/assets/3a76f330-aeb7-489e-b5e9-e1ce0df2e65a)
노래제목과 아티스트명을 추가해주고, "나의 노래 목록에 저장하기" 버튼을 클릭하면 
![image](https://github.com/user-attachments/assets/037eac39-3ee0-4fe1-95fa-1417fc9bc637)
해당 노래가 리스트에 추가됩니다. 

### Tab2 : 나만의 플레이리스트 만들기 
+플리 추가하기 버튼을 누르면 
![image](https://github.com/user-attachments/assets/fe14e0ce-241d-4c39-acbe-04f7c523941a)
플레이리스트 이름, 대표 이미지를 갤러리와 연동해서 사진을 선택하고 tab1에 저장된 노래목록 중 원하는 것을 체크해서 플레이리스트에 저장한다. 
![image](https://github.com/user-attachments/assets/42e9fe9d-548d-49e6-9685-bd3af50b58e4)
업로드하면 위와 같이 보인다. 

플레이리스트의 이름, 플레이리스트에 들어갈 음악 목록, 이미지가 shared_preference를 통해 앱 내부에 저장한다. 
![image](https://github.com/user-attachments/assets/86656b88-ac8d-44b0-bb4f-7fe301f408c0)


### Tab3 : 나만의 export 플리 이미지 소장하기 
tab2에서 만든 플레이리스트를 로드해서 선택한 플레이리스트의 이미지와 추가할 설명내용과 플레이리스트에 있는 많은 노래 중 최대 3개까지만 고른다. <br>
그러고나서 변환 버튼을 눌러 하나의 사진으로 갤러리에 저장된다. 
![image](https://github.com/user-attachments/assets/2d44aa21-2a0b-4d3e-aeef-644661282da0)




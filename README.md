# JAVADOT-platformer-game
자바를 이용해서 만든 로그라이크 형식의 플랫폼게임입니다
https://coqoa.tistory.com/category/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D/%EA%B0%9C%EC%9D%B8%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8

### 210727 
>재실행을 반복하면 느려지는 현상을 해결하기 위해서  
>생성되는 객체에 null값을 대입해서 JVM의 GC가 관리하도록 구현해봤음.  
>별로 효과는 없는듯함..  

### 210728
>mainClass.stop()은 작동하지 않는 메서드였다,  
>stage.close()는 stage.hide()와 같은 역할을 하는 메서드이고 이는 내가 원하는 기능이 아니였다.  
>mainClass.start()를 실행하면 scene은 지속적으로 교체가 되지만 stage는 그대로였다.  
>기존의 scene은 정리되지않고 계속 새로운 scene을 선언하고 사용하니까 메모리가 중복되서 렉이걸리는 원인이 된거라고 판단한다.   

### 210802
>esc버튼을 누르거나 화면밑으로 낙하하면 첫시작위치로 player객체를 옮기고 energy객체도 원상복구하는식으로 쉽게 해결했다.  

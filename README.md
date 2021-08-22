 ---
 # JAVADOT

 ### 제작동기 및 목표

처음 배우는 언어로 `자바`를 선택했고 자바의 GUI인 `javafx`로 게임을 만들어 보고 싶었습니다  
javafx로 게임을 만들기 위해 참고할 사이트는 비교적 제한적이었고 이 부분이 오히려 짧은 시간에 더 많이 배울 수 있는 밑거름이 됐다고 생각합니다   
저는 무엇이든  첫단추를 꿰는게 가장 중요하다고 생각합니다.   
목표는 `무조건 내가 만족할 수준으로 배포하기` 입니다.  

---

#### 제작기간 
2021.06.24~ 08.23

---
<br/>
<br/>
[시연영상]
<br/>
<br/>

---
 

## JAVADOT은 도트 플랫포머 게임입니다

### 게임의 목표  
```

Player는 해저에서부터 시작해서 해수면, 육지, 비바람과 번개가 몰아치는 하늘을 건너 태양에 도달해야 합니다.

```  

### 게임의 특징 : 
```
1. 점프횟수에 제한이 있다
2. 땅을 밟으면 점프를 1회 할 수 있다
3. energy를 먹으면 점프 횟수 1 추가한다
4. 각종 상호작용블럭에 의해 죽을 수 있다.  
5. 죽으면 첫스테이지부터 다시 시작한다
```

 ### 키설명
 |행동|키|
 |:--:|:--:| 
 |이동|  `<-`,`->`|
 |점프|  `spacebar`|
 |재시작|   `esc`|
 |볼륨|on = `F11`, off=`F12`| 

 ```- 시연을 위해 숫자키 2 ~ 8, F1 ~ F7 키를 통해 순간이동할 수 있게 구현해놨습니다 ``` 

---

## Stage설명 및 상호작용하는 블럭 설명
```
JAVADOT은 1 Stage에서 7 Stage까지 존재합니다.  
Player와 상호작용하는 블럭들을 적절히 이용해서 스테이지를 클리어하면 됩니다.  
```

### 시작페이지
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327825-129d7693-ec04-4355-a57d-f76fcabbd5e4.gif"/>
  ➥ 시작페이지입니다.
  <br/><br/><br/>
  
### 기본적인 상호작용
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327903-797d1d00-dae8-4be5-8fc5-7f18bec48001.gif"/>
  ➥ energy블럭입니다. 먹으면 점프횟수가 늘어납니다.
  <br/><br/><br/>
  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327923-81fa6e99-3640-42a6-ab2c-4a76f0192097.gif"/>
  ➥ door블럭입니다. (접촉시 다음스테이지로 넘어갑니다)
  <br/><br/><br/>
  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327933-b0bde2b6-6539-4082-aa58-51e2f8b13f25.gif"/>
  ➥ 화면 아래로 벗어나면 1 Stage부터 재시작합니다. 
  <br/><br/><br/>

### 1 Stage : 해저
  
  <br/>
  <details>
    <summary> 1Stage 코드 및 사진을 보려면 클릭하세요  🧐 </summary>
    <div markdown="1">       
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324285-99261f1e-b075-43fc-871f-bba4053d6f2c.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324148-047b1752-29ca-4f61-a7d4-61b6c29d4f71.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324149-b19199f0-f3e9-4f82-8b58-a5ee8cc5de3d.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324152-1d328b77-10b5-41b3-9b0c-bdfc447217d5.png"/>
    </div>
  </details>
  <br/>
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130328181-156452ec-c682-4d4a-b277-1b18ba7fbf7d.gif"/>
  ➥ 해저를 표현하기 위해 바닥은 모래색으로 표현했습니다.    
  <br/><br/><br/>
  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130326291-f77cc2ff-9adb-4d34-a17d-42ac1c44cc78.gif"/>
  ➥ 수중에서 헤엄치는 느낌을 주기위해 닿으면 점프를 할 수 있는 블럭을 곳곳에 배치했습니다    
  <br/><br/><br/>

  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130326249-311c6b6d-05e0-46b3-87f4-d98442e395b9.gif"/>
  ➥ 맵의 디자인 요소로 하얀색 블럭으로 공기방울을 표현했습니다 2가지 속도를 줘서 좀 더 사실적으로 표현할 수 있었습니다.

  ➥ 해초류를 그렸고 밟을 수 있는 해초류와 밟지 못하는 해초류는 색으로 구분했습니다.   
  <br/><br/><br/>

### 2 Stage : 수중

  <br/>
  <details>
    <summary> 2Stage 코드 및 사진을 보려면 클릭하세요 🧐 </summary>
    <div markdown="2">       
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324423-995b9d90-df06-4071-b834-d104619f731a.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324425-648b5eab-7ef4-4349-9daf-2eee4f510cd0.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324427-470ecafb-d9c6-4e0e-a114-2d9ec82207b8.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324429-cf944b55-e720-4857-8f53-3d15344c14d2.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324430-e354d629-a861-4e68-bf82-612712606807.png"/>
    </div>
  </details>
  <br/>
  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130326839-847f0cb0-a456-49f4-aa55-9459a5ecfa21.gif"/>
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130326846-b220b105-81ed-436a-9181-42ac0d9dcb11.gif"/>
  ➥ 해초의 줄기를 표현하기 위해 초록색 객체를 사용했고 이 객체는 밟고 다니거나 옆에 붙을 수 있습니다 (붙었을 때는 투명해지도록 구현을 해 뒀습니다)  
  <br/><br/><br/>

### 3 Stage : 해수면

  <br/>
  <details>
    <summary>  3Stage 코드 및 사진을 보려면 클릭하세요 🧐 </summary>
    <div markdown="3">       
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324475-1fe8bc3f-a00f-41f7-ad3a-09dee1945127.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324478-52892ad3-a78b-425c-8839-319bd4170738.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324479-914c1400-c553-4c11-b6e6-11e7624947c2.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324480-68ea2067-151d-4687-a6ab-49a163b9725f.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324481-6f0bc674-7060-4bf5-83e3-e0c7a15ae1a4.png"/>
    </div>
  </details>
  <br/>
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327591-104a4c3e-6a65-4d5b-979b-662638159b3c.gif"/>
  ➥ 벽에 붙지않는 블럭입니다
  <br/><br/><br/>
  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327012-0ff28aee-1ee1-4180-9973-e48e5517267c.gif" />
  ➥ 닿으면 1 Stage부터 재시작하는 하얀색 블럭입니다.  
  <br/><br/><br/>

  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327014-bd339ed8-77a9-4196-a951-d21c3d1044af.gif"/>
  ➥ 점프해서 밟으면 높이 뛰는 발판입니다
  <br/><br/><br/>

### 4 Stage : 육지

  <br/>
  <details>
    <summary>4Stage 코드 및 사진을 보려면 클릭하세요 🧐 </summary>
    <div markdown="4">       
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324545-8b84a6a6-0f17-41cd-99fd-a68c5b5e5cd9.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324546-06072b64-9a00-4e82-8de8-8be68509922e.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324548-134d419e-4158-4f18-8922-ed2807a9f35e.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324550-9cd4ad87-2f0b-4113-8169-80f9e447c284.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324551-eb72e80d-d1cd-4f26-bbce-23fda2af0216.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324552-f9b3a443-d1c9-4458-a7ae-5e0d51d8b171.png"/>
    </div>
  </details>
  <br/>
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327535-c0fbc530-cd27-4da4-b8bc-c347e192443f.gif"/>
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327530-872bd243-c59f-4472-9480-e5d7edf7a5bc.gif"/>
  ➥ 짚라인을 표현하기 위해 만든 블럭으로 Player를 좌,우로 이동시켜줍니다
  <br/><br/><br/>

### 5 Stage : 하늘과 비바람

  <br/>
  <details>
    <summary> 5Stage 코드 및 사진을 보려면 클릭하세요 🧐 </summary>
    <div markdown="5">       
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324624-9722fffb-6c67-4bd7-a11a-96dc2c3be5cc.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324626-1a3d8113-2f16-453a-8da2-d4af9eb6974e.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324629-d590360f-a835-48b8-bb5f-b428d0d2b699.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324631-22e0cb9a-7f7c-4dee-a764-090391c5be26.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324632-905ced0d-424a-45d1-8ab0-6fd5c59c2bde.png"/>
    </div>
  </details>
  <br/>

  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327633-733ea9c1-b35f-4fb6-ad04-a372d95648f5.gif"/>
  ➥ 먹구름은 아래에서도 통과할 수 있으며, Player가 두려워도 앞으로 나아가는 것을 표현하고자 덜덜 떨게 만들었습니다.  
  <br/><br/><br/>
  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327643-7e7663a1-1313-4c2d-94c1-a0225e27f6a0.gif"/>
  ➥ 비를 맞으면 1 Stage부터 재시작합니다.  
  <br/><br/><br/>
  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327694-7fda4249-1831-4b97-ac73-933da51d7750.gif"/>
  ➥ 공기저항을 표현하기 위해 만들어진 블럭입니다. (낙하속도가 줄어듭니다)
  <br/><br/><br/>

### 6 Stage : 고난과 역경

  <br/>
  <details>
    <summary> 6Stage 코드 및 사진을 보려면 클릭하세요 🧐 </summary>
    <div markdown="6">       
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324688-5b07b250-8e44-4563-9c5b-0e562d3402be.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324690-72510821-0472-4acd-a987-541d49282c9c.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324692-9712ae1c-49fb-4633-8cc4-e836b0ef898b.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324693-ca036280-0b16-4d97-a096-5e25ae63765c.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324696-66eb3e5e-2f7e-4471-b07c-e36078b4394c.png"/>
    </div>
  </details>
  <br/>
  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327720-ae40655d-10d1-4249-b858-d6044ae5f941.gif"/>
  ➥ 위, 아래로 움직이는 블럭입니다. (아래에서 통과할 수 없습니다)
  <br/><br/><br/>

### 7 Stage : 먹구름과 번개 그리고 태양.

  <br/>
  <details>
    <summary> 7Stage 코드 및 사진을 보려면 클릭하세요 🧐 </summary>
    <div markdown="7">       
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324726-be323306-de30-45e3-8496-12e0010da95b.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324728-b9d72e11-dfbe-4547-85c2-67c40fffbe0b.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324729-f41b19be-4ee4-4154-99a5-0c30a21db303.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324732-5b54c47b-19de-4a32-bfb0-8e02cfbd078c.png"/>
    </div>
  </details>
  <br/>

  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327753-48df59fd-9ed5-485d-aa3a-b10c358ec30e.gif"/>
  ➥ 번개입니다. (맞으면 1 Stage부터 재시작합니다)
  <br/><br/><br/>

  
  <img width="40%" src="https://user-images.githubusercontent.com/81023768/130327761-21cfe992-2eba-447f-b49b-d6f42d347f11.gif"/>
  ➥ 태양입니다. (태양에 들어가면 게임 클리어입니다)
  <br/><br/><br/>



### Clear 

  <br/>
  <details>
    <summary> clear 코드 및 사진을 보려면 클릭하세요 🧐 </summary>
    <div markdown="8">       
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324753-47304492-84f8-4a0d-a6ae-948ef7b252c4.png"/>
      <img width="45%" src="https://user-images.githubusercontent.com/81023768/130324751-609ffa0b-0e47-4e34-b065-28c65c4652ce.png"/>
    </div>
  </details>
  <br/>


-----
210822 작성중..
메일주소
블로그주소
등등



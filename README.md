# spring-gift-order

# step0 코드 옮기기
- [x] option table 생성

# step1 카카오 로그인
- [x] 카카오 로그인을 통해 인가 코드 받기
- [x] 인가코드로 로그인하기
- [x] 받아온 이메일로 로그인하기

# step2 주문하기
- [x] 위시리스트에서 주문 시 상품 명, 옵션, 수량, 요청사항 정보를 담은 메세지 전송
- [x] 주문 시 위시리스트에서 삭제, 옵션에서 주문수량만큼 감소

# step3 배포하기
- [x] EC2 인스턴스 생성 및 서버 환경 구축 (Ubuntu + Java 21)
- [x] 배포 스크립트(`deploy.sh`) 작성 및 백그라운드 실행 처리
- [x] 카카오 메시지 템플릿 직렬화 오류 수정
- [x] 카카오 로그인 및 메시지 전송 동작 확인

## 배포주소
- EC2 서버 주소: http://3.34.126.196:8080
- 시작 페이지: [http://3.34.126.196:8080/admin/products](http://3.34.126.196:8080/admin/products)

## 🛠 배포 스크립트 (`deploy.sh`)

```bash
#!/bin/bash

JAR_NAME="spring-gift-0.0.1-SNAPSHOT.jar"

# 실행 중인 프로세스 종료
PID=$(pgrep -f $JAR_NAME)

if [ -n "$PID" ]; then
  echo ">> 기존 실행 중: $PID → 종료"
  kill -15 $PID
  sleep 5
else
  echo ">> 실행 중인 프로세스 없음"
fi

# 백그라운드 실행
echo ">> 새 애플리케이션 실행"
nohup java -jar $JAR_NAME > /dev/null 2>&1 &
echo 
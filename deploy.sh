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

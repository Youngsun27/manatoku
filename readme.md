# Manatoku Spring Project

## 배포링크 
https://manatoku.today


## 실행 전 필수 설정

`src/main/resources/db.properties` 파일을 직접 생성해주세요.

아래 내용을 복사해서 본인 DB 정보로 수정해주세요.
```properties
db.driver=oracle.jdbc.driver.OracleDriver
db.url=jdbc:oracle:thin:@DB주소:1521:xe
db.username=DB아이디
db.password=DB비밀번호
```

## 실행 방법

1. 레포지토리 clone
2. `db.properties` 파일 생성 후 DB 정보 입력
3. 서버 실행


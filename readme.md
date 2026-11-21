# Manatoku Spring Project

## URL
(http://manatoku.duckdns.org/)

## ログイン ID/PW
ID : manatoku
PW : a1234567!

## 配布前のチェック

`src/main/resources/db.properties` ファイルを直接作成してください。

以下の内容をコピーして、ご自身のDB情報に変更してください。
```properties
db.driver=oracle.jdbc.driver.OracleDriver
db.url=jdbc:oracle:thin:@DB주소:1521:xe
db.username=DB ID
db.password=DB PW
```

## 配布前のチェック2

1. 레포지토리 clone
2. `db.properties` 파일 생성 후 DB 정보 입력
3. 서버 실행


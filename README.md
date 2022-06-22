# 마일리지 적립 서비스

## 기술 스택
- Spring boot 2.7.0
- Java
- JDK 17
- JPA
- Swagger 3.0
- GCP SQL

## 실행 방법

### 1. IDE 에서 프로젝트 open
- 개발 IDE : IntelliJ
- (참고) vscode에서 실행하실 경우 dependency 설치가 따로 필요합니다. (lombok)

### 2. DB 설정
- GCP SQL을 사용하므로 별도 설치가 필요하지 않습니다.
#### username, password 설정
- ```src/main/resources/application.yml``` 파일을 열어, ```datasource:username``` ```datasource:password``` 에 사전에 전달드린 정보를 입력해주세요.
- (참고) DB 관리 도구(Dbeaver, HeidiSQL, etc...)로 접속하여 테이블을 확인할 수 있습니다.

### 3. API TEST
#### Swagger

실행 후, ```/swagger-ui/index.html``` 에 접속하여 테스트 가능합니다.
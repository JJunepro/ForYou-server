# ForYou Server

Spring Boot 기반 공통코드 관리 시스템

## 🚀 시작하기

### 1. 환경 설정

1. `application.properties.example` 파일을 복사하여 `application.properties` 생성
2. 데이터베이스 정보를 실제 값으로 수정

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

### 2. 데이터베이스 설정

`application.properties`에서 다음 값들을 수정하세요:

```properties
# AWS RDS PostgreSQL 설정
spring.datasource.url=jdbc:postgresql://YOUR_RDS_ENDPOINT:5432/YOUR_DATABASE?sslmode=require
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 4. API 테스트

- Health Check: `GET http://localhost:8080/api/public/health`
- 코드 그룹 목록: `GET http://localhost:8080/api/code-mst`
- 상세 코드 목록: `GET http://localhost:8080/api/code-dtl`

## 📁 프로젝트 구조

```
src/main/java/com/foryou/foryouserver/
├── entity/          # JPA 엔티티
├── repository/      # 데이터 접근 계층
├── service/         # 비즈니스 로직
├── controller/      # REST API 컨트롤러
├── dto/            # 데이터 전송 객체
└── config/         # 설정 클래스
```

## 🔧 기술 스택

- **Backend**: Spring Boot 3.5.5, Spring Data JPA, Hibernate
- **Database**: PostgreSQL (AWS RDS)
- **Build Tool**: Gradle
- **Lombok**: 코드 간소화
- **Frontend**: Vite + React + TypeScript (포트 5173)

## 🔒 보안

- `application.properties`는 `.gitignore`에 포함되어 Git에 업로드되지 않습니다
- 실제 데이터베이스 정보는 환경 변수나 별도 설정 파일로 관리하세요

## 📝 API 문서

### 코드 그룹 관리 (CodeMst)
- `POST /api/code-mst` - 코드 그룹 생성
- `GET /api/code-mst` - 모든 코드 그룹 목록
- `GET /api/code-mst/{grpCd}` - 특정 코드 그룹 조회
- `PUT /api/code-mst/{grpCd}` - 코드 그룹 수정
- `DELETE /api/code-mst/{grpCd}` - 코드 그룹 삭제

### 상세 코드 관리 (CodeDtl)
- `POST /api/code-dtl` - 상세 코드 생성
- `GET /api/code-dtl/{cd}` - 특정 상세 코드 조회
- `GET /api/code-dtl/group/{grpCd}` - 그룹별 상세 코드 목록
- `PUT /api/code-dtl/{cd}` - 상세 코드 수정
- `DELETE /api/code-dtl/{cd}` - 상세 코드 삭제

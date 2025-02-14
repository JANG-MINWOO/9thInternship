# 🚀 Internship Backend Project

## 📌 프로젝트 개요

Spring Boot 기반의 **JWT 인증 및 인가 시스템을 포함한 백엔드 프로젝트**입니다. Access Token과 Refresh Token을 활용한 인증 로직을 구현했으며, Spring Security, AOP, Interceptor 등의 기능을 통해 보안 및 로깅 기능을 추가하였습니다.

---

## 🛠️ 기술 스택

- **Backend**: Java 17, Spring Boot 3.4.1, Spring Security, JWT
- **Database**: MySQL (운영환경)
- **Build Tool**: Gradle
- **Testing**: JUnit 5, MockMvc
- **Logging**: AOP, Interceptor, SLF4J
- **API Docs**: Swagger

---

## 🔑 인증 및 보안

### 1️⃣ **JWT 기반 인증 & 인가**

- Access Token: 60분 유효기간, `Authorization` 헤더에 저장
- Refresh Token: 7일 유효기간, `HttpOnly` 쿠키에 저장
- Access Token 만료 시, Refresh Token으로 재발급 API 제공

### 2️⃣ **Spring Security 설정**

- `JwtAuthenticationFilter`: 로그인 시 JWT 발급
- `JwtAuthorizationFilter`: 요청 시 JWT 검증 및 사용자 인증 처리
- `UserDetailsServiceImpl`: 사용자 인증 정보 로드
- `BCryptPasswordEncoder`: 비밀번호 암호화 적용

---

## 📌 핵심 기능

### 🔹 **회원가입 & 로그인**

- `/signup`: 사용자 회원가입
- `/sign`: 로그인 및 JWT 발급

### 🔹 **JWT 토큰 관련 API**

- `/api/jwt/decode`: Access Token 디코딩
![Image](https://github.com/user-attachments/assets/02a74e44-6296-4c6c-b8ad-a4f99c31818b)
- `/api/jwt/refresh`: Refresh Token을 이용한 Access Token 재발급

### 🔹 **관리자 API (Interceptor 적용)**

- `/restricted-api/health`: 서버 상태 체크 API (관리자 전용)

---

## 📂 프로젝트 구조

```
📦 src/main/java/com/sparta/internship
 ├── 📂 config
 │   ├── 📜 WebSecurityConfig.java  # Spring Security 설정
 │   ├── 📜 JwtUtil.java  # JWT 토큰 생성 및 검증
 │   ├── 📜 JwtAuthenticationFilter.java  # 로그인 시 JWT 생성 필터
 │   ├── 📜 JwtAuthorizationFilter.java  # JWT 검증 필터
 │   ├── 📜 LoggingAspect.java  # AOP 기반 로깅 기능
 │   ├── 📜 RequestInterceptor.java  # Interceptor 기반 인증
 │   ├── 📜 WebConfig.java  # Spring MVC Interceptor 설정
 │
 ├── 📂 domain
 │   ├── 📂 user
 │   │   ├── 📜 User.java  # 사용자 엔티티
 │   │   ├── 📜 UserRole.java  # 사용자 역할 Enum
 │   │   ├── 📜 UserService.java  # 사용자 서비스 로직
 │   │   ├── 📜 UserRepository.java  # JPA 사용자 레포지토리
 │   │   ├── 📜 UserController.java  # 회원가입 & 로그인 API
 │   │
 │   ├── 📂 token
 │   │   ├── 📜 JwtController.java  # JWT 토큰 관련 API
 │   │   ├── 📜 JwtService.java  # JWT 토큰 검증 및 재발급 서비스
 │   │
 │   ├── 📂 admin
 │   │   ├── 📜 HealthCheckController.java  # 서버 상태 체크 API
 │   │   ├── 📜 HealthCheckService.java  # 헬스 체크 서비스
 │
 ├── 📂 test/java/com/sparta/internship
 │   ├── 📜 JwtUtilTest.java  # JWT 생성 및 검증 테스트
 │   ├── 📜 UserControllerTest.java  # 사용자 컨트롤러 테스트
 │   ├── 📜 UserControllerTest2.java  # 로그인 테스트
```

---

## 📝 테스트 및 검증

### 1️⃣ JWT 검증 테스트 

- `testCreateToken()`: Access Token 생성 테스트
- `testValidateToken()`: Access Token 검증 테스트
- `testAccessTokenExpiredAndRefreshTokenValid()`: Access Token 만료 후 Refresh Token 검증 테스트

### 2️⃣ 회원가입 & 로그인 테스트

- `signupSuccess()`: 회원가입 성공 테스트
- `loginSuccess()`: 로그인 성공 테스트 (`Authorization` 헤더 & `Refresh-Token` 쿠키 검증)
![Image](https://github.com/user-attachments/assets/37f20b3c-c051-4887-9b4e-56ff7d232f0b)

---

## 🚀 실행 방법

### 1️⃣ **애플리케이션 실행**

```bash
./gradlew bootRun
```

### 2️⃣ **Swagger API 문서 확인**

- `http://localhost:8080/swagger-ui/index.html`

### 3️⃣ **테스트 실행**

```bash
./gradlew test
```

---

## 🔗 API 명세

| Method | Endpoint                 | 설명                                    |
| ------ | ------------------------ | ------------------------------------- |
| `POST` | `/signup`                | 회원가입                                  |
| `POST` | `/sign`                  | 로그인 (Access Token & Refresh Token 발급) |
| `GET`  | `/api/jwt/decode`        | Access Token 디코딩                      |
| `POST` | `/api/jwt/refresh`       | Refresh Token으로 Access Token 재발급      |
| `GET`  | `/restricted-api/health` | 서버 상태 체크 (관리자 전용)                     |

---

## ✨ 개선 사항 및 업데이트 기록

- ✅ **AOP 로깅 기능 추가** (`LoggingAspect.java`)
![Image](https://github.com/user-attachments/assets/d274f54f-8651-4e5d-9110-e48a24585063)
- ✅ **Interceptor 기반 인증 추가** (`RequestInterceptor.java`)
- ✅ **Access Token & Refresh Token 저장 방식 변경 (헤더 + 쿠키)**
- ✅ **Refresh Token 검증 로직 개선**
- ✅ **JWT 만료 체크 로직 수정 (401 응답 반환)**

---

## 📌 결론

이 프로젝트는 **JWT 기반 인증을 활용한 보안 강화 API 서버**를 구축하는 예제입니다. Spring Security, AOP, Interceptor 등을 활용하여 **안전하고 확장 가능한 인증 시스템을 제공**하며, 테스트 코드 작성 및 API 문서화를 통해 유지보수성을 높였습니다.


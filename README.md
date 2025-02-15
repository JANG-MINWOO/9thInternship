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

- Access Token: 60분 유효기간, `Authorization` 헤더에 저장 -> 테스트를 위해 배포 환경에선 **유효기간 1분**으로 설정
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

### 1️⃣ **Swagger API 문서 확인**
- `http://localhost:8080/swagger-ui/index.html` **로컬환경**
- 
<br>

### 2️⃣ **회원가입 기능 예시**
![Image](https://github.com/user-attachments/assets/bd650708-c72c-4c9e-a9d5-453440780929)
- 회원가입 기능은 위 사진과 마찬가지로 원하는 이름, 비밀번호, 닉네임을 설정하여 입력하면 됩니다.
- 가입기능은 정상적으로 동작하지만, 편의를 위해 DB에 테스트용 ID 를 미리 생성해 두겠습니다.
- 회원가입 시, 기본적으로 **일반유저 권한**으로 가입이 진행됩니다.
- - ID : Admin (관리자 권한) (**현재 토큰관련 API 와 Health-Check API 를 관리자만 사용할 수 있도록 Interceptor 를 설정해두었습니다.**)
  - P/W : 12341234
  - ID : User (일반 유저 권한)
  - P/W : 12341234

### 3️⃣ **로그인 기능**
![Image](https://github.com/user-attachments/assets/7c8570e6-8220-452e-a043-42f3e9b4ea77)
- 로그인 시, 처음에는 전체 API의 원활한 테스트를 위해 `Admin/12341234`계정으로 입력 부탁드립니다.
<br>

![Image](https://github.com/user-attachments/assets/9bda343e-eac3-44a2-9562-665dfeb3adcf)
- 로그인 성공 시, `Access Token` 과 `Refresh Token` 이 반환됩니다.
- *중요* : *Access Token 의 "Bearer " 를 제외한 `eyJhbGciOiJIUzI1NiJ9...` 부분을 복사하여, Swagger 최상단 Authorize 버튼을 누르고 입력해주셔야 API 에 접근가능합니다.*
- *중요2* : *Test 를 위해 임의로 Access Token 의 **만료 시간을 1분으로** 설정하였습니다. 아래의 기능을 수행하는데는 충분한 시간입니다.*
<br>

### 4️⃣ **Access Token 디코딩 & Health Check API**
![Image](https://github.com/user-attachments/assets/6e062ef2-91f6-411a-8706-6fcfda3b8085)
- 위 사진의 두 기능은 별다른 입력값이 없습니다. `Try it out` 버튼을 클릭한 뒤 요청을 보내시면 확인할 수 있습니다.
<br>

### 5️⃣ **Access Token 만료 후 Refresh Token 으로 재발급**
- 1분이 지나게 되면 Access Token 이 만료되므로, `Authorize` 에 등록해놓은 토큰값으로는 API 를 정상적으로 실행할 수 없습니다.
- 상단의 사용자 로그인시 같이 반환 받은 `refreshToken: "Bearer eyJhbGciOiJIUzI1NiJ9..."` 부분을 확인합니다.
- 프로젝트의 비즈니스 로직에 따라, `Refresh Token` 은 쿠키에 `Refresh-Token` 이라는 `Key` 값으로 저장되게 되어있습니다.
- 따라서, Access Token 재발급 API 의 파라미터 - Cookie 부분에 아래 사진과 같이 입력합니다.
![Image](https://github.com/user-attachments/assets/c8f01c59-e1ee-4379-a2c4-e6c669e48c3c)
- 입력란에 `Refresh-Token=Bearer <토큰값>` 형식으로 입력하면 되고, 토큰 값은 로그인시 반환받은 Refresh Token 을 참고하시면 됩니다.
- *중요* : *잊지 않고, 새롭게 재발급 받은 Access Token 을 **Authorize 버튼에 재등록** 해주셔야 합니다.*


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


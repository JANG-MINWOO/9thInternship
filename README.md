
# Spring Security 기반 회원가입 및 로그인 프로젝트

## 프로젝트 개요

Spring Security를 활용하여 회원가입 및 로그인 기능을 구현한 프로젝트입니다. 이 프로젝트는 JWT(Json Web Token)를 사용하여 인증 및 인가를 처리하며, EC2에 배포하여 실제 환경에서 동작할 수 있도록 구성되었습니다.

---

## 주요 기능

### 1. 회원가입
- 사용자는 `username`, `password`, `nickname`을 입력하여 회원가입을 진행합니다.
- 비밀번호는 `BCryptPasswordEncoder`를 사용하여 암호화된 상태로 저장됩니다.
- 유효성 검사를 통해 중복된 `username`이나 형식에 맞지 않는 `password`를 방지합니다.

**회원가입 API**:
- **URL**: `/signup`
- **HTTP Method**: POST
- **Request Body**:
  ```json
  {
    "username": "testuser",
    "password": "Test@1234",
    "nickname": "nickname"
  }
  ```
- **Response**:
  ```json
  {
	"username": "JIN HO",
	"nickname": "Mentos",
	"authorities": [
          {
            "authorityName": "ROLE_USER"
          }
	  ]		
  }
  ```

### 2. 로그인
- 사용자는 `username`과 `password`를 입력하여 로그인을 시도합니다.
- 성공적으로 로그인하면 JWT 토큰이 발급됩니다.
- 실패 시 적절한 에러 메시지를 반환합니다.

**로그인 API**:
- **URL**: `/sign`
- **HTTP Method**: GET
- **Request Body**:
  ```json
  {
    "username": "testuser",
    "password": "Test@1234"
  }
  ```
- **Response (성공)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
  ```
- **Response (실패)**:
  ```json
  {
    "error": "로그인에 실패했습니다. 사용자 이름 또는 비밀번호를 확인해주세요."
  }
  ```

### 3. 인증 및 인가
- **JwtAuthenticationFilter**: 로그인 시도 시 사용자 인증을 처리합니다.
- **JwtAuthorizationFilter**: 모든 요청에서 JWT 토큰을 검증하고, 사용자 권한을 설정합니다.

---

## 코드 개선 사항
AI를 통해 다음과 같은 개선 작업을 진행했습니다:

1. **필터 예외 처리 강화**:
   - `JwtAuthenticationFilter`에서 인증 실패 시 JSON 응답과 상세 에러 메시지를 반환하도록 개선.
   - Spring Security 기본 HTML 응답을 대체.

2. **테스트 환경 개선**:
   - `@SpringBootTest`와 `@AutoConfigureMockMvc`를 활용하여 Spring Security 필터 체인과 통합된 테스트 환경 구축.
   - 회원가입 및 로그인 테스트 작성.

3. **테스트 신뢰성 향상**:
   - Mock 데이터를 활용한 단위 테스트와 실제 DB를 활용한 통합 테스트를 분리.

---

## 테스트 코드

### 회원가입 테스트
```java
@Test
@DisplayName("회원가입 - 성공")
void signupSuccess() throws Exception {
    // Given
    SignupRequest request = new SignupRequest();
    request.setUsername("testuser");
    request.setPassword("Test@1234");
    request.setNickname("nickname");

    // When & Then
    mockMvc.perform(post("/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk());
}
```

### 로그인 테스트
```java
@Test
@DisplayName("로그인 성공 테스트")
void loginSuccess() throws Exception {
    // Given
    SigninRequest signinRequest = new SigninRequest();
    signinRequest.setUsername("testuser");
    signinRequest.setPassword("Test@1234");

    // When & Then
    mockMvc.perform(post("/sign")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(signinRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty());
}
```

---

## 배포
EC2 인스턴스에 애플리케이션을 배포하고, 어플리케이션 심사기간동안은 EC2 인스턴스가 항시 실행중입니다.

테스트를 다음 주소를 열어 요청을 보낼 수 있습니다.

https://documenter.getpostman.com/view/38576841/2sAYQanBmX

배포용 회원가입과 로그인을 통해 테스트 가능합니다.

---

## 기술 스택

- **Spring Boot**: 3.4
- **Spring Security**: 인증 및 인가 처리
- **JWT**: 인증 토큰 관리
- **MySQL**: 데이터베이스
- **Hibernate**: ORM
- **JUnit**: 테스트 프레임워크

---

## 개선 및 학습 포인트

- Spring Security와 JWT를 활용한 인증 및 인가 구현.
- 테스트 신뢰성을 높이기 위해 통합 테스트 환경 구성.
- AWS EC2 배포를 통해 실제 환경에서 애플리케이션 동작 확인.


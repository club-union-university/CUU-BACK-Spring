# CUU-BACK-Spring

Club Union University(CUU) 백엔드 API — 동아리·행사·학교 게시판·알림 등을 제공하는 **Spring Boot** 서비스입니다.

## 스택

| 구분 | 내용 |
|------|------|
| 런타임 | Java **17** |
| 프레임워크 | Spring Boot **4.0.x** (Web MVC, Data JPA, Security) |
| DB (기본) | **H2** 인메모리 (`jdbc:h2:mem:crew`), `data.sql` 초기화 |
| 인증 | **Firebase ID Token** 검증 후 자체 **JWT** 발급 (`jjwt`) |
| 외부 연동 | 행사 AI 단계용 **Nest(Gemini) 서버** HTTP 프록시 (`AiClient` → `ai.node-server.base-url`) |

## 도메인 개요

- **`/api/auth`** — Firebase 로그인·회원가입, 내 정보 (`/me`)
- **`/api/clubs`** — 동아리 CRUD, 멤버십
- **`/api/events`** — 행사 생명주기, 참가 신청·승인, `/ai/step1`·`/ai/step2` (Nest로 바디 포워딩)
- **`/api/posts`**, **`/api/events/{id}/posts`**, **`/api/clubs/{id}/posts`**, **`/api/schools/{id}/posts`** — 게시글·댓글
- **`/api/schools`** — 학교·시설 (GET 계열은 인증 없이 허용)
- **`/api/notifications`**, **`/api/users`** — 알림·사용자

공개 엔드포인트는 `SecurityConfig` 기준으로 `/api/auth/login`, `/api/auth/signup`, `GET /api/schools/**`, H2 콘솔 등입니다.

## 로컬 실행

```bash
./gradlew bootRun
```

기본 포트는 **8080**입니다. Docker 이미지는 `PORT` 환경변수로 바인딩합니다.

### H2 콘솔

개발 프로필 기준으로 콘솔이 켜져 있으며 경로는 **`/h2-console`** 입니다. JDBC URL은 `application.properties`의 `spring.datasource.url`과 동일하게 맞추면 됩니다.

## 환경 변수

| 변수 | 설명 |
|------|------|
| `JWT_SECRET` | JWT 서명 키 (Spring relaxed binding → `jwt.secret`). 프로덕션에서는 **반드시** 강한 비밀값으로 설정 |
| `JWT_EXPIRATION_MS` | 선택, 토큰 만료(ms) — `jwt.expiration-ms` |
| `AI_NODE_SERVER_BASE_URL` | Nest AI 서버 베이스 URL. 미설정 시 기본값은 Railway 배포 URL(`/api` 프리픽스 포함). 자세한 값은 `application.properties` 참고 |

Firebase Admin SDK 사용 시 서비스 계정 JSON 경로 등은 `Firebase` 초기화 코드·배포 환경에 맞게 구성해야 합니다.

## CORS

`SecurityConfig`에서 **`/api/**`** 에 대해 허용된 Origin 예시:

- `http://localhost:5173` (Vite 로컬)
- `https://cuu-web.vercel.app`

추가 프론트 도메인은 동일 설정에 Origin을 넣어야 합니다.

## Docker

```bash
docker build -t cuu-back-spring .
docker run -p 8080:8080 -e JWT_SECRET=your-secret cuu-back-spring
```

멀티스테이지 빌드로 `bootJar` 후 JRE 17 이미지에서 실행합니다.

## Railway

[`railway.toml`](railway.toml) 에서 **Dockerfile** 빌더를 사용합니다. 레포 루트 `Dockerfile` 기준으로 배포하면 됩니다.

## 관련 저장소

- 프론트엔드: [CUU-FRONT-React](https://github.com/club-union-university/CUU-FRONT-React)
- AI(Nest) 서버: 프로젝트 내 `ai.node-server.base-url` 로 연결 (예: Railway에 배포된 `cuu-back-ai-production` 인스턴스)

## 라이선스

조직 정책에 따릅니다. 별도 LICENSE 파일이 없으면 저장소 관리자에게 문의하세요.

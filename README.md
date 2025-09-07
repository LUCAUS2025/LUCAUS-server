# LUCAUS2025 (server)
<div align="center">
  <img width="1372" height="569" alt="LUCAUS" src="https://github.com/user-attachments/assets/22f1f1fa-f7c9-4095-860b-4a31bb738188" />
</div>

## Summary

**2025년 중앙대학교 봄 축제**를 위한 공식 웹사이트의 **백엔드**입니다.  
 축제 운영에 필요한 다양한 서비스들을 통합하여 **정보 제공**, **방문자 참여**, **데이터 분석**을 원활하게 지원하도록 설계되었습니다.

---

### 프로젝트 목적

- 축제 참여자에게 **실시간 안내** 및 **편리한 정보 접근** 제공
- 운영팀을 위한 **로깅, 트래픽 대응, 통계 수집** 기반 마련
  
---

### 핵심 기능

- 공지사항, 분실물, 도장판, 부스/푸드트럭 운영 정보 등 **REST API** 제공  
- **Notion 기반 CMS** 연동으로 실시간 콘텐츠 관리   
- **Redis 캐시** 및 **Navite Query** 를 활용한 성능 최적화  
- **AWS 기반 인프라 배포** 및 **GitHub Actions 기반 CI/CD 자동화**  

---
## Contributors ✨

<div align=center>
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/eunsu02">
        <img src="https://avatars.githubusercontent.com/u/101704889?v=4" width="200px" alt="eunsu02"/>
        <br />
        <strong>@eunsu02</strong>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kimtree24">
        <img src="https://avatars.githubusercontent.com/u/152477481?v=4" width="200px" alt="kimtree24"/>
        <br />
        <strong>@kimtree24</strong>
      </a>
    </td>
  </tr>
  <tr>
    <td align="center"><b>백엔드 개발</b></td>
    <td align="center"><b>백엔드 개발</b></td>
  </tr>
  <tr>
    <td>
      <ul align="left">
        <li>공지사항/분실물 API</li>
        <li>노션DB 데이터파이프라인 구축</li>
        <li>AWS EC2/RDS/S3, Redis, Swagger 구축</li>
        <li>트래픽 대응 방안 구축 및 설계</li>
      </ul>
    </td>
    <td>
      <ul align="left">
        <li>부스/푸드트럭/광장기획전 API</li>
        <li>로그 수집 및 데이터 분석용 파이프라인 설계</li>
        <li>응답 코드 체계 설계 및 커스텀 예외 처리</li>
        <li>CI/CD 구축</li>
      </ul>
    </td>
  </tr>
</table>

</div>

---
## Architecture
<p align="center">
  <img src="https://github.com/user-attachments/assets/37af9c83-7818-40f2-81ef-8b261d9343ef" alt="Infrastructure Diagram" width="700" />
</p>

### 클라이언트 요청 처리 흐름

1. **클라이언트**는 `Route 53`을 통해 도메인 이름으로 서버에 접근합니다.
2. `AWS Certificate Manager`는 HTTPS 연결을 위한 SSL 인증서를 제공합니다.
3. `AWS WAF`(Web Application Firewall)는 **악의적인 IP 차단**, **비정상 요청 필터링** 등의 보안 기능을 수행합니다.
4. `Elastic Load Balancing`은 여러 `EC2 인스턴스`로 트래픽을 분산시켜 **부하 분산 및 장애 대응**을 수행합니다.

---

### 서버 및 스케일링 구성

- **EC2 인스턴스**에 Spring Boot 백엔드 서버와 Redis 캐시 서버가 실행됩니다.
- `AWS Auto Scaling`이 서버의 부하를 모니터링하고, **트래픽 증가 시 인스턴스를 자동으로 확장**합니다.
- `AWS RDS`를 통해 MySQL 기반의 안정적인 관계형 DB를 제공합니다.

---

### 로깅 및 모니터링 파이프라인

- `AWS CloudWatch`는 서버 로그, API 응답 시간, 에러 발생 등을 모니터링합니다.
- 로그는 `AWS S3`에 주기적으로 저장됩니다 (`crontab + shell script` 기반 자동 업로드).
- 저장된 로그는 `Amazon EventBridge`를 통해 이벤트 기반으로 처리됩니다.
- **이상 징후 탐지 시 `AWS Lambda`가 Slack 알림을 발송**해 운영팀에 실시간 공유합니다.

---

### CI/CD 및 보안 자동화

- `GitHub Actions`를 통해 CI/CD 파이프라인을 구성하여, **코드 변경 시 자동 배포**가 이루어집니다.
- **IP 차단 시스템**은 비정상 요청이 감지되면 WAF에 자동으로 IP를 등록하여 차단합니다.
- Slack 알림을 통해 운영자에게 주요 이벤트 알림을 전달합니다.

---

## 운영 자동화 및 보안 기능

### IP 자동 차단 시스템
- 일정 시간 내 다량 요청(IP 기준)이 감지되면 해당 IP를 자동 차단
- Spring 서버 로그를 실시간 분석하여 공격/오류 패턴 탐지
- `Blacklist`를 관리하여 악성 IP의 접근을 선제적으로 차단
- 교내 IP의 경우 `WhiteList`로 관리하여 접근 허용

---

### API 로그 수집 및 주기적 업로드
- 쉘 스크립트를 통해 API 로그를 정기적으로 AWS S3에 업로드
- 요청/응답 로그 기반 사용자 행동 분석 및 성능 모니터링 기반 마련

---

### 주기적 작업 스케줄링 (Crontab)
- `crontab` 등록을 통해 자동 IP 차단 및 로그 업로드 스크립트 주기 실행
- 시스템 상태 점검 및 로그 누락 방지를 위한 자동화 설정

---

### 장애 및 이벤트 모니터링 (Slack 연동)
- 주요 서버 이벤트 발생 시 Slack 채널로 실시간 전송
- IP 차단, 로그 업로드 실패, 시스템 에러 등 운영팀 실시간 대응 가능

---

### 고가용성 및 오토스케일링 인프라
- EC2 오토스케일링 그룹 구성으로 트래픽 증가 시 인스턴스 자동 추가
- 정적 리소스는 S3 + CloudFront(CDN)를 통해 빠르고 안정적으로 제공
- 로깅 및 차단 기능은 모든 인스턴스에서 병렬로 작동 가능

---

### 기술 스택 활용 포인트

- **Spring Boot 3 + JPA + MySQL**로 안정적인 데이터 처리  
- **AWS EC2 / RDS / S3 + Redis** 기반 운영 환경 구축  
- **Swagger 기반 API 문서 자동화**로 협업 및 테스트 용이  
- **로그 기반 유저 행동 분석 파이프라인 설계**  

---

## 폴더 구조

<summary> server/ 폴더 구조</summary>

<pre><code>
├── .github/                  # GitHub 이슈/PR 템플릿 & Actions 설정
│   ├── ISSUE_TEMPLATE/
│   ├── workflows/
│   └── PULL_REQUEST_TEMPLATE.md
├── gradle/                   # Gradle Wrapper 관련 파일
├── src/
│   ├── main/
│   │   ├── java/com/likelion13/lucaus_api/
│   │   │   ├── aop/              # AOP (로깅, 필터링 등)
│   │   │   ├── common/           # 설정, 예외, 응답, 유틸 등 공통 모듈
│   │   │   ├── controller/       # REST API 컨트롤러
│   │   │   ├── domain/           # Entity 및 Repository
│   │   │   ├── dto/              # Request/Response DTO
│   │   │   ├── enums/            # 열거형 상수
│   │   │   ├── security/         # JWT 기반 인증 처리
│   │   │   └── service/          # 비즈니스 로직
│   │   └── resources/
│   │       └── application.yml  # 설정 파일
│   └── test/                   # 테스트 코드
├── build.gradle
├── gradlew / gradlew.bat
├── settings.gradle
└── README.md
</code></pre>

---

## ERD

<table>
  <tr>
    <td align="center"><b>메인 엔티티</b></td>
    <td align="center"><b>광장기획전 엔티티</b></td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/90bf1376-dc83-4039-acea-2513eefb2f10" width="500" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/29b64910-5c02-4204-b532-8fb13d2b7c28" width="500" />
    </td>
  </tr>
</table>

---

## Branch Convention

이 프로젝트는 기능 단위 개발 → dev 브랜치 통합 → main 배포 흐름을 따릅니다. 브랜치 네이밍 컨벤션은 다음과 같습니다:

| 브랜치 타입    | 용도 |
|----------------|------|
| `main`         | 실제 운영 배포용 브랜치입니다. |
| `dev`          | 기능 브랜치들이 통합되는 개발 브랜치입니다. |
| `feat/*`       | 새로운 기능을 개발할 때 사용하는 브랜치입니다. 개발 완료 후 `dev` 브랜치에 merge 됩니다. |
| `fix/*`        | 버그나 기능 보완 작업 시 사용하는 브랜치입니다. |
| `hot-fix/*`    | 운영 중인 `main` 브랜치에 직접 영향을 주는 긴급 수정 사항에 사용됩니다. |
| `setting/*`    | 설정 관련 디렉토리 구조/CI 구성 등을 위한 브랜치입니다. |
| `docs/*`       | 문서 작업용 브랜치입니다. |
| `chore/*`      | 빌드, 패키지 등 유지보수용 브랜치입니다. |
| `test/*`       | 테스트 목적의 브랜치입니다. |

---

## Commit Convetion

| 타입       | 설명                                                                 | 예시 |
|------------|----------------------------------------------------------------------|------|
| `feat`     | 새로운 기능 구현                                                      | `feat: 구글 로그인 API 기능 구현 - #11` |
| `fix`      | 코드 오류 수정                                                       | `fix: 회원가입 비즈니스 로직 오류 수정 (#10)` |
| `del`      | 불필요한 코드 삭제                                                   | `del: 불필요한 import 제거 (#12)` |
| `docs`     | 문서 작성 또는 수정 (`README`, `wiki`, etc.)                         | `docs: 리드미 수정 (#14)` |
| `refactor` | 기능 변경 없이 코드 리팩토링 (성능 개선, 가독성 향상 등)             | `refactor: 코드 로직 개선 (#15)` |
| `chore`    | 빌드 설정, 패키지 매니저, yml 설정 등 변경                            | `chore: yml 수정 (#21)`<br>`chore: lombok 의존성 추가 (#22)` |
| `test`     | 테스트 코드 추가 및 수정                                              | `test: 로그인 API 테스트 코드 작성 (#20)` |
| `setting`  | 프로젝트 초기 세팅, 환경설정 파일 추가 등                            | `setting: 프로젝트 초기 세팅` |
| `merge`    | 브랜치 병합 커밋 (주로 conflict 해결 시)                              | `merge: dev 브랜치 병합` |

---

## Teck Stack ✨

| 항목                 | 사용 기술 및 설명                                                                 |
|----------------------|------------------------------------------------------------------------------|
| **IDE**              | IntelliJ IDEA                                                                |
| **Language**         | Java 17                                                                      |
| **Framework**        | Spring Boot 3.3.1, Gradle                                                    |
| **ORM**              | Spring Data JPA, Native Query              |
| **Database**         | MySQL (AWS RDS)                                                              |
| **Authentication**   | Spring Security, JWT (jjwt)                                                  |
| **Cache**            | Redis (Spring Cache + Jedis)                                                 |
| **Infra**            | AWS EC2 (오토스케일링 구성), AWS RDS, Amazon CloudFront (CDN 적용)           |
| **WAF**              | Python 기반 WAF 시스템 (로그 분석 및 차단 자동화 스크립트 활용)     |
| **Monitoring**       | Slack Webhook 연동 (서버 이벤트 및 차단 알림 전송), CloudWatch Logs           |
| **Task Scheduler**   | Crontab을 활용한 로그 정기 업로드 및 분석 스크립트 자동 실행              |
| **CI/CD**            | GitHub Actions                                                               |
| **API Docs**         | Swagger (springdoc-openapi), Notion                  |
| **External Libs**    | AWS SDK (S3 업로드용), Lombok, Validation API                                 |
| **Tools**            | Postman, Figma, Discord, slack |

<br>

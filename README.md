# LUCAUS-server
![2025 LUCAUS 로고 초안_0326](https://github.com/user-attachments/assets/d5f2f5c8-0f7d-4c61-9d46-4b3828b957c4)


<br>

## 설명
<aside>
중앙대 2025 봄 축제 LUCAUS 웹사이트의 서버입니다.
</aside>

<br/><br/>

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
        <li>부스 API</li>
        <li>도장판 API</li>
        <li>로그인 기능 구현</li>
        <li>CI/CD 구축</li>
      </ul>
    </td>
  </tr>
</table>

</div>
<hr></hr>


## Architecture

<br>
<img width="2182" height="1960" alt="루카우스 drawio" src="https://github.com/user-attachments/assets/37af9c83-7818-40f2-81ef-8b261d9343ef" />

<hr></hr>

## 📁 폴더 구조

**주요 디렉토리 설명:**
- **aop/**: 로깅, 접근 제한, 실행 시간 측정 등 횡단 관심사 처리
- **common/**: 전역 설정, 예외 처리, API 응답 형식 등 공통 기능
- **controller/**: REST API 엔드포인트 정의 (부스, 분실물, 공지사항, 도장판 등)
- **domain/**: 데이터베이스 엔티티와 Repository 인터페이스
- **dto/**: 클라이언트와 서버 간 데이터 전송을 위한 객체
- **security/**: JWT 기반 인증 및 보안 처리
- **service/**: 핵심 비즈니스 로직 구현
```
server/
├── .github/ # GitHub 템플릿 및 CI/CD 설정
│ ├── ISSUE_TEMPLATE/ # 이슈 템플릿
│ ├── workflows/ # GitHub Actions 워크플로우
│ └── PULL_REQUEST_TEMPLATE.md # PR 템플릿
├── gradle/ # Gradle Wrapper 파일
├── src/
│ ├── main/
│ │ ├── java/com/likelion13/lucaus_api/
│ │ │ ├── aop/ # AOP 관련 클래스 (로깅, 필터링)
│ │ │ ├── common/ # 공통 설정 및 유틸리티
│ │ │ │ ├── config/ # 설정 클래스 (Security, Redis, S3, Swagger 등)
│ │ │ │ ├── exception/ # 예외 처리 관련
│ │ │ │ ├── response/ # API 응답 형식
│ │ │ │ └── util/ # 유틸리티 클래스
│ │ │ ├── controller/ # REST API 컨트롤러
│ │ │ ├── domain/ # 도메인 계층
│ │ │ │ ├── entity/ # JPA 엔티티
│ │ │ │ │ ├── booth/ # 부스 관련 엔티티
│ │ │ │ │ ├── detailedNotices/ # 상세 공지사항 엔티티
│ │ │ │ │ ├── foodTruck/ # 푸드트럭 관련 엔티티
│ │ │ │ │ ├── lostItems/ # 분실물 엔티티
│ │ │ │ │ ├── shortNotices/ # 간단 공지사항 엔티티
│ │ │ │ │ ├── stamp/ # 도장판 관련 엔티티
│ │ │ │ │ └── visitor/ # 방문자 수 엔티티
│ │ │ │ └── repository/ # JPA Repository 인터페이스
│ │ │ ├── dto/ # 데이터 전송 객체
│ │ │ │ ├── Notion/ # Notion API 연동 DTO
│ │ │ │ ├── request/ # 요청 DTO
│ │ │ │ └── response/ # 응답 DTO
│ │ │ ├── enums/ # 열거형 클래스
│ │ │ ├── security/ # JWT 인증 관련
│ │ │ ├── service/ # 비즈니스 로직 서비스
│ │ │ │ ├── auth/ # 인증 서비스
│ │ │ │ ├── booth/ # 부스 서비스
│ │ │ │ ├── detailedNotices/ # 상세 공지사항 서비스
│ │ │ │ ├── foodTruck/ # 푸드트럭 서비스
│ │ │ │ ├── lostItems/ # 분실물 서비스
│ │ │ │ ├── notion/ # Notion 연동 서비스
│ │ │ │ ├── s3/ # AWS S3 서비스
│ │ │ │ ├── shortNotices/ # 간단 공지사항 서비스
│ │ │ │ ├── stamp/ # 도장판 서비스
│ │ │ │ ├── user/ # 사용자 서비스
│ │ │ │ └── visitor/ # 방문자 수 서비스
│ │ │ └── LucausApiApplication.java # 메인 애플리케이션 클래스
│ │ └── resources/
│ │ └── application.yml # 애플리케이션 설정 파일
│ └── test/ # 테스트 코드
├── build.gradle # Gradle 빌드 설정
├── gradlew # Gradle Wrapper (Unix/Linux)
├── gradlew.bat # Gradle Wrapper (Windows)
├── README.md # 프로젝트 문서
└── settings.gradle # Gradle 프로젝트 설정
```

<hr></hr>

## ERD
<img width="765" height="549" alt="스크린샷 2025-09-04 오후 5 22 34" src="https://github.com/user-attachments/assets/29b64910-5c02-4204-b532-8fb13d2b7c28" />
<img width="1082" height="816" alt="스크린샷 2025-09-04 오후 5 22 15" src="https://github.com/user-attachments/assets/90bf1376-dc83-4039-acea-2513eefb2f10" />


<hr></hr>

## Branch Convention

- `main` : 프로덕트를 배포하는 브랜치입니다.
- `dev`: 프로덕트 배포 전 기능을 개발하는 브랜치입니다.
- `feat`: 단위 기능을 개발하는 브랜치로 단위 기능 개발이 완료되면 develop 브랜치에 merge 합니다.
- `hotfix`: main 브랜치로 프로덕트가 배포 된 이후 이슈가 발생했을 때 이를 긴급하게 해결하는 브랜치입니다.

EX: 

<hr></hr>

## Commit Convetion
- **feat** : 새로운 기능 구현 `feat: 구글 로그인 API 기능 구현 - #11`
- **fix** : 코드 오류 수정 `fix: 회원가입 비즈니스 로직 오류 수정 (#10)`
- **del** : 불필요한 코드 삭제 `del: 불필요한 import 제거 (#12)`
- **docs** : README나 wiki 등의 문서 개정 `docs: 리드미 수정 (#14)`
- **refactor** : 내부 로직은 변경 하지 않고 기존의 코드를 개선하는 리팩터링 `refactor: 코드 로직 개선 (#15)`
- **chore** : 의존성 추가, yml 추가와 수정, 패키지 구조 변경, 파일 이동 등의 작업 `chore: yml 수정 (#21)`, `chore: lombok 의존성 추가 (#22)`
- **test**: 테스트 코드 작성, 수정 `test: 로그인 API 테스트 코드 작성 (#20)`
- **setting**: 세팅
- **merge**: 머지

EX: [feat] 로그인 기능 구현

<br>

<hr></hr>


## Teck Stack ✨

| 항목                 | 사용 기술                                |
| ------------------ | ------------------------------------ |
| **IDE**            | IntelliJ IDEA                        |
| **Language**       | Java 17                              |
| **Framework**      | Spring Boot 3.3.1, Gradle            |
| **ORM**            | Spring Data JPA                      |
| **Database**       | MySQL (AWS RDS)                      |
| **Authentication** | Spring Security, JWT (jjwt)          |
| **Cache**          | Redis (Spring Cache + Jedis)         |
| **Infra**          | AWS EC2, AWS RDS                     |
| **CI/CD**          | GitHub Actions                       |
| **API Docs**       | Swagger (springdoc-openapi), Notion  |
| **External Libs**  | AWS SDK (S3), Lombok, Validation API |
| **Tools**          | Postman, Figma, Discord              |


<br>

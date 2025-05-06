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

<hr></hr>

## 📁 폴더 구조

```

```

<hr></hr>

## ERD


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

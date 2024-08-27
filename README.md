# YelloBook - 옐로북 <a href="https://yellobook.site"><img src="./images/logo.png" align="left" width="100"></a>

[![codecov](https://codecov.io/gh/Yellobook/yellobook-backend/branch/develop/graph/badge.svg?token=ZDMLLQDDTN)](https://codecov.io/gh/Yellobook/yellobook-backend)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Yellobook_yellobook-backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Yellobook_yellobook-backend)
[![CI Status](https://github.com/Yellobook/yellobook-backend/actions/workflows/ci.yml/badge.svg)](https://github.com/Yellobook/yellobook-backend/actions)
[![CD Status](https://github.com/Yellobook/yellobook-backend/actions/workflows/cd.yml/badge.svg)](https://github.com/Yellobook/yellobook-backend/actions)
 
## 1. 기술 스택

### Backend
- Java 17
- Gradle 8.8
- Spring Boot 3.3.1
- Postgres 16.x
- Redis 7.x
- Spring Data JPA
- QueryDSL
- JUnit5
- Mockito

### Infra

- AWS
- Docker
- Github Actions CI/CD
- Nginx

## 2. 협업툴

<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">   
<img src="https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=jira&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">


## 3. 모듈 구조

- api: 애플리케이션 모듈
- domain: 도메인 모듈
- common: 공통 모듈

### 모듈 설명 및 역할

- api
  - 독립적으로 실행 가능한 애플리케이션 모듈
  - 설계한 모든 모듈을 의존하여 실행한다.
  - SpringBoot 애플리케이션을 실행하며, 사용자 요청을 처리하고, 비즈니스 로직을 수행한다.
- domain:
  - 엔티티정보와 관련된 비즈니스 로직을 담고 있는 모듈
  - JPA Entity 와 Repository가 정의되며, 데이터베이스와의 상호작용을 담당한다.
- common
  - 애플리케이션에서 공통적으로 사용되는 설정을 모아둔 모듈
  - 공통 응답 포맷, 공통 에러코드, 공통 예외 처리 핸들러, 공통으로 사용되는 상수, 유틸리티 클래스, 로깅 설정을 포함한다.


## 4. 개발 프로세스

Jira 의 Scrum 기반 개발 프로세스를 이용한다.

백로그 구성 → 작업 할당 → 스프린트진행 → 스프린트 회고

- 스토리(Story): 비즈니스적인 기능
- 태스크(Task):  설계, 인프라 구성 등 관리적 업무

스토리와 태스크 아래에 적절한 서브태스크를 배치해 작업 흐름을 파악할 수 있도록 한다.

1. 지라 티켓 기반 branch  생성 (`feature/티켓번호`)
2. 개발 완료시 PR
3. develop merge : 코드 리뷰 / approve 1명 이상 완료시


## 5. 버전 관리 규칙

### Commit/PR 접두사 규칙

- `feat/[Feature]`: 새로운 기능 추가
- `fix/[Fix]`: 버그 수정
- `hotfix/[HotFix]`: 긴급 수정
- `docs/[Docs]`: 문서 수정
- `style/[Style]`: 코드 포맷팅등 코드 변경이 없는 경우
- `refactor/[Refactor]`: 코드 리팩토링
- `test/[Test]`: 테스트 코드, 리팩토링 테스트 코드 추가
- `chore/[Chore]`: 빌드 업무 수정, 패키지 매니저 수정
-  `merge`: 브랜치 병합, 병합 충돌 해결
 

### Commit 규칙

커밋은 `접두사(티켓번호): 내용` 형식으로 한다. (Jira 의 서브태스크에 해당한다.)

```bash
feat(YB-2): accessToken 발급로직 구현
```

### PR 규칙

PR 제목은 `[접두사/티켓번호]: 내용`  형식으로 한다. (Jira 의 스토리/태스크에 해당한다.)

하나의 PR 에 너무 많은 작업을 담는 것을 피한다.  
PR 이 포함하고 있는 작업 내용에 대해서는 Label 을 붙인다.  
접두사는 주된 작업을 기준으로 선택한다. 
- 새로운 기능을 추가하면서 해당 기능의 테스트도 함꼐 추가하는 경우 [Feature/티켓 번호]: 내용
- 기존 기능에 대한 테스트를 추가하면서, 소규모 리팩토링을 함께 진행하는 경우 [Test/티켓번호]: 내용
 
```bash
[Feature/YB-1]: 소셜 로그인 기능 구현
```

본인 이외 PR 에 기여한 사람이 있다면 공동작업자로 추가한다.

ex) 데이비드가 PR을 올리고, 그리/테리가 함께 작업한 경우 PR 을 올릴 때 아래처럼 추가한다.
```bash
[Feature/YB-1]: 소셜 로그인 기능 구현

Co-Authored-By: Ywoosang <opellong13@gmail.com>
Co-Authored-By: jorippppong <duelee75@gmail.com>
```

### Merge 규칙

병합 방법은 merge commit 을 이용한다.

merge commit 메시지는 PR 이력을 파악하기 쉽도록 PR 제목과 동일하게 작성하며, 추적이 용이하도록 PR 번호를 추가한다.

```bash
[Feature/YB-1]: 소셜 로그인 기능 구현 (#1)
```

리뷰가 끝나고 appove가 완료된 후 PR을 올린사람이 Merge 한다.

Merge Conflict 가 발생할 경우, PR을 올린 사람이 충돌을 해결한다.

 

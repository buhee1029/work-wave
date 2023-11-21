![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=300&section=header&text=Work%20Wave&fontSize=90&animation=fadeIn&fontAlignY=38&descAlignY=51&descAlign=62)

## 프로젝트 개요
> 본 서비스는 기업 내부에서 사용되는 웹 서비스로, 프로젝트와 작업 관리를 위한 직관적이고 유연한 도구로, 팀 간 협업과 업무 효율성을 향상시키는데 목적을 두고 있습니다.

<br/>

## 개발환경
```
• IDE : IntelliJ IDEA Ultimate
• 언어 : Java 17
• 프레임워크 : Spring Boot 3.1.5
• 빌드 도구 : Gradle
• 데이터베이스 : MySQL 8.0
```

<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Boot 3.1.5-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white"/></a>
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Data JPA-gray?style=for-the-badge&logoColor=white"/></a>
<img src="https://img.shields.io/badge/MySQL 8-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Junit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white"/></a>
<img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"/></a>

<br/>

## 주요기능
> [요구사항 명세](https://docs.google.com/document/d/1tN0aXgr13YclqWS5e-Fxdkqnccc2tBYCMbHxRiIx5vs/edit)

### 🌈 팀 관리
- 팀 생성과 초대: 사용자는 팀을 생성하고 다른 사용자를 팀에 초대할 수 있습니다. 
- 초대 수락: 초대받은 사용자는 초대를 수락하여 해당 팀의 팀원이 됩니다.

### 📁 워크플로우(칸반보드) 관리
- 워크플로우 기능을 통해 프로젝트의 진행 상황을 직관적으로 파악할 수 있습니다.
- ...

### 🚀 작업(티켓) 관리
- ...

<br/>

## ERD (모델링)
<img src = "images/erd.png"/>

<br/>

## API 명세
#### 사용자

| Action | Verbs  | URL Pattern |
|:------:|--------|:-----------:|
|  회원가입  | POST   |    /join    |
|  로그인   | POST   |   /login    |

#### 팀
| Action  | Verbs |        URL Pattern         |
|:-------:|:-----:|:--------------------------:|
|  팀 생성   | POST  |           /teams           |
|  팀원 초대  | POST  |   /teams/{teamId}/invite   |
|  초대 승인  | POST  | /invites/{inviteId}/accept |

#### 워크플로우(칸반보드)
|   Action   | Verbs  |                 URL Pattern                  |
|:----------:|:------:|----------------------------------------------|
| 칸반보드 전체 조회 |  GET   |          /teams/{teamId}/workflows           |
|  칸반보드 추가   |  POST  |          /teams/{teamId}/workflows           |
|  칸반보드 수정   |  PUT   |    /teams/{teamId}/workflows/{workflowId}    |
|  칸반보드 이동   | PATCH  | /teams/{teamId}/workflows/move/{newPosition} |
|  칸반보드 삭제   | DELETE | /teams/{teamId}/workflows/{workflowId}       |

#### 작업(티켓)
| Action |  Verbs  | URL Pattern                                 |
|:------:|:-------:|---------------------------------------------|
| 작업 추가  |  POST   | /workflows/{workflowId}/works               |
| 작업 수정  |   PUT   | /workflows/{workflowId}/works/{workId}      |
| 작업 이동  |  PATCH  | /workflows/{workflowId}/works/{workId}/move |
| 작업 삭제  | DELETE  | /workflows/{workflowId}/works{workId}       |

<br/>

---
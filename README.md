# familing-backend

    
## Commit convention    

```
1. Type(File or function): Subject 
2. Type: Subject
```

|    Type    | 설명                                             |
|:----------:|------------------------------------------------|
|    feat    | 새로운 기능 추가                                      |
|    fix     | 버그 수정                                          |
|   style    | 코드 수정 없음 (세미콜론 누락, 코드 포맷팅, 파일, 폴더명 수정 혹은 이동 등) |
|  refactor  | 코드 리팩토링                                        |
|  comment   | 주석 추가 및 변경                                     |
|    docs    | 문서 수정 (README.md 등)                            |
|    test    | 테스트 코드 추가                                      |
|   chore    | 빌드 업무 수정, 패키지 매니저 수정 (pom.xml 등)               |
|   remove   | 파일 삭제                                          |

## Git Flow

![image](https://github.com/Familing/familing-backend/assets/64734115/90aae5a5-1a90-4649-97a3-089f67a3cd37)

master -> main 입니다.

### 기능 구현 시
1. issue탭에서 기능 구현 이슈 등록 (구현할 기능, todo 작성)
2. develop 브랜치로 이동 후  이슈 번호의 브랜치와 같단한 기능 설명이름을 가지는 브랜치 생성 
ex) 이슈가 #2번이고 로그인 기능을 구현할 계획이라면  
develop 브랜치에서 아래와 같은 브랜치 생성

```
git checkout -b feature/2-login
```

기능 구현 후 develop 브랜치로 PR 생성 후 코드 리뷰 후 merge

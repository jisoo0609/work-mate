# Work Mate - Team Convention
## **Commit Type Rule**

- 타입은 태그와 제목으로 구성되고, 태그는 영어로 쓰되 첫 문자는 대문자로 한다.

[클로리셔 작은 공간:티스토리]

| 태그 | 제목 |
| --- | --- |
| Feat: | 새로운 기능 추가 |
| Fix: | 버그 수정 |
| Docs: | 문서 수정 |
| Style: | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| Refactor: | 코드 리팩토링 |
| Test: | 테스트 (테스트 코드 추가, 수정 삭제, 비즈니스 로직 변경 없는 경우) |
| Chore: | 위에 걸리지 않는 기타 변경사항 (빌드 스크립트 수정, assets image, 패키지 매니저 등) |
| Design: | CSS 등 사용자 UI 디자인 변경 |
| Comment: | 필요한 주석 추가 및 변경 |
| Init: | 프로젝트 초기 설정 |
| Rename: | 파일 혹은 폴더명 수정하거나 옮기는 경우 |
| Remove: | 파일을 삭제하는 작업만 수행하는 경우 |
- Ex)

```java
Feat: 로그인 기능 추가
```

---

## 명명

### 폴더명: LowerCase

- Ex) `controller`, `service`, `config`

### 파일, 클래스명: PascalCase

- Ex) `UserController`, `ConfirmUser()`

### 변수, 메서드 명: camelCase

- Ex) `age`, `userId`, `paymentKey`
- 실수형
    - l과 f로 시작
    - Ex) `float fSum`, `long lAverage`
- 상수
    - CONSTANT_CASE

    ```java
    private final int NUMBER_PI
    ```

- 배열
    - arr

    ```java
    int [] arrNum
    ```

- 약어
    - PascalCase
    - Ex) HTTP, API, URL -> `HttpApiUrl`

---

## 선언

### 애너테이션

- 선언 후 새 줄 사용

```java
@GetMapping
public void home(){}
```

### Long 형의 숫자

- 뒤에 대문자 L 추가

```java
long num = 300L
```

### 중괄호

- K&R 스타일

```java
public class MyClass{
    public boolian subclass(String msg){
        if(msg == null){
            return false;
        } else {
            return true;
        }
    }
}
```

### 내용이 없는 블록

- 같은 줄에서 중괄호를 닫는 것을 허용

```java
public void close(){}
```
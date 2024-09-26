# 옐로북 Java 스타일가이드

스타일가이드는 [우아한 테크코스 스타일 가이드](https://github.com/woowacourse/woowacourse-docs/blob/main/styleguide/java/intellij-java-wooteco-style.xml)
를 바탕으로 구성되어 있습니다.

### 객체 생성에 Builder 패턴을 이용중이기 때문에 메소드 체이닝 가독성 향상을 위해 아래 옵션을 추가했습니다.

1. **`METHOD_CALL_CHAIN_WRAP = 2`**
    - 메서드 체인이 길어질 때 자동으로 줄바꿈을 추가합니다. 빌더 패턴에서 메서드 체인을 계속 이어서 호출하는 경우, 체인이 길어질 때 줄바꿈이 적용됩니다.

2. **`BINARY_OPERATION_SIGN_ON_NEXT_LINE = true`**
    - 메서드 체인의 다음 메서드 호출이 **`.` 연산자와 함께 다음 줄에 배치**됩니다.

### import 순서를 통일시키기 위해 import 선언의 순서를 조정하고 패키지 그룹 간 빈 줄을 추가했습니다.

[캠퍼스 핵데이 Java 코딩 컨벤션 - import 선언의 순서와 빈 줄 삽입](https://naver.github.io/hackday-conventions-java/#import-grouping) 을
참고했습니다.

- java, jakarta (기존의 javax), org, com 순서로 패키지 그룹을 묶었습니다.
- 각 패키지 그룹 간에 빈 줄이 추가되어 구분됩니 다.
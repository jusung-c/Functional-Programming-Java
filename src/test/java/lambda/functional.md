### 함수형 인터페이스 종류
| 입력 (Input) | 출력 (Output) | 함수형 (Functional) | 비고 (Remarks) |
| --- | --- | --- | --- |
| 있음 (Present) | 없음 (Void) | Consumer | accept |
| 없음 (Void) | 있음 (Present) | Supplier | get |
| 있음 (Present) | 있음 (Present) | Function | apply |
| 있음 (Present) | boolean | Predicate | test |
| 없음 (Void) | 없음 (Void) | Runnable | run |

### 람바식 사용을 위한 함수형 인터페이스의 조건
- 추상 메서드가 한 개인 인터페이스를 만든다.
  - static, default 메서드 있어도 됨
- (옵션) @FunctionalInterface 어노테이션 명시

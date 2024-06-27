package practice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionalTest {
    private static Stream<Arguments> nameFactory() {
        return Stream.of(
                Arguments.arguments(null, ""),
                Arguments.arguments("matthew", "MATTHEW")
        );
    }

    @ParameterizedTest
    @MethodSource("nameFactory")
    public void null_체크_전통적인_방식(String name, String expected) {
        String upperedname = "";

        if (name != null) {
            upperedname = name.toUpperCase();
        }

        assertEquals(expected, upperedname);
    }

    @ParameterizedTest
    @MethodSource("nameFactory")
    public void null_체크_optional(String name, String expected) {
        String upperName = Optional.ofNullable(name)
                .map(mapper -> mapper.toUpperCase()).orElse("");

        assertEquals(expected, upperName);

        if (Optional.ofNullable(name).isPresent()) {
            upperName = name.toUpperCase();
        }

        assertEquals(expected, upperName);
    }

    private static Stream<Arguments> listFactory() {
        List<Employee> employees =
                List.of(Employee.builder().name("jack").department("sales").build(),
                        Employee.builder().name("sam").department("sales").build(),
                        Employee.builder().name("dean").department("development").build(),
                        Employee.builder().name("mary").department("sales").build(),
                        Employee.builder().name("simon").department("development").build());
        return Stream.of(
                Arguments.arguments(employees, "jack", "sales"),
                Arguments.arguments(employees, "dean", "development"),
                Arguments.arguments(employees, "__blank__", "ceo")
        );
    }

    @ParameterizedTest
    @MethodSource("listFactory")
    void 부서별로_조회해서_목록의_첫번째_직원정보를_가져오기(List<Employee> emps, String expected, String dept) {
        String findFirst = emps.stream().filter(emp -> emp.getDepartment().equals(dept)).findFirst()
                .map(e -> e.getName()).orElse("__blank__");

        System.out.println("findFirst = " + findFirst);

        assertEquals(expected, findFirst);
    }

    @ParameterizedTest
    @MethodSource("listFactory")
    void optional의_올바른_사용법(List<Employee> emps, String expected, String dept) {
        Optional<Employee> firstEmp = findFirstEmpByDept(emps, dept);

        if (firstEmp.isEmpty()) {
            // 값을 찾기 못했을 경우 로직 수행
            assertEquals(expected, "__blank__");
        } else {
            String firstName = firstEmp.get().getName();
            assertEquals(expected, firstName);
        }

    }

    private Optional<Employee> findFirstEmpByDept(List<Employee> emps, String dept) {
        List<Employee> empByDept = emps.stream().filter(emp -> emp.getDepartment().equals(dept)).toList();

        return empByDept.isEmpty() ? Optional.empty() : Optional.of(empByDept.get(0));
    }

}

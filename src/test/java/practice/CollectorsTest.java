package practice;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CollectorsTest {
    private static Stream<Arguments> dataListFactory() {
        return Stream.of(arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                List.of("MATTHEW", "ALISON", "JACK", "SAM", "DEAN"), "대문자로 바꾸기"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void 최종_결과물을_컬렉션으로_만들기(List<String> source, List<String> expected, String testTitle) {
        List<String> upperNames = source.stream().map(name -> name.toUpperCase()).toList();

        assertEquals(upperNames, expected);
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void collect_메서드_사용(List<String> source, List<String> expected, String testTitle) {
        List<String> upperNames = source.stream().map(name -> name.toUpperCase()).collect(Collectors.toList());

        assertEquals(upperNames, expected);
    }

    private static Stream<Arguments> joiningDataFactory() {
        return Stream.of(arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                "MATTHEW, ALISON, JACK, SAM, DEAN", "대문자로 바꾸고 join하기"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("joiningDataFactory")
    public void 쉼표로_join_하기(List<String> source, String expected, String testTitle) {
        String result = source.stream()
                .map(name -> name.toUpperCase())
                .collect(Collectors.joining(", "));

        assertEquals(result, expected);
        System.out.println("result = " + result);
    }

    private static Stream<Arguments> setDataFactory() {
        return Stream.of(
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"), 5, "set으로 만들기"),
                arguments(List.of("matthew", "alison", "matthew", "dean", "dean"), 3, "set으로 만들기2"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("setDataFactory")
    public void set으로_묶어보기(List<String> source, int expected, String testTitle) {
        Set<String> names = source.stream().collect(Collectors.toSet());
        System.out.println("names = " + names);

        assertEquals(expected, names.size());
    }

    private static Stream<Arguments> employeeDataFactory() {
        List<Employee> employees =
                List.of(Employee.builder().name("jack").department("sales").build(),
                        Employee.builder().name("sam").department("sales").build(),
                        Employee.builder().name("dean").department("development").build(),
                        Employee.builder().name("mary").department("sales").build(),
                        Employee.builder().name("simon").department("development").build());
        return Stream.of(arguments(employees, 3, "sales"),
                arguments(employees, 2, "development"));
    }

    @ParameterizedTest(name = "{2} 부서의 사원수 : {1}")
    @MethodSource("employeeDataFactory")
    public void sql의_groupby를_구현해볼까(List<Employee> employees, int countOfDepartment,
                                    String department) {

        Map<String, List<Employee>> dept = employees.stream()
                .collect(Collectors.groupingBy(emp -> emp.getDepartment()));


        assertEquals(countOfDepartment, dept.get(department).size());

        // 필터 사용도 가능
        List<Employee> list = employees.stream().filter(emp -> emp.getDepartment().equals(department)).toList();
        assertEquals(countOfDepartment, list.size());

        // 3개의 매개변수를 받는 groupingBy
        Map<String, Set<Employee>> collect = employees.stream()
                .collect(Collectors.groupingBy(emp -> emp.getDepartment(), Collectors.toSet()));

        assertEquals(countOfDepartment, collect.get(department).size());
    }
}

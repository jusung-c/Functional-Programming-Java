import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ComparatorTest {
    private static Stream<Arguments> sortingDataFactory() {
        return Stream.of(
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        List.of("alison", "dean", "jack", "matthew", "sam"), "정렬1"),
                arguments(List.of("mon", "sum", "tue", "thu", "fri", "wed", "sat"),
                        List.of("fri", "mon", "sat", "sum", "thu", "tue", "wed"), "정렬2"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("sortingDataFactory")
    public void 정렬해보자(List<String> source, List<String> expected, String testTitle) {
        List<String> list = source.stream().sorted(String::compareTo).toList();
        System.out.println("list = " + list);

    }

    private static Stream<Arguments> employeeDataFactory() {

        return Stream.of(
                arguments(List.of(
                                new Employee("matthew", "sales", 23),
                                new Employee("alison", "marketing", 43),
                                new Employee("jack", "development", 32),
                                new Employee("sam", "sales", 38),
                                new Employee("dean", "development", 23),
                                new Employee("alan", "development", 49),
                                new Employee("jupyter", "development", 23),
                                new Employee("jay", "development", 43)
                        )
                        , "23,23,23,32,38,43,43,49"
                        , "23-dean,23-jupyter,23-matthew,32-jack,38-sam,43-alison,43-jay,49-alan"
                        , "49-alan,43-alison,43-jay,38-sam,32-jack,23-dean,23-jupyter,23-matthew"
                ));
    }

    @ParameterizedTest(name="Collections.sort() 정렬")
    @MethodSource("employeeDataFactory")
    public void Collections_sort_메서드를_사용하여_정렬(List<Employee> source, String expected) {
        String collect = source.stream().sorted((emp1, emp2) -> emp1.getAge() - emp2.getAge())
                .map(emp -> String.valueOf(emp.getAge()))
                .collect(Collectors.joining(","));

        assertEquals(expected, collect);
        System.out.println("collect = " + collect);
    }

    @ParameterizedTest(name="stream() 정렬")
    @MethodSource("employeeDataFactory")
    public void comparator를_사용하여_나이순으로_정렬(List<Employee> source, String expected) {
        String sorted = source.stream()
                .sorted((e1, e2) -> e1.getAge() - e2.getAge())
                .map(e -> String.valueOf(e.getAge()))
                .collect(Collectors.joining(","));

        assertEquals(expected, sorted);
    }

    @ParameterizedTest(name="메서드레퍼런스를 이용한 정렬")
    @MethodSource("employeeDataFactory")
    public void comparator_대신_메서드레퍼런스를_이용해보자(List<Employee> source, String expected) {
        String sorted = source.stream()
                .sorted(Employee::ageGap)
                .map(e -> String.valueOf(e.getAge()))
                .collect(Collectors.joining(","));
        assertEquals(expected, sorted);
    }

    @ParameterizedTest(name="Comparator.comparing().thenComparing()을 이용한 다중 정렬")
    @MethodSource("employeeDataFactory")
    public void 다중_정렬하기(List<Employee> source, String expected, String multiExpected, String reversedExpected) {
        final Function<Employee, Integer> byAge = Employee::getAge;
        final Function<Employee, String> byName = Employee::getName;

        List<Employee> toSort = new ArrayList<>(source);

        toSort.sort(Comparator.comparing(byAge).thenComparing(byName));

        StringJoiner joiner = new StringJoiner(",");
        for (Employee employee : toSort) {
            String s = employee.getAge() + "-" + employee.getName();
            joiner.add(s);
        }
        String sorted = joiner.toString();
        assertEquals(multiExpected, sorted);

        Stream.of(sorted.split(",")).forEach(System.out::println);

        String reversedSorted = source.stream()
                .sorted(Comparator.comparing(byAge).reversed().thenComparing(byName))
                .map(e -> e.getAge() + "-" + e.getName())
                .collect(Collectors.joining(","));
        assertEquals(reversedExpected, reversedSorted);

        Stream.of(reversedSorted.split(",")).forEach(System.out::println);
    }
}

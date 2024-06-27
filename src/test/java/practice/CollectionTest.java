package practice;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CollectionTest {

    private static Stream<Arguments> dataListFactory() {
        return Stream.of(arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                List.of("MATTHEW", "ALISON", "JACK", "SAM", "DEAN"), "대문자로 바꾸기"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void 컬렉션을_이터레이트하는_기존의_방법(List<String> source, List<String> expected, String testTitle) {
        List<String> upperNames = new ArrayList<>();
        for (String name : source) {
            upperNames.add(name.toUpperCase());
        }

        assertEquals(expected, upperNames);
        System.out.println("upperNames = " + upperNames);
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void foreach_메서드_사용(List<String> source, List<String> expected, String testTitle) {
        List<String> upperNames = new ArrayList<>();
        source.forEach(name -> upperNames.add(name.toUpperCase()));

        assertEquals(expected, upperNames);
        System.out.println("upperNames = " + upperNames);
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("dataListFactory")
    public void stream_사용(List<String> source, List<String> expected, String testTitle) {
        // map(Function)
        List<String> upperNames = source.stream().map(name -> name.toUpperCase()).toList();

        assertEquals(expected, upperNames);
        System.out.println("upperNames = " + upperNames);
    }

    private static Stream<Arguments> filterDataFactory() {
        return Stream.of(
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        List.of("matthew", "alison", "jack", "sam", "dean"), "a"),
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        List.of("alison", "sam"), "s"),
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        List.of("matthew", "sam"), "m"));
    }


    @ParameterizedTest(name = "{2}")
    @MethodSource("filterDataFactory")
    public void 필터로_걸러내기(List<String> source, List<String> expected, String filterString) {
        // filter(Predicate)
        List<String> filterNames = source.stream().filter(name -> name.contains(filterString)).toList();

        assertEquals(expected, filterNames);
        System.out.println("filterNames = " + filterNames);
    }

    private static Stream<Arguments> reduceDataFactory() {
        return Stream.of(
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        "matthew, alison, jack, sam, dean", ", "),
                arguments(List.of("matthew", "alison", "jack", "sam", "dean"),
                        "matthew-alison-jack-sam-dean", "-"));
    }


    @ParameterizedTest(name = "separator : {2}")
    @MethodSource("reduceDataFactory")
    public void 데이터_합치기(List<String> source, String expected, String separator) {
        Optional<String> reducedNames = source.stream().reduce((acc, next) -> {
            return acc + separator + next;
        });

        assertEquals(expected, reducedNames.get());
    }

    @Test
    public void 합계를_구해보자() {
        List<Integer> scores = List.of(89, 78, 37, 98, 100, 63, 78);

        int sum = scores.stream().reduce((a, b) -> a + b).get();

        assertEquals(543, sum);
    }

}

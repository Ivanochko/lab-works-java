import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dispatcher {
    /* На основі методів інтерфейсу Stream виконати наступні дії:
        - в тексті реалізувати початок кожного слова з прописної (великої) букви;
        - вивести все речення тексту за зростанням кількості слів;
        - колекцію цілих чисел поділити на колекції з додатніх та від’ємних елементів.
    */

    static String text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
            "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
            "when an unknown printer took a galley of type and scrambled it to make a type specimen book. " +
            "It has survived not only five centuries, but also the leap into electronic typesetting, " +
            "remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, " +
            "and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

    public static void main(String[] args) {

        task1(text);
        System.out.println();
        task2(text);
        System.out.println();
        task3_2();
    }

    static void task1(String text) {
        System.out.println("Capitalized text: ");
        System.out.println(toCapitalizeWordsInText(text));
        System.out.println("==================================");
    }

    static void task2(String text) {
        System.out.println("Sorted text: ");
        Arrays.stream(text.split("[.!?] "))
                .sorted(Comparator.comparingInt(s -> s.split(" ").length))
                .forEach(System.out::println);
        System.out.println("==================================");
    }

    static void task3() {
        List<Integer> arraysNumbers = Arrays.asList(1, -2, -3, 4, 5, -6, 7, -8, -9, 10, 11, -13, 15);

        List<Integer> positiveNumbers = arraysNumbers.stream().filter(i -> i > -1).collect(Collectors.toList());
        List<Integer> negativeNumbers = arraysNumbers.stream().filter(i -> i < 0).collect(Collectors.toList());

        System.out.println("Separated numbers: ");
        System.out.println(positiveNumbers);
        System.out.println(negativeNumbers);
        System.out.println("==================================");
    }

    static void task3_2() {
        List<Integer> arraysNumbers = Arrays.asList(1, -2, -3, 4, 5, -6, 7, -8, -9, 10, 11, -13, 15);

        List<Integer> positiveNumbers = new ArrayList<>();
        List<Integer> negativeNumbers = new ArrayList<>();

        arraysNumbers.forEach(integer -> {
            if (integer < 0) {
                negativeNumbers.add(integer);
            } else {
                positiveNumbers.add(integer);
            }
        });
        System.out.println("Separated numbers: ");
        System.out.println(positiveNumbers);
        System.out.println(negativeNumbers);
        System.out.println("==================================");
    }

    static String toCapitalizeWordsInText(String text) {
        return Stream.of(text.split(" "))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));
    }
}

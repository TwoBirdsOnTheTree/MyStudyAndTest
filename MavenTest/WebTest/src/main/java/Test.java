import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {
        IntStream.range(0, 3).forEach(i -> {
            System.out.println(i);
        });
    }
}

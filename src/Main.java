import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(final String[] args) throws InterruptedException {
        var seed = createSeed();

        List<Integer> binaries = toList(seed);
        List<Integer> random = new ArrayList<>();

        while (true) {
            while (random.size() <= 5) {
                random.add(binaries.get(binaries.size() - 1));
                binaries.remove(binaries.size() - 1);
                Integer xor = binaries.get(binaries.size() - 1) ^ binaries.get(binaries.size() - 2);
                binaries.add(0, xor);
            }
            var binary = random.stream().map(String::valueOf).collect(Collectors.joining(""));
            var decimal = toDecimal(binary);
            var hexadecimal = toHexadecimal(binary);

            System.out.println(decimal + " - " + hexadecimal);
            random = new ArrayList<>();
            Thread.sleep(1000);
        }


    }

    private static List<Integer> toList(String seed) {
        return seed.chars()
                .mapToObj(i -> (char) i)
                .map(c -> Integer.parseInt(String.valueOf(c)))
                .map(Integer::toBinaryString)
                .map(s -> s.chars()
                        .mapToObj(i -> (char) i)
                        .map(c -> Integer.parseInt(String.valueOf(c)))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static String createSeed() {
        LocalDateTime dateTime = LocalDateTime.now();
        return String.format("%s%s%s%s",
                dateTime.getHour(),
                dateTime.getMinute(),
                dateTime.getSecond(),
                dateTime.getNano());
    }

    public static int toDecimal(String binaryNumber) {
        return Integer.parseInt(binaryNumber, 2);
    }

    public static String toHexadecimal(String binaryNumber) {
        return Integer.toHexString(Integer.parseInt(binaryNumber, 2));
    }
}

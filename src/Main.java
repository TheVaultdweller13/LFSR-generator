import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(final String[] args) throws InterruptedException {
        var seed = createSeed();

        List<String> generated = new ArrayList<>();
        List<Byte> binaries = toList(seed);

        do {
            List<Byte> random = generator(binaries, 24);
            String binary = toBinaryString(random);
            int decimal = toDecimal(binary);
            String hexadecimal = toHexadecimal(binary).toUpperCase();

            generated.add(binary);

            System.out.println(decimal + " - " + hexadecimal);

            Thread.sleep(500);
        } while (generated.size() == new HashSet<>(generated).size());

        System.out.println("Number of pseudo-random numbers generated: " + generated.size());

    }

    private static List<Byte> generator(List<Byte> binaries, int length) {
        List<Byte> random = new ArrayList<>();
        while (random.size() <= length) {
            random.add(binaries.get(binaries.size() - 1));
            binaries.remove(binaries.size() - 1);
            byte xor = (byte) (binaries.get(binaries.size() - 1) ^ binaries.get(binaries.size() - 2));
            binaries.add(0, xor);
        }
        if (random.stream().noneMatch(b -> b != 0)) {
            return generator(binaries, length);
        }
        return random;
    }

    private static List<Byte> toList(String seed) {
        return seed.chars()
                .mapToObj(i -> (char) i)
                .map(c -> Byte.parseByte(String.valueOf(c)))
                .map(Integer::toBinaryString)
                .map(s -> s.chars()
                        .mapToObj(i -> (char) i)
                        .map(c -> Byte.parseByte(String.valueOf(c)))
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

    private static String toBinaryString(List<Byte> random) {
        return random.stream().map(String::valueOf).collect(Collectors.joining(""));
    }


    private static int toDecimal(String binaryNumber) {
        return Integer.parseInt(binaryNumber, 2);
    }

    private static String toHexadecimal(String binaryNumber) {
        return Integer.toHexString(Integer.parseInt(binaryNumber, 2));
    }
}

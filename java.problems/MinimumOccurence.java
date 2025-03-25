package practisequestions;

import java.util.*;

public class MinimumOccurence {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.next();
        findOccurrence(inputString);
        findOccurrenceWithStream(inputString);
    }


    private static void findOccurrence(String input) {
        Map<Character, Integer> occurrenceCount = new HashMap<>();
        for (int i = 0; i < input.length(); i++) {
            Integer count = occurrenceCount.getOrDefault(input.charAt(i), 0);
            occurrenceCount.put(input.charAt(i), count + 1);
        }

        int minCount = Integer.MAX_VALUE;

        if (!occurrenceCount.isEmpty()) {
            for (var entry : occurrenceCount.entrySet()) {
                if (entry.getValue() < minCount) {
                    minCount = entry.getValue();
                }
            }
        }
        printMinOccurrence(occurrenceCount, minCount);
    }


    private static void findOccurrenceWithStream(String input) {
        Map<Character, Integer> occurrenceCount = new HashMap<>();
        Arrays.stream(input.split(""))
                .map(s -> s.charAt(0))
                .forEach(c -> occurrenceCount.put(c, occurrenceCount.getOrDefault(c, 0) + 1));

        Optional<Integer> min = occurrenceCount.values()
                .stream().min(Integer::compareTo);

        printMinOccurrence(occurrenceCount, min.get());


    }

    private static void printMinOccurrence(Map<Character, Integer> occurrenceCount, int min) {
        occurrenceCount.forEach((k, v) -> {
            if (Objects.equals(v, min)) {
                System.out.println("Min occurrence is of " + k + " with count of " + v);
            }
        });
    }

}

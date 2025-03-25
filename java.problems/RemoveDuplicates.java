package practisequestions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicates {

    public static void main(String[] args) {
        String duplicates = "The the quick brown fox jumped over the lazy dog. It it didn't see that that the dog was actually awake. The fox fox looked back back, surprised surprised by the sudden movement. It was was a strange strange morning in the the forest.";
        String[] splitInput = duplicates.split("[ ,]+"); //splitting on the basis of whitespace along with the , to handle the back,
        removeDuplicates(splitInput);
        removeDuplicatesWithStream(splitInput);
    }


    private static void removeDuplicates(String[] splitInput) {
        Set<String> removedDuplicates = new HashSet<>();
        for (String s : splitInput) {
            if (removedDuplicates.add(s.toLowerCase())) {
                System.out.println(s);
            }
        }
    }

    private static void removeDuplicatesWithStream(String[] splitInput) {
        Set<String> removedDuplicates = new HashSet<>();
        Arrays.stream(splitInput).filter(str -> removedDuplicates.add(str.toLowerCase())).forEach(System.out::println);
    }

}

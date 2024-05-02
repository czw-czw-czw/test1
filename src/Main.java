import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
public class Main {
    public static Map<String, Set<String>> createInvertedIndex(String folderPath) {
        Map<String, Set<String>> invertedIndex = new HashMap<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("Error opening folder");
            return invertedIndex;
        }

        for (File file : files) {
            if (!file.getName().endsWith(".txt")) {
                continue;
            }
            try {
                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8).toLowerCase();
                String[] words = content.split("\\s+");

                for (String word : words) {
                    if (invertedIndex.containsKey(word)) {
                        invertedIndex.get(word).add(file.getName());
                    } else {
                        Set<String> docs = new HashSet<>();
                        docs.add(file.getName());
                        invertedIndex.put(word, docs);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return invertedIndex;
    }

    public static Set<String> searchDocuments(Map<String, Set<String>> invertedIndex, String[] words) {
        Set<String> commonDocs = new HashSet<>();
        for (String word : words) {
            Set<String> docs = invertedIndex.getOrDefault(word.toLowerCase(), new HashSet<>());
            if (commonDocs.isEmpty()) {
                commonDocs.addAll(docs);
            } else {
                commonDocs.retainAll(docs);
            }
        }
        return commonDocs;
    }

    public static void main(String[] args) {
        String folderPath = "C:\\Users\\czw\\IdeaProjects\\test1\\";
        while (true) {
            System.out.println("Enter words separated by spaces (or -quit- to exit): ");
            Scanner userInputScanner = new Scanner(System.in);
            String userInput = userInputScanner.nextLine();
            if (userInput.equals("-quit-")) {
                break;
            }

            String[] words = userInput.split(" ");

            Map<String, Set<String>> invertedIndex = createInvertedIndex(folderPath);

            Set<String> resultDocs = searchDocuments(invertedIndex, words);
            if (!resultDocs.isEmpty()) {
                System.out.println("Documents containing all the words:");
                for (String doc : resultDocs) {
                    System.out.println(doc);
                }
            } else {
                System.out.println("No documents contain all the words.");
            }
        }
    }
}
import java.util.*;

class PlagiarismDetector {

    private final int N;
    private final Map<String, Set<String>> ngramIndex;

    PlagiarismDetector(int n) {
        this.N = n;
        this.ngramIndex = new HashMap<>();
    }

    public List<String> extractNGrams(String text) {
        List<String> words = Arrays.asList(text.toLowerCase().split("\\s+"));
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.size() - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words.get(i + j)).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }
        return ngrams;
    }

    public void indexDocument(String documentId, String text) {
        List<String> ngrams = extractNGrams(text);

        for (String gram : ngrams) {
            ngramIndex
                    .computeIfAbsent(gram, k -> new HashSet<>())
                    .add(documentId);
        }
    }

    public void analyzeDocument(String documentId, String text) {
        List<String> ngrams = extractNGrams(text);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {
            if (ngramIndex.containsKey(gram)) {
                for (String doc : ngramIndex.get(gram)) {
                    matchCount.put(doc, matchCount.getOrDefault(doc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            double similarity = (entry.getValue() * 100.0) / ngrams.size();
            System.out.println(
                    "Found " + entry.getValue() + " matching n-grams with \"" +
                            entry.getKey() + "\""
            );
            System.out.printf("Similarity: %.1f%% %s%n",
                    similarity,
                    similarity >= 60 ? "(PLAGIARISM DETECTED)" : "(suspicious)"
            );
        }
    }
}

public class PS4 {
    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector(5);

        String essay089 = "data structures and algorithms are fundamental in computer science education";
        String essay092 = "data structures and algorithms are fundamental in computer science and software engineering";
        String essay123 = "data structures and algorithms are fundamental in computer science education and practice";

        detector.indexDocument("essay_089.txt", essay089);
        detector.indexDocument("essay_092.txt", essay092);

        detector.analyzeDocument("essay_123.txt", essay123);
    }
}
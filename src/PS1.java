import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
class UsernameChecker {

    private ConcurrentHashMap<String, Integer> userMap;
    private ConcurrentHashMap<String, Integer> attemptCount;

    public UsernameChecker() {
        userMap = new ConcurrentHashMap<>();
        attemptCount = new ConcurrentHashMap<>();
        userMap.put("john_doe", 101);
        userMap.put("admin", 1);
        userMap.put("root", 2);
    }
    public boolean checkAvailability(String username) {
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);
        return !userMap.containsKey(username);
    }
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        int count = 1;
        while (suggestions.size() < 3) {
            String newName = username + count;
            if (!userMap.containsKey(newName)) {
                suggestions.add(newName);
            }
            count++;
        }
        if (username.contains("_")) {
            String dotVersion = username.replace("_", ".");
            if (!userMap.containsKey(dotVersion)) {
                suggestions.add(dotVersion);
            }
        }
        return suggestions;
    }
    public String getMostAttempted() {
        String popular = null;
        int maxAttempts = 0;
        for (Map.Entry<String, Integer> entry : attemptCount.entrySet()) {
            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                popular = entry.getKey();
            }
        }
        return popular;
    }
    public void registerUser(String username, int userId) {
        userMap.put(username, userId);
    }
}
public class PS1 {
    public static void main(String[] args) {

        UsernameChecker checker = new UsernameChecker();
        System.out.println("Check Availability:");
        System.out.println("john_doe → " + checker.checkAvailability("john_doe"));
        System.out.println("jane_smith → " + checker.checkAvailability("jane_smith"));
        System.out.println("\nSuggestions for john_doe:");
        System.out.println(checker.suggestAlternatives("john_doe"));
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        System.out.println("\nMost Attempted Username:");
        System.out.println(checker.getMostAttempted());
    }
}
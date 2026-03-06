import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class InventoryManager {
    private ConcurrentHashMap<String, AtomicInteger> stockMap;
    private ConcurrentHashMap<String, LinkedHashMap<Integer, Integer>> waitingList;

    public InventoryManager() {
        stockMap = new ConcurrentHashMap<>();
        waitingList = new ConcurrentHashMap<>();
        stockMap.put("IPHONE15_256GB", new AtomicInteger(100));
        waitingList.put("IPHONE15_256GB", new LinkedHashMap<>());
    }
    public int checkStock(String productId) {
        AtomicInteger stock = stockMap.get(productId);
        return (stock != null) ? stock.get() : 0;
    }
    public synchronized String purchaseItem(String productId, int userId) {

        AtomicInteger stock = stockMap.get(productId);

        if (stock == null) {
            return "Product not found";
        }
        if (stock.get() > 0) {
            stock.decrementAndGet();
            return "Success, " + stock.get() + " units remaining";
        }
        LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);
        queue.put(userId, queue.size() + 1);

        return "Added to waiting list, position #" + queue.size();
    }
    public void showWaitingList(String productId) {
        System.out.println("Waiting List:");
        LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);

        for (Map.Entry<Integer, Integer> entry : queue.entrySet()) {
            System.out.println("UserId: " + entry.getKey() +
                    " Position: " + entry.getValue());
        }
    }
}
public class PS2 {
    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        System.out.println("Initial Stock:");
        System.out.println("IPHONE15_256GB → " +
                manager.checkStock("IPHONE15_256GB") + " units available");
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 98; i++) {
            manager.purchaseItem("IPHONE15_256GB", 20000 + i);
        }
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 88888));

        manager.showWaitingList("IPHONE15_256GB");
    }
}
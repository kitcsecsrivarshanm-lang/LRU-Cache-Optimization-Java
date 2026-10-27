import java.util.HashMap;
import java.util.Scanner;

public class LRUCacheProject {

    static class Node {
        int key;
        String value;
        Node prev, next;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    static class LRUCache {
        private final int capacity;
        private final HashMap<Integer, Node> map;
        private final Node head, tail;

        private int hits = 0;
        private int misses = 0;

        LRUCache(int capacity) {
            this.capacity = capacity;
            map = new HashMap<>();

            head = new Node(0, "");
            tail = new Node(0, "");
            head.next = tail;
            tail.prev = head;
        }

        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void addToFront(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        private void moveToFront(Node node) {
            removeNode(node);
            addToFront(node);
        }

        public String get(int key) {
            Node node = map.get(key);

            if (node == null) {
                misses++;
                return null;
            }

            hits++;
            moveToFront(node);
            return node.value;
        }

        public void put(int key, String value) {
            if (capacity == 0) {
                System.out.println("Cache capacity is zero.");
                return;
            }

            Node node = map.get(key);

            if (node != null) {
                node.value = value;
                moveToFront(node);
                System.out.println("Value updated successfully.");
                return;
            }

            if (map.size() == capacity) {
                Node lru = tail.prev;
                removeNode(lru);
                map.remove(lru.key);
                System.out.println("Evicted least recently used key: " + lru.key);
            }

            Node newNode = new Node(key, value);
            addToFront(newNode);
            map.put(key, newNode);
            System.out.println("Value inserted successfully.");
        }

        public void display() {
            if (map.isEmpty()) {
                System.out.println("Cache is empty.");
                return;
            }

            System.out.print("MRU -> ");
            Node current = head.next;

            while (current != tail) {
                System.out.print("[" + current.key + ": " + current.value + "]");
                current = current.next;
                if (current != tail) {
                    System.out.print(" <-> ");
                }
            }

            System.out.println(" <- LRU");
        }

        public void showStats() {
            int total = hits + misses;

            System.out.println("\\n--- Cache Statistics ---");
            System.out.println("Capacity       : " + capacity);
            System.out.println("Current entries: " + map.size());
            System.out.println("Cache hits     : " + hits);
            System.out.println("Cache misses   : " + misses);

            double hitRate = total == 0 ? 0.0 : (hits * 100.0) / total;
            System.out.printf("Hit rate        : %.2f%%%n", hitRate);
        }

        public void browserSimulation() {
            int[] pages = {1, 2, 3, 1, 4, 2, 5, 1};

            System.out.println("\\n--- Browser Cache Simulation ---");
            for (int page : pages) {
                String result = get(page);

                if (result != null) {
                    System.out.println("Page " + page + ": CACHE HIT");
                } else {
                    System.out.println("Page " + page + ": CACHE MISS");
                    put(page, "WebPage-" + page);
                }
            }

            System.out.println("\\nFinal cache:");
            display();
            showStats();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("       LRU CACHE PROJECT");
        System.out.println("================================");
        System.out.print("Enter cache capacity: ");

        int capacity = sc.nextInt();
        sc.nextLine();

        if (capacity < 0) {
            System.out.println("Capacity cannot be negative.");
            sc.close();
            return;
        }

        LRUCache cache = new LRUCache(capacity);

        while (true) {
            System.out.println("\\n========== MENU ==========");
            System.out.println("1. Put (Insert/Update)");
            System.out.println("2. Get (Search)");
            System.out.println("3. Display Cache");
            System.out.println("4. Show Statistics");
            System.out.println("5. Browser Cache Simulation");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter key: ");
                    int key = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter value: ");
                    String value = sc.nextLine();
                    cache.put(key, value);
                    break;

                case 2:
                    System.out.print("Enter key to search: ");
                    int searchKey = sc.nextInt();

                    String result = cache.get(searchKey);
                    if (result == null) {
                        System.out.println("Cache miss! Key not found.");
                    } else {
                        System.out.println("Cache hit! Value: " + result);
                    }
                    break;

                case 3:
                    cache.display();
                    break;

                case 4:
                    cache.showStats();
                    break;

                case 5:
                    cache.browserSimulation();
                    break;

                case 6:
                    System.out.println("Thank you for using LRU Cache!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

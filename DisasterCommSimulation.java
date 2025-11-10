import java.util.*;

class Message {
    int id;
    int source;
    int destination;
    String data;
    boolean delivered = false;

    public Message(int id, int source, int destination, String data) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.data = data;
    }
}

class Node {
    int id;
    Random random = new Random();

    public Node(int id) {
        this.id = id;
    }

    // Simulate sending a message with reliability
    public boolean sendMessage(Node receiver, Message msg, double reliability) {
        System.out.println("Node " + id + " sending message " + msg.id + " to Node " + receiver.id);
        // Random link failure simulation
        boolean success = random.nextDouble() < reliability;

        if (success) {
            receiver.receiveMessage(msg);
            return true;
        } else {
            System.out.println("❌ Transmission failed between Node " + id + " and Node " + receiver.id);
            return false;
        }
    }

    public void receiveMessage(Message msg) {
        msg.delivered = true;
        System.out.println("✅ Node " + id + " received message " + msg.id + " from Node " + msg.source);
    }
}

public class DisasterCommSimulation {
    public static void main(String[] args) {
        // Simulation setup
        int numNodes = 5;
        double linkReliability = 0.7; // 70% chance that a link works
        int maxRetries = 3;

        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            nodes.add(new Node(i));
        }

        // Create messages to send
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1, 0, 3, "Rescue Team 0 reporting to HQ"));
        messages.add(new Message(2, 1, 4, "Drone 1 sending location data"));
        messages.add(new Message(3, 2, 0, "Team 2 requesting backup"));

        int totalSent = 0, totalDelivered = 0;

        // Simulate message sending
        for (Message msg : messages) {
            Node sender = nodes.get(msg.source);
            Node receiver = nodes.get(msg.destination);

            boolean delivered = false;
            int attempts = 0;

            while (!delivered && attempts < maxRetries) {
                attempts++;
                totalSent++;
                delivered = sender.sendMessage(receiver, msg, linkReliability);
                if (!delivered) {
                    System.out.println("↩ Retrying (" + attempts + "/" + maxRetries + ")...");
                }
            }

            if (delivered) {
                totalDelivered++;
            } else {
                System.out.println("⚠ Message " + msg.id + " could not be delivered after retries.");
            }
            System.out.println();
        }

        // Simulation summary
        System.out.println("========== Simulation Summary ==========");
        System.out.println("Total Messages Sent: " + totalSent);
        System.out.println("Total Delivered: " + totalDelivered);
        System.out.println("Delivery Success Rate: " +
                String.format("%.2f", (100.0 * totalDelivered / messages.size())) + "%");
    }
}

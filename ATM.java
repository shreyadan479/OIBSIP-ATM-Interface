import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// User class to represent ATM users
class User {
    private String userId;
    private String userPin;
    private double balance;
    private Map<String, Double> transactionHistory;

    public User(String userId, String userPin, double initialBalance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = initialBalance;
        this.transactionHistory = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public boolean checkPin(String enteredPin) {
        return userPin.equals(enteredPin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposit", amount);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            addTransaction("Withdrawal", amount);
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    public void transfer(User recipient, double amount) {
        if (amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            addTransaction("Transfer to " + recipient.getUserId(), amount);
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    public void displayTransactionHistory() {
        System.out.println("Transaction History for " + userId + ":");
        for (Map.Entry<String, Double> entry : transactionHistory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void addTransaction(String type, double amount) {
        transactionHistory.put(type, amount);
    }
}

// ATM class to manage user interactions
class ATM {
    private Map<String, User> users;
    private User currentUser;

    public ATM() {
        this.users = new HashMap<>();
        // Add some sample users
        users.put("user1", new User("user1", "1234", 1000));
        users.put("user2", new User("user2", "5678", 2000));
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        System.out.print("Enter User PIN: ");
        String userPin = scanner.nextLine();

        if (users.containsKey(userId) && users.get(userId).checkPin(userPin)) {
            currentUser = users.get(userId);
            displayMenu(scanner);
        } else {
            System.out.println("Invalid User ID or PIN. Exiting...");
        }

        scanner.close();
    }

    private void displayMenu(Scanner scanner) {
        int choice;

        do {
            System.out.println("\nATM Menu:");
            System.out.println("1. Transactions History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("\n4. Transfer");
            System.out.println("5. Quit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    currentUser.displayTransactionHistory();
                    break;
                case 2:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    currentUser.withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    currentUser.deposit(depositAmount);
                    break;
                case 4:
                    System.out.print("Enter recipient's User ID: ");
                    String recipientId = scanner.next();
                    System.out.print("Enter transfer amount: ");
                    double transferAmount = scanner.nextDouble();

                    if (users.containsKey(recipientId)) {
                        currentUser.transfer(users.get(recipientId), transferAmount);
                    } else {
                        System.out.println("Recipient not found!");
                    }
                    break;
                case 5:
                    System.out.println("Exiting ATM. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 5);
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
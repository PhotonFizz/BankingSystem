package com.banking.system;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Scanner;

public class BankingSystemDemo {
    private static Bank bank = new Bank("Bangladesh com.banking.system.Bank");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to " + bank.getBankName() + "!");

        // Create sample data
        createSampleData();

        // Main program loop
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createNewCustomer();
                    break;
                case 2:
                    createNewAccount();
                    break;
                case 3:
                    performTransaction();
                    break;
                case 4:
                    viewAccountInfo();
                    break;
                case 5:
                    bank.displayAllAccounts();
                    break;
                case 6:
                    System.out.println("Thank you for using " + bank.getBankName() + "!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void createSampleData() {
        // Create sample customers and accounts for testing
        Customer customer1 = bank.createCustomer("Kemal Ataturk", "Thessaloniki, Greece", "555-1234", "kemalataturk@gmail.com");
        Customer customer2 = bank.createCustomer("Joseph Stalin", "Gori, Georgia", "555-5678", "josephstalin@gmail.com");

        bank.createAccount(customer1.getCustomerID(), "checking");
        bank.createAccount(customer1.getCustomerID(), "savings");
        bank.createAccount(customer2.getCustomerID(), "checking");

        System.out.println("\n--- Sample data created ---\n");
    }

    private static void displayMainMenu() {
        System.out.println("\n=== BANKING SYSTEM MENU ===");
        System.out.println("1. Create New com.banking.system.Customer");
        System.out.println("2. Create New com.banking.system.Account");
        System.out.println("3. Perform com.banking.system.Transaction");
        System.out.println("4. View com.banking.system.Account Information");
        System.out.println("5. View All Accounts");
        System.out.println("6. Exit");
    }

    private static void createNewCustomer() {
        System.out.println("\n--- Create New com.banking.system.Customer ---");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        bank.createCustomer(name, address, phone, email);
    }

    private static void createNewAccount() {
        System.out.println("\n--- Create New com.banking.system.Account ---");
        int customerID = getIntInput("Enter customer ID: ");
        System.out.print("Enter account type (checking/savings): ");
        String accountType = scanner.nextLine();

        bank.createAccount(customerID, accountType);
    }

    private static void performTransaction() {
        System.out.println("\n--- Perform com.banking.system.Transaction ---");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        Account account = bank.findAccount(accountNumber);
        if (account == null) {
            System.out.println("com.banking.system.Account not found!");
            return;
        }

        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. View com.banking.system.Transaction History");

        int choice = getIntInput("Choose transaction type: ");

        switch (choice) {
            case 1:
                double depositAmount = getDoubleInput("Enter deposit amount: $");
                account.deposit(depositAmount);
                break;
            case 2:
                double withdrawAmount = getDoubleInput("Enter withdrawal amount: $");
                account.withdraw(withdrawAmount);
                break;
            case 3:
                System.out.print("Enter target account number: ");
                String targetAccountNumber = scanner.nextLine();
                Account targetAccount = bank.findAccount(targetAccountNumber);
                if (targetAccount == null) {
                    System.out.println("Target account not found!");
                    return;
                }
                double transferAmount = getDoubleInput("Enter transfer amount: $");
                account.transfer(targetAccount, transferAmount);
                break;
            case 4:
                account.displayTransactionHistory();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void viewAccountInfo() {
        System.out.println("\n--- View com.banking.system.Account Information ---");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        Account account = bank.findAccount(accountNumber);
        if (account == null) {
            System.out.println("com.banking.system.Account not found!");
            return;
        }

        Customer customer = bank.findCustomer(account.getCustomerID());
        System.out.println("\n--- com.banking.system.Account Details ---");
        System.out.println(account);
        System.out.println("\n--- com.banking.system.Customer Details ---");
        System.out.println(customer);
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid amount.");
            }
        }
    }

    public static class BankingSystemGUI extends Application {

        private Bank bank;
        private Stage primaryStage;
        private Scene mainMenuScene, customerScene, accountScene, transactionScene;

        // UI Components that need to be accessed globally
        private ComboBox<String> accountComboBox;
        private TextArea outputArea;
        private ObservableList<String> accountList;

        @Override
        public void start(Stage primaryStage) {
            this.primaryStage = primaryStage;
            this.bank = new Bank("First National com.banking.system.Bank - GUI");

            // Initialize account list for combo boxes
            accountList = FXCollections.observableArrayList();

            // Create sample data
            createSampleData();

            // Create all scenes
            createMainMenuScene();
            createCustomerScene();
            createAccountScene();
            createTransactionScene();

            // Setup primary stage
            primaryStage.setTitle("Banking System - " + bank.getBankName());
            primaryStage.setScene(mainMenuScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }

        private void createSampleData() {
            Customer customer1 = bank.createCustomer("John Doe", "123 Main St", "555-1234", "john@email.com");
            Customer customer2 = bank.createCustomer("Jane Smith", "456 Oak Ave", "555-5678", "jane@email.com");

            Account acc1 = bank.createAccount(customer1.getCustomerID(), "checking");
            Account acc2 = bank.createAccount(customer1.getCustomerID(), "savings");
            Account acc3 = bank.createAccount(customer2.getCustomerID(), "checking");

            // Add initial deposits
            acc1.deposit(1000.0);
            acc2.deposit(5000.0);
            acc3.deposit(750.0);

            updateAccountList();
        }

        private void createMainMenuScene() {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(50));
            root.setStyle("-fx-background-color: #f0f8ff;");

            // Title
            Label titleLabel = new Label("🏦 " + bank.getBankName());
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            titleLabel.setStyle("-fx-text-fill: #2c3e50;");

            // Subtitle
            Label subtitleLabel = new Label("Welcome to Your Digital Banking Experience");
            subtitleLabel.setFont(Font.font("Arial", 16));
            subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

            // Buttons
            Button customerBtn = createStyledButton("👥 Manage Customers", "#3498db");
            Button accountBtn = createStyledButton("💳 Manage Accounts", "#2ecc71");
            Button transactionBtn = createStyledButton("💰 Transactions", "#e74c3c");
            Button exitBtn = createStyledButton("🚪 Exit", "#95a5a6");

            // Button actions
            customerBtn.setOnAction(e -> primaryStage.setScene(customerScene));
            accountBtn.setOnAction(e -> primaryStage.setScene(accountScene));
            transactionBtn.setOnAction(e -> {
                updateAccountList();
                primaryStage.setScene(transactionScene);
            });
            exitBtn.setOnAction(e -> primaryStage.close());

            root.getChildren().addAll(titleLabel, subtitleLabel,
                    new Separator(),
                    customerBtn, accountBtn, transactionBtn, exitBtn);

            mainMenuScene = new Scene(root, 500, 600);
        }

        private void createCustomerScene() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #f0f8ff;");

            // Header
            VBox header = new VBox(10);
            header.setAlignment(Pos.CENTER);
            header.setPadding(new Insets(20));
            Label titleLabel = new Label("👥 com.banking.system.Customer Management");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            header.getChildren().add(titleLabel);
            root.setTop(header);

            // Form
            GridPane form = new GridPane();
            form.setHgap(10);
            form.setVgap(15);
            form.setPadding(new Insets(20));
            form.setAlignment(Pos.CENTER);

            // Form fields
            TextField nameField = new TextField();
            TextField addressField = new TextField();
            TextField phoneField = new TextField();
            TextField emailField = new TextField();

            form.add(new Label("Name:"), 0, 0);
            form.add(nameField, 1, 0);
            form.add(new Label("Address:"), 0, 1);
            form.add(addressField, 1, 1);
            form.add(new Label("Phone:"), 0, 2);
            form.add(phoneField, 1, 2);
            form.add(new Label("Email:"), 0, 3);
            form.add(emailField, 1, 3);

            root.setCenter(form);

            // Buttons
            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(20));

            Button createBtn = createStyledButton("Create com.banking.system.Customer", "#2ecc71");
            Button backBtn = createStyledButton("← Back to Main Menu", "#95a5a6");

            createBtn.setOnAction(e -> {
                if (validateFields(nameField, addressField, phoneField, emailField)) {
                    Customer customer = bank.createCustomer(
                            nameField.getText().trim(),
                            addressField.getText().trim(),
                            phoneField.getText().trim(),
                            emailField.getText().trim()
                    );

                    showAlert("Success", "com.banking.system.Customer created successfully!\ncom.banking.system.Customer ID: " + customer.getCustomerID());
                    clearFields(nameField, addressField, phoneField, emailField);
                }
            });

            backBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));

            buttonBox.getChildren().addAll(createBtn, backBtn);
            root.setBottom(buttonBox);

            customerScene = new Scene(root, 500, 400);
        }

        private void createAccountScene() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #f0f8ff;");

            // Header
            VBox header = new VBox(10);
            header.setAlignment(Pos.CENTER);
            header.setPadding(new Insets(20));
            Label titleLabel = new Label("💳 com.banking.system.Account Management");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            header.getChildren().add(titleLabel);
            root.setTop(header);

            // Form
            VBox form = new VBox(15);
            form.setAlignment(Pos.CENTER);
            form.setPadding(new Insets(20));

            TextField customerIdField = new TextField();
            customerIdField.setPromptText("Enter com.banking.system.Customer ID");

            ComboBox<String> accountTypeBox = new ComboBox<>();
            accountTypeBox.getItems().addAll("checking", "savings");
            accountTypeBox.setPromptText("Select com.banking.system.Account Type");

            form.getChildren().addAll(
                    new Label("com.banking.system.Customer ID:"),
                    customerIdField,
                    new Label("com.banking.system.Account Type:"),
                    accountTypeBox
            );

            root.setCenter(form);

            // Output area for account list
            outputArea = new TextArea();
            outputArea.setEditable(false);
            outputArea.setPrefRowCount(8);
            outputArea.setStyle("-fx-font-family: monospace;");

            VBox outputBox = new VBox(10);
            outputBox.setPadding(new Insets(20));
            outputBox.getChildren().addAll(new Label("All Accounts:"), outputArea);

            VBox centerBox = new VBox();
            centerBox.getChildren().addAll(form, new Separator(), outputBox);
            root.setCenter(centerBox);

            // Buttons
            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(20));

            Button createBtn = createStyledButton("Create com.banking.system.Account", "#2ecc71");
            Button refreshBtn = createStyledButton("Refresh List", "#3498db");
            Button backBtn = createStyledButton("← Back to Main Menu", "#95a5a6");

            createBtn.setOnAction(e -> {
                try {
                    int customerId = Integer.parseInt(customerIdField.getText().trim());
                    String accountType = accountTypeBox.getValue();

                    if (accountType == null) {
                        showAlert("Error", "Please select an account type!");
                        return;
                    }

                    Account account = bank.createAccount(customerId, accountType);
                    if (account != null) {
                        showAlert("Success", "com.banking.system.Account created successfully!\ncom.banking.system.Account Number: " + account.getAccountNumber());
                        customerIdField.clear();
                        accountTypeBox.setValue(null);
                        updateAccountList();
                        refreshAccountDisplay();
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Error", "Please enter a valid com.banking.system.Customer ID!");
                }
            });

            refreshBtn.setOnAction(e -> refreshAccountDisplay());
            backBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));

            buttonBox.getChildren().addAll(createBtn, refreshBtn, backBtn);
            root.setBottom(buttonBox);

            // Initial load
            refreshAccountDisplay();

            accountScene = new Scene(root, 600, 700);
        }

        private void createTransactionScene() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #f0f8ff;");

            // Header
            VBox header = new VBox(10);
            header.setAlignment(Pos.CENTER);
            header.setPadding(new Insets(20));
            Label titleLabel = new Label("💰 com.banking.system.Transaction Center");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            header.getChildren().add(titleLabel);
            root.setTop(header);

            // com.banking.system.Transaction form
            VBox form = new VBox(15);
            form.setAlignment(Pos.CENTER);
            form.setPadding(new Insets(20));

            accountComboBox = new ComboBox<>(accountList);
            accountComboBox.setPromptText("Select com.banking.system.Account");

            TextField amountField = new TextField();
            amountField.setPromptText("Enter Amount");

            ComboBox<String> targetAccountBox = new ComboBox<>(accountList);
            targetAccountBox.setPromptText("Select Target com.banking.system.Account (for transfers)");

            form.getChildren().addAll(
                    new Label("Select com.banking.system.Account:"),
                    accountComboBox,
                    new Label("Amount:"),
                    amountField,
                    new Label("Target com.banking.system.Account (for transfers):"),
                    targetAccountBox
            );

            root.setCenter(form);

            // com.banking.system.Transaction buttons
            HBox transactionButtons = new HBox(10);
            transactionButtons.setAlignment(Pos.CENTER);
            transactionButtons.setPadding(new Insets(20));

            Button depositBtn = createStyledButton("💵 Deposit", "#2ecc71");
            Button withdrawBtn = createStyledButton("💸 Withdraw", "#e74c3c");
            Button transferBtn = createStyledButton("🔄 Transfer", "#f39c12");
            Button historyBtn = createStyledButton("📊 View History", "#9b59b6");

            // com.banking.system.Transaction button actions
            depositBtn.setOnAction(e -> performTransaction("deposit", amountField, targetAccountBox));
            withdrawBtn.setOnAction(e -> performTransaction("withdraw", amountField, targetAccountBox));
            transferBtn.setOnAction(e -> performTransaction("transfer", amountField, targetAccountBox));
            historyBtn.setOnAction(e -> showTransactionHistory());

            transactionButtons.getChildren().addAll(depositBtn, withdrawBtn, transferBtn, historyBtn);

            // Back button
            HBox backButtonBox = new HBox();
            backButtonBox.setAlignment(Pos.CENTER);
            backButtonBox.setPadding(new Insets(10));
            Button backBtn = createStyledButton("← Back to Main Menu", "#95a5a6");
            backBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));
            backButtonBox.getChildren().add(backBtn);

            VBox bottomBox = new VBox();
            bottomBox.getChildren().addAll(transactionButtons, backButtonBox);
            root.setBottom(bottomBox);

            transactionScene = new Scene(root, 600, 500);
        }

        private Button createStyledButton(String text, String color) {
            Button button = new Button(text);
            button.setPrefWidth(200);
            button.setPrefHeight(40);
            button.setStyle(
                    "-fx-background-color: " + color + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-cursor: hand;"
            );

            // Hover effect
            button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.8;"));
            button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.8;", "")));

            return button;
        }

        private boolean validateFields(TextField... fields) {
            for (TextField field : fields) {
                if (field.getText().trim().isEmpty()) {
                    showAlert("Error", "Please fill in all fields!");
                    return false;
                }
            }
            return true;
        }

        private void clearFields(TextField... fields) {
            for (TextField field : fields) {
                field.clear();
            }
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        private void updateAccountList() {
            accountList.clear();
            // This is a simplified way - in a real app you'd have a method to get all accounts
            // For now, we'll populate with some sample account numbers
            // You'll need to modify the com.banking.system.Bank class to provide a method to get all accounts
            accountList.addAll("Sample accounts will appear here");
        }

        private void refreshAccountDisplay() {
            StringBuilder sb = new StringBuilder();
            sb.append("com.banking.system.Account List:\n");
            sb.append("============\n");
            sb.append("Note: You'll need to implement getAllAccounts() method in com.banking.system.Bank class\n");
            sb.append("to display all accounts here.\n\n");
            sb.append("For now, use the sample account numbers from the console output.");
            outputArea.setText(sb.toString());
        }

        private void performTransaction(String type, TextField amountField, ComboBox<String> targetAccountBox) {
            String selectedAccount = accountComboBox.getValue();
            if (selectedAccount == null) {
                showAlert("Error", "Please select an account!");
                return;
            }

            try {
                double amount = Double.parseDouble(amountField.getText().trim());

                // Note: You'll need to implement actual transaction logic here
                // This is a placeholder that shows the concept
                showAlert("com.banking.system.Transaction", type.toUpperCase() + " of $" + amount + " initiated for account: " + selectedAccount);
                amountField.clear();

            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid amount!");
            }
        }

        private void showTransactionHistory() {
            String selectedAccount = accountComboBox.getValue();
            if (selectedAccount == null) {
                showAlert("Error", "Please select an account first!");
                return;
            }

            // Create a new window for transaction history
            Stage historyStage = new Stage();
            VBox historyRoot = new VBox(10);
            historyRoot.setPadding(new Insets(20));

            Label titleLabel = new Label("com.banking.system.Transaction History - " + selectedAccount);
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            TextArea historyArea = new TextArea();
            historyArea.setEditable(false);
            historyArea.setPrefRowCount(15);
            historyArea.setText("com.banking.system.Transaction history for " + selectedAccount + " will appear here.\n\n" +
                    "You'll need to implement the logic to fetch and display\n" +
                    "the actual transaction history from your com.banking.system.Account object.");

            Button closeBtn = new Button("Close");
            closeBtn.setOnAction(e -> historyStage.close());

            historyRoot.getChildren().addAll(titleLabel, historyArea, closeBtn);

            Scene historyScene = new Scene(historyRoot, 500, 400);
            historyStage.setTitle("com.banking.system.Transaction History");
            historyStage.setScene(historyScene);
            historyStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
}

package com.banking.system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BankingSystemGUI extends Application {

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
        this.bank = new Bank("First National Bank - GUI");

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
        Label titleLabel = new Label("👥 Customer Management");
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

        Button createBtn = createStyledButton("Create Customer", "#2ecc71");
        Button backBtn = createStyledButton("← Back to Main Menu", "#95a5a6");

        createBtn.setOnAction(e -> {
            if (validateFields(nameField, addressField, phoneField, emailField)) {
                Customer customer = bank.createCustomer(
                        nameField.getText().trim(),
                        addressField.getText().trim(),
                        phoneField.getText().trim(),
                        emailField.getText().trim()
                );

                showAlert("Success", "Customer created successfully!\nCustomer ID: " + customer.getCustomerID());
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
        Label titleLabel = new Label("💳 Account Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.getChildren().add(titleLabel);
        root.setTop(header);

        // Form
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Enter Customer ID");

        ComboBox<String> accountTypeBox = new ComboBox<>();
        accountTypeBox.getItems().addAll("checking", "savings");
        accountTypeBox.setPromptText("Select Account Type");

        form.getChildren().addAll(
                new Label("Customer ID:"),
                customerIdField,
                new Label("Account Type:"),
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

        Button createBtn = createStyledButton("Create Account", "#2ecc71");
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
                    showAlert("Success", "Account created successfully!\nAccount Number: " + account.getAccountNumber());
                    customerIdField.clear();
                    accountTypeBox.setValue(null);
                    updateAccountList();
                    refreshAccountDisplay();
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid Customer ID!");
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
        Label titleLabel = new Label("💰 Transaction Center");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.getChildren().add(titleLabel);
        root.setTop(header);

        // Transaction form
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        accountComboBox = new ComboBox<>(accountList);
        accountComboBox.setPromptText("Select Account");

        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount");

        ComboBox<String> targetAccountBox = new ComboBox<>(accountList);
        targetAccountBox.setPromptText("Select Target Account (for transfers)");

        form.getChildren().addAll(
                new Label("Select Account:"),
                accountComboBox,
                new Label("Amount:"),
                amountField,
                new Label("Target Account (for transfers):"),
                targetAccountBox
        );

        root.setCenter(form);

        // Transaction buttons
        HBox transactionButtons = new HBox(10);
        transactionButtons.setAlignment(Pos.CENTER);
        transactionButtons.setPadding(new Insets(20));

        Button depositBtn = createStyledButton("💵 Deposit", "#2ecc71");
        Button withdrawBtn = createStyledButton("💸 Withdraw", "#e74c3c");
        Button transferBtn = createStyledButton("🔄 Transfer", "#f39c12");
        Button historyBtn = createStyledButton("📊 View History", "#9b59b6");

        // Transaction button actions
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
        // You'll need to modify the Bank class to provide a method to get all accounts
        accountList.addAll("Sample accounts will appear here");
    }

    private void refreshAccountDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account List:\n");
        sb.append("============\n");
        sb.append("Note: You'll need to implement getAllAccounts() method in Bank class\n");
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
            showAlert("Transaction", type.toUpperCase() + " of $" + amount + " initiated for account: " + selectedAccount);
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

        Label titleLabel = new Label("Transaction History - " + selectedAccount);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextArea historyArea = new TextArea();
        historyArea.setEditable(false);
        historyArea.setPrefRowCount(15);
        historyArea.setText("Transaction history for " + selectedAccount + " will appear here.\n\n" +
                "You'll need to implement the logic to fetch and display\n" +
                "the actual transaction history from your Account object.");

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> historyStage.close());

        historyRoot.getChildren().addAll(titleLabel, historyArea, closeBtn);

        Scene historyScene = new Scene(historyRoot, 500, 400);
        historyStage.setTitle("Transaction History");
        historyStage.setScene(historyScene);
        historyStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
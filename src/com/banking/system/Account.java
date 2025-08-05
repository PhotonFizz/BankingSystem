package com.banking.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected int customerID;
    protected LocalDate dateCreated;
    protected List<Transaction> transactionHistory;


    // Constructor
    public Account(String accountNumber, int customerID) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.customerID = customerID;
        this.dateCreated = LocalDate.now();
        this.transactionHistory = new ArrayList<>();
    }

    // Deposit method
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive");
            return false;
        }

        balance += amount;
        transactionHistory.add(new Transaction(accountNumber, "DEPOSIT", amount, "Cash deposit"));
        System.out.printf("Successfully deposited &%.2f. New balance: &%.2f\n", amount, balance);
        return true;
    }

    // Withdraw method
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdraw amount must be positive!");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds!");
            return false;
        }

        balance -= amount;
        transactionHistory.add(new Transaction(accountNumber, "WITHDRAWAL", amount, "Cash withdrawal"));
        System.out.printf("Successfully withdrew &%.2f. New balance: New balance: &%.2f\n", amount, balance);
        return true;
    }

    // Transfer method
    public boolean transfer(Account targetAccount, double amount) {
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive!");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds for transfer!");
            return false;
        }
        balance -= amount;
        targetAccount.balance += amount;

        // Add transaction records for both TRANSFER_OUT and TRANSFER_IN accounts
        transactionHistory.add(new Transaction(accountNumber, "TRANSFER_OUT", amount, "Transfer to " + targetAccount.getAccountNumber()));
        targetAccount.transactionHistory.add(new Transaction(targetAccount.getAccountNumber(), "TRANSFER_IN", amount, "Transfer from " + accountNumber));

        System.out.printf("Successfully transferred &%.2f to account %s%n", amount, targetAccount.getAccountNumber());
        return true;
    }

    public void displayTransactionHistory() {
        System.out.println("\n--- com.banking.system.Transaction History for com.banking.system.Account " + accountNumber + "---");

        if (transactionHistory.isEmpty()) {
            System.out.println("No transaction found");
        }
        else {
            for (Transaction transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public int getCustomerID() { return customerID; }
    public LocalDate getDateCreated() { return dateCreated; }

    // Abstract method to be implemented by subclass
    public abstract String getAccountType();

    @Override
    public String toString() {
        return String.format("com.banking.system.Account: %s | Type: %s | Balance: $%.2f | Created: %s", accountNumber, getAccountType(), balance, dateCreated);
    }
}

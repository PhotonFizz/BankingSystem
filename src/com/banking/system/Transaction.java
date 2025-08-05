package com.banking.system;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

 public class Transaction {
        private static int transactionCounter = 1000;
        private int transactionID;
        private String accountNumber;
        private String type;
        private double amount;
        private LocalDateTime timestamp;
        private String description;

        public Transaction(String accountNumber, String type, double amount, String description) {
            this.transactionID = ++transactionCounter;
            this.accountNumber = accountNumber;
            this.type = type;
            this.amount = amount;
            this.timestamp = LocalDateTime.now();
            this.description = description;
        }

        // Getters
        public int getTransactionID() { return transactionID;}
        public String getAccountNumber() { return accountNumber;}
        public String getType() { return type;}
        public double getAmount() { return amount;}
        public LocalDateTime getTimestamp() { return timestamp;}
        public String getDescription() { return description;}

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return String.format("ID: %d | %s | %s | $%.2f | %s | %s",
                    transactionID, timestamp.format(formatter), type, amount, accountNumber, description);
        }
 }
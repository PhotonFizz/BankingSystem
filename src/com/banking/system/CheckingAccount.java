package com.banking.system;

public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 500.0;
    private final boolean overdraftProtection;

    public CheckingAccount(String accountNumber, int customerID, boolean overdraftProtection) {
        super(accountNumber, customerID);
        this.overdraftProtection = overdraftProtection;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive!");
            return false;
        }

        double availableBalance = overdraftProtection ? balance + OVERDRAFT_LIMIT : balance;

        if (amount > availableBalance) {
            System.out.println("Insufficient funds! Available balance: $" + String.format("%.2f", availableBalance));
            return false;
        }

        balance -= amount;
        transactionHistory.add(new Transaction(accountNumber, "WITHDRAWAL", amount, "Cash withdrawal"));
        System.out.printf("Successfully withdrew $%.2f. New balance: $%.2f%n", amount, balance);

        if (balance < 0) {
            System.out.println("Warning: com.banking.system.Account is overdrawn!");
        }

        return true;
    }

    @Override
    public String getAccountType() {
        return "Checking" + (overdraftProtection ? " (with Overdraft)" : "");
    }

    public boolean hasOverdraftProtection() {
        return overdraftProtection;
    }
}

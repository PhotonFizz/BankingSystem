package com.banking.system;

public class SavingsAccount extends Account {
    private static final double ANNUAL_INTEREST_RATE = 0.02; // 2% annual interest
    private static final int MAX_WITHDRAWALS_PER_MONTH = 6;
    private int withdrawalsThisMonth;

    public SavingsAccount(String accountNumber, int customerID) {
        super(accountNumber, customerID);
        this.withdrawalsThisMonth = 0;
    }

    @Override
    public boolean withdraw(double amount) {
        if (withdrawalsThisMonth >= MAX_WITHDRAWALS_PER_MONTH) {
            System.out.println("Monthly withdrawal limit exceeded! Maximum " + MAX_WITHDRAWALS_PER_MONTH + " withdrawals allowed.");
            return false;
        }

        boolean success = super.withdraw(amount);
        if (success) {
            withdrawalsThisMonth++;
        }
        return success;
    }

    public void calculateMonthlyInterest() {
        double monthlyInterest = balance * (ANNUAL_INTEREST_RATE / 12);
        if (monthlyInterest > 0) {
            balance += monthlyInterest;
            transactionHistory.add(new Transaction(accountNumber, "INTEREST", monthlyInterest, "Monthly interest"));
            System.out.printf("Interest applied: $%.2f. New balance: $%.2f%n", monthlyInterest, balance);
        }
        withdrawalsThisMonth = 0; // Reset monthly withdrawal counter
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    public int getWithdrawalsThisMonth() {
        return withdrawalsThisMonth;
    }
}

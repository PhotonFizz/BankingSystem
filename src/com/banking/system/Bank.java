package com.banking.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bank {
    private String bankName;
    private Map<Integer, Customer> customers;
    private Map<String, Account> accounts;
    private int nextCustomerID;

    public Bank(String bankName) {
        this.bankName = bankName;
        this.customers = new HashMap<>();
        this.accounts = new HashMap<>();
        this.nextCustomerID = 1001;
    }

    public Customer createCustomer(String name, String address, String phone, String email) {
        Customer customer = new Customer(nextCustomerID++, name, address, phone, email);
        customers.put(customer.getCustomerID(), customer);
        System.out.println("com.banking.system.Customer created successfully!");
        System.out.println(customer);
        return customer;
    }

    public Account createAccount(int customerID, String accountType) {
        if (!customers.containsKey(customerID)) {
            System.out.println("com.banking.system.Customer not found!");
            return null;
        }

        String accountNumber = generateAccountNumber();
        Account account;

        switch (accountType.toLowerCase()) {
            case "checking":
                account = new CheckingAccount(accountNumber, customerID, true);
                break;
            case "savings":
                account = new SavingsAccount(accountNumber, customerID);
                break;
            default:
                System.out.println("Invalid account type! Use 'checking' or 'savings'");
                return null;
        }

        accounts.put(accountNumber, account);
        System.out.println("com.banking.system.Account created successfully!");
        System.out.println(account);
        return account;
    }

    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public Customer findCustomer(int customerID) {
        return customers.get(customerID);
    }

    public void displayAllAccounts() {
        System.out.println("\n--- All com.banking.system.Bank Accounts ---");
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account account : accounts.values()) {
                Customer customer = customers.get(account.getCustomerID());
                System.out.println(account + " | Owner: " + customer.getName());
            }
        }
    }

    private String generateAccountNumber() {
        Random random = new Random();
        return String.format("%010d", random.nextInt(1000000000));
    }

    public String getBankName() {
        return bankName;
    }
}

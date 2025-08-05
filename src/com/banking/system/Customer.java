package com.banking.system;

public class Customer {
    private int customerID;
    private String name;
    private String address;
    private String phone;
    private String email;

    public Customer(int customerID, String name, String address, String phone, String email) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Getters
    public int getCustomerID() { return customerID; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // Setters
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "com.banking.system.Customer ID: " + customerID + "\nName: " + name +
                "\nAddress: " + address + "\nPhone: " + phone + "\nEmail: " + email;
    }
}

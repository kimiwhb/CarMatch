package com.awang.carmatch;

public class HelperClass {

    String name, email, phone, password, budget;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public HelperClass(String name, String email, String phone, String budget, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.budget = budget;
    }

    public HelperClass() {
    }
}

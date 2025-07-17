package model;

import org.json.JSONObject;

// Represents an expense with name and amount
public class Expense {
    private String name;
    private double amount;

    // EFFECTS: Initializes name and amount based on the corresponding name and amount
    // in the method parameter
    public Expense(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("amount", amount);
        return json;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }
}

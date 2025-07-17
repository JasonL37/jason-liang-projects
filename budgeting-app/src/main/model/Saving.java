package model;

import org.json.JSONObject;

// Represents a saving with name and amount
public class Saving {
    private String name;
    private double amount;

    // EFFECTS: Initializes name and amount based on the corresponding name and amount
    // in the method parameter
    public Saving(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    // Inspired by toJson method in JsonSerializationDemo
    // EFFECTS: parses a saving to a JSONObject
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

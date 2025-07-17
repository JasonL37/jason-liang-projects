package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


// Represents a budget with a balance, income, and a listOfExpenses and listOfSavings
public class Budget {

    private double income;
    private double balance;
    private List<Expense> listOfExpenses;
    private List<Saving> listOfSavings;

    // EFFECTS: Sets income and balance to the parameters balance and income,
    // income is added to balance, listOfExpenses and listOfSavings are initialized
    public Budget(double balance, double income) {
        this.income = income;
        this.balance = balance;
        this.balance += income;
        listOfExpenses = new ArrayList<>();
        listOfSavings = new ArrayList<>();
    }

    public double getIncome() {
        return income;
    }

    // REQUIRES: a name with a length greater than 0
    // MODIFIES: this
    // EFFECTS: Creates a new expense from the parameters name and amount.
    // Subtracts balance by amount and adds expense to listOfExpenses
    public void addExpense(String name, double amount) {
        Expense expense = new Expense(name, amount);
        balance -= amount;
        listOfExpenses.add(expense);
    }

    // REQUIRES: a name with a length greater than 0
    // MODIFIES: this
    // EFFECTS: Creates a new saving from the parameters name and amount.
    // Adds balance by amount and adds saving to listOfSaving
    public void addSaving(String name, double amount) {
        Saving saving = new Saving(name, amount);
        balance += amount;
        listOfSavings.add(saving);
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("income", income);
        jsonObject.put("balance", balance);
        jsonObject.put("listOfSavings", listOfSavingsToJson());
        jsonObject.put("listOfExpenses", listOfExpensesToJson());
        return jsonObject;
    }

    public JSONArray listOfSavingsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Saving s : listOfSavings) {
            jsonArray.put(s.toJson());
        }
        return jsonArray;
    }

    public JSONArray listOfExpensesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Expense e : listOfExpenses) {
            jsonArray.put(e.toJson());
        }
        return jsonArray;
    }

    public double getBalance() {
        return balance;
    }

    public List<Expense> getListOfExpenses() {
        return listOfExpenses;
    }

    public List<Saving> getListOfSavings() {
        return listOfSavings;
    }
}

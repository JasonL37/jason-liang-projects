package persistence;

import model.Account;
import model.Budget;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

public class AccountReader {
    private String input;

    // Inspired from JsonSerializationDemo
    // EFFECTS: Gets the input String with where the file is located
    public AccountReader(String input) {
        this.input = input;
    }

    // Inspired by the read method in JsonSerializationDemo
    // EFFECTS: Reads an account from a file and returns it
    // If an error occurs when reading the data from the file, throws an IOException
    public Account read() throws IOException {
        String data = readFile(input);
        JSONObject jsonObject = new JSONObject(data);
        return toAccount(jsonObject);
    }

    // Inspired by the parseWorkroom method
    // EFFECTS: changes a JSONObject to an account and returns that account
    private Account toAccount(JSONObject jsonObject) {
        String accName = jsonObject.getString("accName");
        double income = jsonObject.getDouble("income");
        int creditScore = jsonObject.getInt("creditScore");
        boolean accStatus = jsonObject.getBoolean("accStatus");
        String credScoreStatus = jsonObject.getString("credScoreStatus");
        Account acc = new Account(accName, income, creditScore, accStatus, credScoreStatus);
        addBudgets(acc, jsonObject);
        addApproachingBills(acc, jsonObject);
        addUpcomingBills(acc, jsonObject);
        addOverdueBills(acc, jsonObject);
        return acc;
    }

    // Inspired by the addThingies method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses budgetList from a JSONObject and adds it into the budgetList of acc
    private void addBudgets(Account acc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("budgetList");
        for (Object json : jsonArray) {
            JSONObject nextBudget = (JSONObject) json;
            addBudget(acc, nextBudget);
        }
    }

    // Inspired by the addThingy method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses budget from a JSONObject and adds it into the budgetList of acc
    private void addBudget(Account acc, JSONObject jsonObject) {
        double income = jsonObject.getDouble("income");
        double balance = jsonObject.getDouble("balance");
        Budget budget = new Budget(balance, income);
        addSavings(budget, jsonObject);
        addExpenses(budget, jsonObject);
        acc.addBudget(balance);
    }

    // Inspired by the addThingies method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses upcomingBillsList from a JSONObject and adds it into the
    // upcomingBillsList in acc
    private void addUpcomingBills(Account acc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("upcomingBillsList");
        for (Object json : jsonArray) {
            JSONObject nextBill = (JSONObject) json;
            addBill(acc, nextBill);
        }
    }

    // Inspired by the addThingies method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses approachingBillsList from a JSONObject and adds it into the
    // approachingBillsList in acc
    private void addApproachingBills(Account acc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("approachingBillsList");
        for (Object json : jsonArray) {
            JSONObject nextBill = (JSONObject) json;
            addBill(acc, nextBill);
        }
    }

    // Inspired by the addThingies method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses overdueBillsList from a JSONObject and adds it into the
    // overdueBillsList in acc
    private void addOverdueBills(Account acc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("overdueBillsList");
        for (Object json : jsonArray) {
            JSONObject nextBill = (JSONObject) json;
            addBill(acc, nextBill);
        }
    }

    // Inspired by the addThingy method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses a bill from a JSONObject and adds it into the acc
    private void addBill(Account acc, JSONObject jsonObject) {
        String name = jsonObject.getString("billName");
        double amountOwed = jsonObject.getDouble("amountOwed");
        String date = jsonObject.getString("dueDate");
        LocalDate dueDate = LocalDate.parse(date);
        acc.addBill(name, amountOwed, dueDate);
    }

    // Inspired by the addThingies method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses listOfSavings from a JSONObject and adds into a budget in acc
    private void addSavings(Budget budget, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("listOfSavings");
        for (Object json : jsonArray) {
            JSONObject nextSaving = (JSONObject) json;
            addSaving(budget, nextSaving);
        }
    }

    // Inspired by the addThingy method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses a saving from a JSONObject and adds into a budget in acc
    private void addSaving(Budget budget, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double amount = jsonObject.getDouble("amount");
        budget.addSaving(name, amount);
    }

    // Inspired by the addThingies method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses listOfExpenses from a JSONObject and adds into a budget in acc
    private void addExpenses(Budget budget, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("listOfExpenses");
        for (Object json : jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            addExpense(budget, nextExpense);
        }
    }

    // Inspired by the addThingy method in JSONSerializationDemo
    // MODIFIES: acc
    // EFFECTS: parses an expense from a JSONObject and adds into a budget in acc
    private void addExpense(Budget budget, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double amount = jsonObject.getDouble("amount");
        budget.addExpense(name, amount);
    }

    // Inspired by the readFile method in JsonSerializationDemo
    // EFFECTS: reads the input file as a string and then returns it
    public String readFile(String input) throws IOException {
        StringBuilder sourceBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(input), StandardCharsets.UTF_8)) {
            stream.forEach(s -> sourceBuilder.append(s));
        }
        return sourceBuilder.toString();
    }
}

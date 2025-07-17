package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


// Represents a user account that has a name, credit score, income, account status,
// credit score status, list of budgets, and a list of upcoming, approaching, and overdue bills
public class Account {
    private String accName; // Name of the account
    private List<Budget> budgetList; // List of budgets for an account
    private int creditScore;
    private double income;
    private boolean accStatus;
    private String credScoreStatus;
    private List<Bills> upcomingBillsList;
    private List<Bills> overdueBillsList;
    private List<Bills> approachingBillsList;

    // EFFECTS: Sets accStatus to false, accName to null, income to 0, creditScore = 300
    // and initializes budgetList, upcomingBillsList, approachingBillsList, and overdueBillsList
    // as ArrayLists
    public Account() {
        this.accStatus = false;
        this.accName = "";
        this.income = 0;
        this.creditScore = 300;
        this.credScoreStatus = "";
        budgetList = new ArrayList<>();
        upcomingBillsList = new ArrayList<>();
        approachingBillsList = new ArrayList<>();
        overdueBillsList = new ArrayList<>();
    }

    // EFFECTS: Calls the default constructor and then sets accName, income, creditScore, accStatus, and credScoreStatus
    // to the accName, income, creditScore, accStatus, credScoreStatus in the parameter of the constructor
    public Account(String accName, double income, int creditScore, boolean accStatus, String credScoreStatus) {
        this();
        this.accName = accName;
        this.income = income;
        this.creditScore = creditScore;
        this.accStatus = accStatus;
        this.credScoreStatus = credScoreStatus;
    }

    // Inspired by toJson method in JsonSerializationDemo
    // EFFECTS: Puts the fields and lists in the class into a JSONObject and returns that
    // JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("accStatus", accStatus);
        json.put("accName", accName);
        json.put("income", income);
        json.put("creditScore", creditScore);
        json.put("credScoreStatus", credScoreStatus);
        json.put("budgetList", budgetListToJson());
        json.put("upcomingBillsList", upcomingBillsListToJson());
        json.put("approachingBillsList", approachingBillsListToJson());
        json.put("overdueBillsList", overdueBillsListToJson());
        return json;
    }

    // Inspired by thingiesToJson method in JsonSerializationDemo
    // EFFECTS: Puts the budgets within each budgetList into a JSONArray
    public JSONArray budgetListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Budget b : budgetList) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

    // Inspired by thingiesToJson method in JsonSerializationDemo
    // EFFECTS: Puts the bills within each upcomingBillsList into a JSONArray
    public JSONArray upcomingBillsListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Bills b : upcomingBillsList) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

    // Inspired by thingiesToJson method in JsonSerializationDemo
    // EFFECTS: Puts the bills within each approachingBillsList into a JSONArray
    public JSONArray approachingBillsListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Bills b : approachingBillsList) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

    // Inspired by thingiesToJson method in JsonSerializationDemo
    // EFFECTS: Puts the bills within each overdueBillsList into a JSONArray
    public JSONArray overdueBillsListToJson() {

        JSONArray jsonArray = new JSONArray();

        for (Bills b : overdueBillsList) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: Makes a string that summarizes the balance of each budget and their index
    // and returns that string. If the budgetList is empty, it will return null.
    public String printSummary() {
        if (budgetList.isEmpty()) {
            return null;
        }

        StringBuilder result = new StringBuilder("Budget Index\t|\t Balance\t\t\n");
        int index = 0;
        for (Budget b : budgetList) {
            result.append("\t\t").append(index).append("\t\t|\t");
            result.append(b.getBalance()).append("\t\n");
            index++;
        }
        return result.toString();
    }

    public double getIncome() {
        return income;
    }

    public String getAccName() {
        return accName;
    }

    public List<Budget> getBudgetList() {
        return budgetList;
    }

    public List<Bills> getUpcomingBillsList() {
        return upcomingBillsList;
    }

    public List<Bills> getApproachingBillsList() {
        return approachingBillsList;
    }

    public List<Bills> getOverdueBillsList() {
        return overdueBillsList;
    }

    public double getCreditScore() {
        return creditScore;
    }

    public boolean getAccStatus() {
        return accStatus;
    }

    public String getCredScoreStatus() {
        return credScoreStatus;
    }

    // MODIFIES: this
    // EFFECTS: Creates a budget object and adds the budget to budgetList
    public void addBudget(double balance) {
        Budget budget = new Budget(balance, income);
        budgetList.add(budget);
        EventLog.getInstance().logEvent(new Event("Added a budget to an account"));
    }

    // MODIFIES: this
    // EFFECTS: Returns false is budgetList is empty.
    // Otherwise, returns true after successfully removing a budget
    public boolean removeBudget(int index) {
        if (budgetList.isEmpty()) {
            return false;
        } else {
            budgetList.remove(index);
            EventLog.getInstance().logEvent(new Event("Removed a budget from an account"));
            return true;
        }
    }

    // REQUIRES: Length of name > 0, amount > 0, dueDate being a valid date
    // MODIFIES: this
    // EFFECTS: Creates a new bill with the values of the parameter.
    // Adds the bill into the corresponding list based on the urgency of the due date
    public void addBill(String name, double amount, LocalDate dueDate) {
        Bills bill = new Bills(name, amount, dueDate);
        if (bill.determineUrgency().equals("Overdue")) {
            overdueBillsList.add(bill);
        } else if (bill.determineUrgency().equals("Approaching")) {
            approachingBillsList.add(bill);
        } else {
            upcomingBillsList.add(bill);
        }
    }

    //REQUIRES: Length of name > 0, income >= 0 and 900 >= creditScore >= 300
    //MODIFIES: this
    //EFFECTS: Initializes name, income and creditScore based on the parameters
    // and changes the status of credScoreStatus based on the value of creditScore
    public void setUpAccount(String name, double income, int creditScore) {
        this.accName = name;
        this.income = income;
        this.creditScore = creditScore;
        accStatus = true;
        if (creditScore == 900) {
            credScoreStatus = "Perfect";
        } else if (creditScore >= 760) {
            credScoreStatus = "Excellent";
        } else if (creditScore >= 725) {
            credScoreStatus = "Great";
        } else if (creditScore >= 660) {
            credScoreStatus = "Good";
        } else if (creditScore >= 560) {
            credScoreStatus = "Acceptable";
        } else {
            credScoreStatus = "Poor";
        }
    }
}

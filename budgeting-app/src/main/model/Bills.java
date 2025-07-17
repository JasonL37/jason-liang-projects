package model;

import org.json.JSONObject;

import java.time.LocalDate;

// Represents a bill that has a name, amountOwed and when the bill is due
public class Bills {
    private String billName;
    private double amountOwed;
    private LocalDate dueDate;

    // EFFECTS: Initializes billName, amountOwed, and dueDate based on the
    // corresponding billName, amountOwed, and dueDate in the method parameter
    public Bills(String billName, double amountOwed, LocalDate dueDate) {
        this.billName = billName;
        this.amountOwed = amountOwed;
        this.dueDate = dueDate;
    }

    public String getBillName() {
        return billName;
    }

    public double getAmountOwed() {
        return amountOwed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    // https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
    // EFFECTS: Checks if a bill is overdue, approaching, or upcoming;
    // if a bill's dueDate is after the current date, then billStatus is set to Overdue,
    // if the year of dueDate is the same as the current year, and the days between the dueDate
    // and the current date is less than 31, the billStatus is set to Approaching
    // Otherwise, the billStatus is set to Upcoming
    // Returns the status of the bill
    public String determineUrgency() {
        String billStatus;
        if (LocalDate.now().isAfter(dueDate)) {
            billStatus = "Overdue";
        } else if ((dueDate.getDayOfYear() - LocalDate.now().getDayOfYear() < 31)
                && dueDate.getYear() == LocalDate.now().getYear()) {
            billStatus = "Approaching";
        } else {
            billStatus = "Upcoming";
        }
        return billStatus;
    }

    // Inspired by toJson method in JsonSerializationDemo
    // EFFECTS: parses a bill to a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("billName", billName);
        json.put("amountOwed", amountOwed);
        json.put("dueDate", dueDate.toString());
        return json;
    }
}

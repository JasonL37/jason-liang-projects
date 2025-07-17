package ui;

import model.Account;
import persistence.AccountReader;
import persistence.AccountWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

// Represents a budgeting application with an account
public class BudgetApp {

    private Account account;
    private Scanner scan;
    private AccountReader accReader;
    private AccountWriter accWriter;
    private static final String STORE_PATH = "./data/account.json";

    // Inspired from TellerApp
    // EFFECTS: Runs the budget app
    public BudgetApp() throws FileNotFoundException {
        account = new Account();
        scan = new Scanner(System.in);
        accReader = new AccountReader(STORE_PATH);
        accWriter = new AccountWriter(STORE_PATH);
        runBudgetApp();
    }

    // Inspired by the runTeller method in TellerApp
    // MODIFIES: this
    // EFFECTS: Displays the menu and gets user input. If input corresponds to
    // the value corresponding to quit in the menu, the program ends, otherwise the
    // program loops
    public void runBudgetApp() {
        boolean play = true;
        int input = 0;
        while (play) {
            displayMenu();

            input = scan.nextInt();
            if (input == 10) {
                System.out.println("Do you want to save the changes to your account?");
                scan.next();
                String exitCommand = scan.nextLine();
                if (exitCommand.contains("y")) {
                    saveAccount();
                }
                play = false;
            } else {
                processInput(input);
            }
        }
    }

    // Inspired by the displayMenu method of the TellerApp
    // EFFECTS: Prints out the menu to the user
    public void displayMenu() {
        System.out.println("\nSelect from");
        System.out.println("\t1) Set up account name, income and credit score");
        System.out.println("\t2) Add budget to account");
        System.out.println("\t3) Remove budget from account");
        System.out.println("\t4) Add bill to account");
        System.out.println("\t5) View budget summary");
        System.out.println("\t6) Add a savings to a budget");
        System.out.println("\t7) Add an expense to a budget");
        System.out.println("\t8) Load account from file");
        System.out.println("\t9) Save account to file");
        System.out.println("\t10) Quit\n");
    }

    // Inspired by the ProcessCommand method from TellerApp
    // MODIFIES: this
    // EFFECTS: Calls the corresponding method displayed on menu based on the value of command
    public void processInput(int command) {
        if (command == 1) {
            setUpAccount();
        } else if (command == 2) {
            addBudget();
        } else if (command == 3) {
            removeBudget();
        } else if (command == 4) {
            addBill();
        } else if (command == 5) {
            viewBudgetSummary();
        } else if (command == 6) {
            addSavings();
        } else if (command == 7) {
            addExpense();
        } else if (command == 8) {
            loadAccount();
        } else if (command == 9) {
            saveAccount();
        } else {
            System.out.println("That is an invalid command");
        }
    }

    // MODIFIES: this
    // EFFECTS: Asks the user for the name, income and credit score of the account
    // to set up an account
    public void setUpAccount() {
        System.out.println("What is the name of the account?");
        String name = scan.next();
        System.out.println("What is the income of the account?");
        double income = scan.nextDouble();
        if (income < 0) {
            System.out.println("This is an invalid income.");
        } else {
            System.out.println("What is the credit score of the account?");
            int creditScore = scan.nextInt();
            if (creditScore > 900 || creditScore < 300) {
                System.out.println("This is an invalid credit score.");
            } else {
                account.setUpAccount(name, income, creditScore);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: If account has been set up, asks the user for a balance and creates a new budget
    public void addBudget() {
        if (!account.getAccStatus()) {
            System.out.println("You must set up your account to add a budget.");
        } else {
            System.out.println("What is the starting balance?");
            double balance = scan.nextInt();
            account.addBudget(balance);
        }
    }

    // EFFECTS: Prints a summary of each budget in the account
    public void viewBudgetSummary() {
        System.out.println(account.printSummary());
    }

    // MODIFIES: this
    // EFFECTS: Removes a budget at an index in the list of budgets
    public void removeBudget() {
        System.out.println("What is the index of the budget you want to remove?");
        int index = scan.nextInt();
        if (index > account.getBudgetList().size()) {
            System.out.println("There is no budget at that index.");
        } else if (!account.removeBudget(index)) {
            System.out.println("There are no budgets");
        } else {
            account.removeBudget(index);
            System.out.println("The budget has been removed");
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates a new savings from the name and amount from the user for
    // the index of a budget that the user gives
    public void addSavings() {
        if (account.getBudgetList().isEmpty()) {
            System.out.println("There is no budget");
        } else {
            System.out.println("What is the name of the savings?");
            String name = scan.next();
            System.out.println("What is the amount of the savings?");
            double amount = scan.nextInt();
            System.out.println("What is the index of the budget you want to apply the savings to?");
            int index = scan.nextInt();
            if (index > account.getBudgetList().size() || index < 0) {
                System.out.println("That is an invalid budget index");
            }
            account.getBudgetList().get(index).addSaving(name, amount);
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates a new expense from the name and amount from the user for
    // the index of a budget that the user gives
    public void addExpense() {
        if (account.getBudgetList().isEmpty()) {
            System.out.println("There is no budget");
        } else {
            System.out.println("What is the name of the expense?");
            String name = scan.next();
            System.out.println("What is the amount of the expense?");
            double amount = scan.nextInt();
            System.out.println("What is the index of the budget you want to apply the expense to?");
            int index = scan.nextInt();
            if (index > account.getBudgetList().size() || index < 0) {
                System.out.println("That is an invalid budget index");
            }
            account.getBudgetList().get(index).addExpense(name, amount);
        }
    }

    // MODIFIES: this
    // EFFECTS: Asks the user for name, amount awed and the due date and creates
    // a new bill from those inputs
    public void addBill() {
        if (!account.getAccStatus()) {
            System.out.println("You must set up your account to add a bill.");
        } else {
            System.out.println("What is the name of the bill?");
            String name = scan.next();
            System.out.println("What is the amount owed?");
            double amountOwed = scan.nextDouble();
            if (amountOwed < 0) {
                System.out.println("That is an invalid amount.");
            } else {
                System.out.println("What year is the bill due?");
                int year = scan.nextInt();
                System.out.println("What month is the bill due?");
                int month = scan.nextInt();
                System.out.println("What day is the bill due?");
                int day = scan.nextInt();
                account.addBill(name, amountOwed, LocalDate.of(year, month, day));
            }
        }
    }

    // Inspired from the loadWorkRoom method in JsonSerializationDemo
    // EFFECTS: Loads everything from file
    private void loadAccount() {
        try {
            account = accReader.read();
            System.out.println("Loaded " + account.getAccName() + " from " + STORE_PATH);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + STORE_PATH);
        }
    }

    // Inspired from the saveWorkRoom method in JsonSerializationDemo
    // EFFECTS: Saves everything to file
    private void saveAccount() {
        try {
            accWriter.openWriter();
            accWriter.write(account);
            accWriter.closeWriter();
            System.out.println("Saved " + account.getAccName() + " to " + STORE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + STORE_PATH);
        }
    }
}

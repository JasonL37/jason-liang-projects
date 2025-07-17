package ui;

import model.Account;
import persistence.AccountReader;
import persistence.AccountWriter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents the BudgetApp's main window frame
public class BudgetAppGUI extends JFrame {

    private Account acc;
    private AccountReader accReader;
    private AccountWriter accWriter;
    private static final String STORE_PATH = "./data/account.json";
    private ImageIcon image = loadImage();
    private JLabel imageAsLabel = new JLabel(image);

    // Inspired from SpaceInvadersBase Constructor and SimpleDrawingPlayer
    // EFFECTS: Sets up the panel and prompts the user if they want to load from file once the program starts
    public BudgetAppGUI() {
        super("BudgetApp");
        acc = new Account();
        accReader = new AccountReader(STORE_PATH);
        accWriter = new AccountWriter(STORE_PATH);
        setMinimumSize(new Dimension(600, 600));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
        addBudgetPanel();
        addWindowListener(new BudgetAppWindow());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        load();
    }

    // Inspired from AlarmSystem
    // EFFECTS: Creates the panel
    public void addBudgetPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        panel.add(new JButton((new AddAccountAction())));
        panel.add(new JButton(new AddBudgetAction()));
        panel.add(new JButton(new RemoveBudgetAction()));
        panel.add(new JButton("Add bill to account"));
        panel.add(new JButton(new ViewBudgetAction()));
        panel.add(new JButton(new AddSavingAction()));
        panel.add(new JButton(new AddExpenseAction()));
        panel.add(new JButton(new SaveToFileAction()));
        panel.add(new JButton(new LoadFromFileAction()));
        panel.add(imageAsLabel);
        add(panel);
    }

    // EFFECTS: Displays an error message on the screen
    public void displayErrorMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "System Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: Loads everything from file. Displays an error message if the file cannot be found
    public void load() {
        int input = JOptionPane.showConfirmDialog(null, "Do you want to load from file?", "Loading",
                JOptionPane.YES_NO_OPTION);
        if (input == 0) {
            try {
                acc = accReader.read();
                JOptionPane.showMessageDialog(null, "Loaded " + acc.getAccName() + " from " + STORE_PATH,
                        "Loaded", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                displayErrorMessage("Unable to load from file: " + STORE_PATH);
            }
        }
    }

    // EFFECTS: Saves everything to file. Displays an error message if it cannot write to the file
    public void save() {
        int input = JOptionPane.showConfirmDialog(null, "Do you want to save to file?", "Saving",
                JOptionPane.YES_NO_OPTION);
        if (input == 0) {
            try {
                accWriter.openWriter();
                accWriter.write(acc);
                accWriter.closeWriter();
                JOptionPane.showMessageDialog(null, "Saved " + acc.getAccName() + " to " + STORE_PATH,
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                displayErrorMessage("Unable to write to file: " + STORE_PATH);
            }
        }
    }

    // Inspired by AlarmSystem
    // MODIFIES: this
    // EFFECTS: Creates a window asking the user for the name, income and credit score of the account
    // to set up an account. Displays an error message if any values are invalid.
    private class AddAccountAction extends AbstractAction {
        // EFFECTS: Creates an action to set up an account
        AddAccountAction() {
            super("Set up account");
        }

        // EFFECTS: Asks the user for values to set up an account
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                String accName = JOptionPane.showInputDialog(null,
                        "What is the name of the account?", "Enter account name",
                        JOptionPane.QUESTION_MESSAGE);
                double income = Double.parseDouble(JOptionPane.showInputDialog(null,
                        "What is the income of the account?", "Enter income",
                        JOptionPane.QUESTION_MESSAGE));
                if (income < 0) {
                    displayErrorMessage("This is an invalid income");
                } else {
                    int creditScore = Integer.parseInt(JOptionPane.showInputDialog(null,
                            "What is the credit score of the account?", "Enter credit score",
                            JOptionPane.QUESTION_MESSAGE));
                    if (creditScore > 900 || creditScore < 300) {
                        displayErrorMessage("This is an invalid credit score");
                    } else {
                        acc.setUpAccount(accName, income, creditScore);
                    }
                }
            } catch (NumberFormatException e) {
                displayErrorMessage(e.getMessage() + "\nYou must give a number");
            }
        }
    }

    // Inspired by AlarmSystem
    // MODIFIES: this
    // EFFECTS: If account has been set up, asks the user for a balance and creates a new budget.
    // If the value is invalid, it displays an error message
    private class AddBudgetAction extends AbstractAction {
        // EFFECTS: Creates an action for when the user wants to add a budget to the account
        AddBudgetAction() {
            super("Add budget to account");
        }

        // EFFECTS: Adds a budget. Asks the user if they want to add multiple budgets.
        @Override
        public void actionPerformed(ActionEvent event) {
            if (!acc.getAccStatus()) {
                displayErrorMessage("You must set up your account to add a budget.");
            } else {
                try {
                    double balance = Double.parseDouble(JOptionPane.showInputDialog(null,
                            "What is the starting balance?", "Enter starting balance",
                            JOptionPane.QUESTION_MESSAGE));
                    int iterate = Integer.parseInt(JOptionPane.showInputDialog(null,
                            "How many of that budget would you like to add?", "Enter number of budgets to add",
                            JOptionPane.QUESTION_MESSAGE));
                    if (iterate > 0) {
                        for (int i = 0; i < iterate; i++) {
                            acc.addBudget(balance);
                        }
                    } else {
                        displayErrorMessage("That is an invalid amount of budgets to add.");
                    }
                } catch (NumberFormatException e) {
                    displayErrorMessage(e.getMessage() + "\nYou must give a number");
                }
            }
        }
    }

    // Inspired by AlarmSystem
    // MODIFIES: this
    // EFFECTS: Removes a budget at an index in the list of budgets. Displays an error message
    // if the index cannot be found
    private class RemoveBudgetAction extends AbstractAction {
        // EFFECTS: Creates an action for when the user wants to remove a budget
        RemoveBudgetAction() {
            super("Remove budget");
        }

        // EFFECTS: Removes a budget at an index in the list of budgets. If the index cannot be
        //found, displays an error message
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                int index = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "What is the index of the budget you want to remove?", "Enter index",
                        JOptionPane.QUESTION_MESSAGE));
                if (index > acc.getBudgetList().size()) {
                    displayErrorMessage("There is no budget at that index.");
                } else if (!acc.removeBudget(index)) {
                    displayErrorMessage("There are no budgets");
                } else {
                    acc.removeBudget(index);
                }
            } catch (NumberFormatException e) {
                displayErrorMessage(e.getMessage() + "\nYou must give a number");
            } catch (IndexOutOfBoundsException e) {
                displayErrorMessage("There is no budget at that index");
            }
        }
    }

    // Inspired by AlarmSystem
    // EFFECTS: Saves to a file when the button is clicked
    private class SaveToFileAction extends AbstractAction {
        // EFFECTS: Creates an action for when the user wants to save to file
        SaveToFileAction() {
            super("Save account to file");
        }

        // EFFECTS: Saves to a file
        @Override
        public void actionPerformed(ActionEvent event) {
            save();
        }
    }

    // Inspired by AlarmSystem
    // EFFECTS: Loads from a file when the button is clicked
    private class LoadFromFileAction extends AbstractAction {
        LoadFromFileAction() {
            super("Load account from file");
        }

        // EFFECTS: Loads from a file
        @Override
        public void actionPerformed(ActionEvent event) {
            load();
        }
    }

    // Inspired by AlarmSystem
    // EFFECTS: Prints a summary of each budget in the account when the button is clicked
    private class ViewBudgetAction extends AbstractAction {
        ViewBudgetAction() {
            super("View budget summary");
        }

        // EFFECTS: Displays the budgets in a list
        @Override
        public void actionPerformed(ActionEvent event) {
            JOptionPane.showMessageDialog(null, new JScrollPane(new JTable(new BudgetTable())));

        }
    }

    // Inspired by AlarmSystem
    // MODIFIES: this
    // EFFECTS: Creates a new savings from the name and amount from the user for
    // the index of a budget that the user gives when the button is clicked
    private class AddSavingAction extends AbstractAction {

        // EFFECTS: Adds a savings to a budget
        AddSavingAction() {
            super("Add a savings to a budget");
        }

        // EFFECTS: Adds a savings to the budget the specified index
        @Override
        public void actionPerformed(ActionEvent event) {
            if (acc.getBudgetList().isEmpty()) {
                displayErrorMessage("There is no budget");
            } else {
                try {
                    String name = JOptionPane.showInputDialog(null,
                            "What is the name of the savings?", "Enter savings name",
                            JOptionPane.QUESTION_MESSAGE);
                    double amount = Double.parseDouble(JOptionPane.showInputDialog(null,
                            "What is the amount of the savings?", "Enter amount",
                            JOptionPane.QUESTION_MESSAGE));
                    int index = Integer.parseInt(JOptionPane.showInputDialog(null,
                            "What is the index of the budget you want to apply the savings to?", "Enter index",
                            JOptionPane.QUESTION_MESSAGE));
                    if (index > acc.getBudgetList().size() || index < 0) {
                        displayErrorMessage("That is an invalid budget index");
                    } else {
                        acc.getBudgetList().get(index).addSaving(name, amount);
                    }
                } catch (NumberFormatException e) {
                    displayErrorMessage(e.getMessage() + "\nYou must give a number");
                }
            }
        }
    }

    // Inspired by AlarmSystem
    // MODIFIES: this
    // EFFECTS: Creates a new expense from the name and amount from the user for
    // the index of a budget that the user gives when the button is clicked
    private class AddExpenseAction extends AbstractAction {

        // EFFECTS: Creates an action related to the button Add an expense to a budget
        AddExpenseAction() {
            super("Add an expense to a budget");
        }

        // MODIFIES: this
        // EFFECTS: Creates a new expense from the name, amount and index from the user
        // that the user gives when an event occurs to the button
        @Override
        public void actionPerformed(ActionEvent event) {
            if (acc.getBudgetList().isEmpty()) {
                displayErrorMessage("There is no budget");
            } else {
                try {
                    String name = JOptionPane.showInputDialog(null,
                            "What is the name of the expense?", "Enter expense name",
                            JOptionPane.QUESTION_MESSAGE);
                    double amount = Double.parseDouble(JOptionPane.showInputDialog(null,
                            "What is the amount of the expense?", "Enter amount",
                            JOptionPane.QUESTION_MESSAGE));
                    int index = Integer.parseInt(JOptionPane.showInputDialog(null,
                            "What is the index of the budget you want to apply the expense to?", "Enter index",
                            JOptionPane.QUESTION_MESSAGE));
                    if (index > acc.getBudgetList().size() || index < 0) {
                        displayErrorMessage("That is an invalid budget index");
                    } else {
                        acc.getBudgetList().get(index).addExpense(name, amount);
                    }
                } catch (NumberFormatException e) {
                    displayErrorMessage(e.getMessage() + "\nYou must give a number");
                }
            }
        }
    }

    // Inspired by TrafficLights
    // EFFECTS: Loads an image from the images directory
    private ImageIcon loadImage() {
        String sep = System.getProperty("file.separator");
        image = new ImageIcon(System.getProperty("user.dir") + sep
                + "images" + sep + "profile1.png");
        return image;
    }

    // Inspired by AlarmSystem
    // Creates a table of all the budgets in account
    private class BudgetTable extends AbstractTableModel {
        // EFFECTS: Sets the name of the column headers based on where the column is
        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "Index";
            } else {
                return "Budget Balance";
            }
        }

        @Override
        public int getRowCount() {
            return acc.getBudgetList().size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        // EFFECTS: If the column index is at 0, then returns the row index.
        // Otherwise, it gives the balance of the account
        @Override
        public Object getValueAt(int row, int column) {
            if (column == 0) {
                return row;
            } else {
                return acc.getBudgetList().get(row).getBalance();
            }
        }
    }
}

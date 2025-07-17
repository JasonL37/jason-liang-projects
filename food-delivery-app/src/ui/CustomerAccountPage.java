package ui;
import controller.AppController;
import model.CustomersModel;
import ui.InputWindows.CustomerDelete;
import ui.InputWindows.CustomerInsert;
import ui.InputWindows.CustomerUpdate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerAccountPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private AppController appController;
    private JTextArea accountList;

    private JTextArea phoneInput = new JTextArea("");

    public CustomerAccountPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Customer Accounts", SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        this.accountList = new JTextArea("Empty");
        JScrollPane scrollPane = new JScrollPane(accountList);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
        String[] customerButtonNames = {"Register new customer account", "Delete customer account"};
        for (String name : customerButtonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new CustomerButtonClickListener());
            buttonPanel.add(button);
        }

        JPanel updatePanel = new JPanel(new BorderLayout());
        phoneInput.setPreferredSize(new Dimension(200, 50));
        Border border = new LineBorder(Color.BLACK, 2, true);
        phoneInput.setBorder(border);
        JButton updateButton = new JButton("Update account information by phone number");
        updateButton.addActionListener(new CustomerButtonClickListener());
        updatePanel.add(phoneInput, BorderLayout.WEST);
        updatePanel.add(updateButton, BorderLayout.CENTER);
        buttonPanel.add(updatePanel);

        String[] customerButtonNames2 = {"Find customers that ordered all of the restaurants",
                "Go back to main page"};
        for (String name : customerButtonNames2) {
            JButton button = new JButton(name);
            button.addActionListener(new CustomerButtonClickListener());
            buttonPanel.add(button);
        }



        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }

    private void loadTables() {
        Object[] tables = appController.requestTable("customer");
        this.accountList.setText("List customer accounts \n");
        this.accountList.append("Phone          Name        Address         Email       Account         Credit Card         Membership          Points      Discount \n");
        for (Object obj: tables) {
            CustomersModel customer = (CustomersModel) obj;
            accountList.append(customer.getCustomerPhone() + "  ");
            accountList.append(customer.getCustomerName() + "  ");
            accountList.append(customer.getCustomerAddress() + "  ");
            accountList.append(customer.getCustomerEmail() + "  ");
            accountList.append(customer.getCustomerAccount() + "  ");
            accountList.append(customer.getCustomerPaymentInfo() + "  ");
            accountList.append(customer.getCustomerMembership() + "  ");
            accountList.append(customer.getCustomerPoints().toString() + "  ");
            accountList.append(customer.getCustomerDiscount() + "  \n");
        }
    }
    private void loadReviews(Object[] results) {
        StringBuilder message = new StringBuilder();
        message.append("Customer phone          Customer name\n");
        boolean found = false;
        for (Object result: results) {
            Object[] row = (Object[]) result;
            for (int i = 0; i < row.length; i++) {
                message.append(row[i]).append("             ");
            }
            found = true;
            message.append("\n");
        }
        if (!found) {
            JOptionPane.showMessageDialog(CustomerAccountPage.this, "No such customer exist");
        } else {;
            JOptionPane.showMessageDialog(CustomerAccountPage.this, message);
        }
    }


    private class CustomerButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Find customers that ordered all of the restaurants")) {
                Object[] division = appController.service("customer", "division",
                        new CustomersModel("y","y","y","y","y","y","y",1,"y"));
                loadReviews(division);

            } else if (source.getText().equals("Register new customer account")) {
                CustomerInsert dialog = new CustomerInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage = (String[]) appController.service("customer", "insert", dialog.getCustomerModel());
                    JOptionPane.showMessageDialog(CustomerAccountPage.this, MainPage.checkInput(insertMessage[0]));
                    loadTables();
                } else {
                    loadTables();
                }



            } else if (source.getText().equals("Delete customer account")) {
                CustomerDelete dialog = new CustomerDelete((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] tables = appController.requestTable("customer");
                    boolean found = false;
                    for (Object obj: tables) {
                        CustomersModel customer = (CustomersModel) obj;
                        if (customer.getCustomerPhone().equals(dialog.getPhone())) {
                            String[] deleteMessage = (String[]) appController.service("customer", "delete", customer);
                            JOptionPane.showMessageDialog(CustomerAccountPage.this, deleteMessage[0]);
                            found = true;
                            loadTables();
                            break;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(CustomerAccountPage.this, "Can not find account with this phone number");
                    }
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Update account information by phone number")) {
                boolean found = false;

                Object[] tables = appController.requestTable("customer");
                for (Object obj: tables) {
                    CustomersModel customer = (CustomersModel) obj;
                    if (customer.getCustomerPhone().equals(phoneInput.getText())) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    CustomerUpdate dialog = new CustomerUpdate((Frame) SwingUtilities.getWindowAncestor(this), phoneInput.getText());
                    dialog.setVisible(true);
                    if (dialog.isValidated()) {
                        String[] updateMessage = (String[]) appController.service("customer", "update", dialog.getCustomerModel());
                        JOptionPane.showMessageDialog(CustomerAccountPage.this, MainPage.checkInput(updateMessage[0]));
                    }
                    loadTables();
                } else {
                    JOptionPane.showMessageDialog(CustomerAccountPage.this, "Can not find account with this phone number");
                }
                phoneInput.setText("");
            } else {
                JOptionPane.showMessageDialog(CustomerAccountPage.this, source.getText() + " button clicked");
            }

        }
    }
}


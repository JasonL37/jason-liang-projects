package ui;

import controller.AppController;
import model.CustomersModel;
import model.DriversModel;
import ui.InputWindows.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private AppController appController;
    private JTextArea driverList;

    private JTextArea phoneInput = new JTextArea("");
    private JTextArea findDriverInput = new JTextArea("");

    public DriverPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Driver", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        this.driverList = new JTextArea("Empty");
        JScrollPane scrollPane = new JScrollPane(driverList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        String[] buttonNames = {"Add new drivers", "Delete drivers"};
        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new DriverButtonClickListener());
            button.setAlignmentX(CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
            buttonPanel.add(button);
        }

        JPanel updatePanel = new JPanel(new BorderLayout());
        phoneInput.setPreferredSize(new Dimension(200, 50));
        Border border = new LineBorder(Color.BLACK, 2, true);
        phoneInput.setBorder(border);
        JButton updateButton = new JButton("Update driver information by phone number");
        updateButton.addActionListener(new DriverButtonClickListener());
        updatePanel.add(phoneInput, BorderLayout.WEST);
        updatePanel.add(updateButton, BorderLayout.CENTER);
        buttonPanel.add(updatePanel);

        JPanel searchDriverPanel = new JPanel(new BorderLayout());
        JButton searchDriverButton = new JButton("Search driver by specific information");
        searchDriverButton.addActionListener(new DriverButtonClickListener());
        searchDriverPanel.add(searchDriverButton, BorderLayout.CENTER);

        buttonPanel.add(searchDriverPanel);

        JButton backButton = new JButton("Go back to main page");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, backButton.getPreferredSize().height));
        backButton.addActionListener(new DriverButtonClickListener());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }

    private void loadTables() {
        Object[] tables = appController.requestTable("driver");
        this.driverList.setText("List drivers \n");
        this.driverList.append("Phone          Name        Driver Account         Vehicle \n");
        for (Object obj: tables) {
            DriversModel driver = (DriversModel) obj;
            driverList.append(driver.getDriverPhone() + "  ");
            driverList.append(driver.getDriverName() + "  ");
            driverList.append(driver.getDriverAccount() + "  ");
            driverList.append(driver.getDriverVehicle() + "  \n");

        }
    }

    private void loadResults(Object[] results) {
        StringBuilder message = new StringBuilder();
        message.append("Found Drivers").append("\n").append("Phone                   Name                Account            Vehicle\n");
        boolean found = false;
        for (Object result: results) {
            Object[] row = (Object[]) result;
            String phone = (String) row[0];
            String name = (String) row[1];
            String account = (String) row[2];
            String vehicle = (String) row[3];
            found = true;
            message.append(phone).append("   ").append(name).append("   ").append(account).append("   ").append(vehicle).append("\n");
        }

        if (found) {
            JOptionPane.showMessageDialog(DriverPage.this, message.toString());
        } else {
            JOptionPane.showMessageDialog(DriverPage.this, "No such driver exist");
        }
    }

    private class DriverButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Search driver by specific information")) {
                SelectionInput dialog = new SelectionInput((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] selectionMessage = appController.service("driver", "selection", dialog.getDriver());
                    loadResults(selectionMessage);
                }

            } else if (source.getText().equals("Add new drivers")) {
                DriverInsert dialog = new DriverInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage = (String[]) appController.service("driver", "insert", dialog.getDriver());
                    JOptionPane.showMessageDialog(DriverPage.this, MainPage.checkInput(insertMessage[0]));
                    loadTables();
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Delete drivers")) {
                DriverDelete dialog = new DriverDelete((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] tables = appController.requestTable("driver");
                    boolean found = false;
                    for (Object obj: tables) {
                        DriversModel driver = (DriversModel) obj;
                        if (driver.getDriverPhone().equals(dialog.getPhone())) {
                            String[] deleteMessage = (String[]) appController.service("driver", "delete", driver);
                            JOptionPane.showMessageDialog(DriverPage.this, deleteMessage[0]);
                            found = true;
                            loadTables();
                            break;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(DriverPage.this, "Can not find driver with this phone number");
                    }
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Update driver information by phone number")) {
                boolean found = false;

                Object[] tables = appController.requestTable("driver");
                for (Object obj: tables) {
                    DriversModel driver = (DriversModel) obj;
                    if (driver.getDriverPhone().equals(phoneInput.getText())) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    DriverUpdate dialog = new DriverUpdate((Frame) SwingUtilities.getWindowAncestor(this), phoneInput.getText());
                    dialog.setVisible(true);
                    if (dialog.isValidated()) {
                        String[] updateMessage = (String[]) appController.service("driver", "update", dialog.getDriver());
                        JOptionPane.showMessageDialog(DriverPage.this, MainPage.checkInput(updateMessage[0]));
                    }
                    loadTables();
                } else {
                    JOptionPane.showMessageDialog(DriverPage.this, "Can not find driver with this phone number");
                }
                phoneInput.setText("");
            } else {
                JOptionPane.showMessageDialog(DriverPage.this, source.getText() + " button clicked");
            }

        }
    }
}


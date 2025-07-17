package ui;

import controller.AppController;
import model.CustomersModel;
import model.DriversModel;
import model.OrdersModel;
import ui.InputWindows.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class OrderPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private AppController appController;

    private JTextArea orderList;



    public OrderPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Orders", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        this.orderList = new JTextArea("List customers order history");
        JScrollPane scrollPane = new JScrollPane(orderList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));

        JButton paymentButton = new JButton("Payment history");
        paymentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        paymentButton.addActionListener(new OrderButtonClickListener());


        paymentPanel.add(paymentButton);
        paymentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        add(paymentPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
        String[] orderButtonNames = {"Add new orders", "Delete orders","Add food to orders"};
        for (String name : orderButtonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new OrderButtonClickListener());
            buttonPanel.add(button);
        }

        JPanel findDriverPanel = new JPanel(new BorderLayout());
        JButton findDriverButton = new JButton("Find drivers with expensive orders");
        findDriverButton.addActionListener(new OrderButtonClickListener());

        findDriverPanel.add(findDriverButton, BorderLayout.CENTER);

        buttonPanel.add(findDriverPanel);

        JButton backButton = new JButton("Go back to main page");
        backButton.addActionListener(new OrderButtonClickListener());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }

    private void loadTables() {
        Object[] tables = appController.requestTable("order");
        this.orderList.setText("List order history \n");
        this.orderList.append("ID           Customer phone        Restaurant address         Driver phone         Total price         Time arrival\n");
        for (Object obj: tables) {
            OrdersModel order = (OrdersModel) obj;
            orderList.append(order.getOrderID() + "  ");
            orderList.append(order.getCustomerPhone() + "  ");
            orderList.append(order.getRestaurantAddress() + "  ");
            orderList.append(order.getDriverPhone() + "  ");
            orderList.append(order.getTotalPrice() + "  ");
            orderList.append(order.getTimeArrival() + "  \n");
        }
    }

    // The JOIN query
    private void loadFindDrivers(DriversModel[] drivers) {
        StringBuilder message = new StringBuilder();
        message.append("Driver with expensive order: \n").append("Phone                 Name               Account                Vehicle\n");
        boolean found = false;
        for (DriversModel driver: drivers) {
            found = true;
            message.append(driver.getDriverPhone()).append("     ").append(driver.getDriverName())
                    .append("   ").append(driver.getDriverAccount()).append("   ").append(driver.getDriverVehicle()).append("\n");
        }
        if (found) {
            JOptionPane.showMessageDialog(OrderPage.this, message.toString());
        } else {
            JOptionPane.showMessageDialog(OrderPage.this, "No such driver exist");
        }

    }

    private class OrderButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Payment history")) {
                cardLayout.show(cardPanel, "PaymentPage");
            } else if (source.getText().equals("Find drivers with expensive orders")) {
                DriversModel[] drivers = (DriversModel[]) appController.service("order", "join",
                        new OrdersModel(1,1.0, Timestamp.valueOf("2002-02-02 00:00:00"),"","",""));
                loadFindDrivers(drivers);
            } else if (source.getText().equals("Add new orders")) {
                OrderInsert dialog = new OrderInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage = (String[]) appController.service("order", "insert", dialog.getOrderModel());
                    JOptionPane.showMessageDialog(OrderPage.this, MainPage.checkInput(insertMessage[0]));
                    loadTables();
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Add food to orders")) {
                OrderedInsert dialog = new OrderedInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage = (String[]) appController.service("ordered", "insert", dialog.getOrderedModel());
                    JOptionPane.showMessageDialog(OrderPage.this, MainPage.checkInput(insertMessage[0]));
                    loadTables();
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Delete orders")) {
                OrderDelete dialog = new OrderDelete((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] tables = appController.requestTable("order");
                    boolean found = false;
                    for (Object obj: tables) {
                        OrdersModel order = (OrdersModel) obj;
                        if (order.getOrderID() == dialog.getID()) {
                            String[] deleteMessage = (String[]) appController.service("order", "delete", order);
                            JOptionPane.showMessageDialog(OrderPage.this, deleteMessage[0]);
                            found = true;
                            loadTables();
                            break;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(OrderPage.this, "Can not find order with this order ID");
                    }
                } else {
                    loadTables();
                }

            } else {
                JOptionPane.showMessageDialog(OrderPage.this, source.getText() + " button clicked");
            }

        }
    }
}




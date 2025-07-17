package ui;

import controller.AppController;
import model.DriversModel;
import model.PaymentBasicInfoModel;
import model.PaymentModel;
import ui.InputWindows.PaymentInsert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private AppController appController;
    private JTextArea paymentList;

    public PaymentPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Payment", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        this.paymentList = new JTextArea("");
        JScrollPane scrollPane = new JScrollPane(paymentList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        String[] paymentButtonNames = {"Make new payments", "Find the numbers of payment status",
                "Go back to orders", "Go back to main page"};
        for (String name : paymentButtonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new PaymentButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }

    private void loadTables() {
        Object[] tables = appController.requestTable("payment");
        this.paymentList.setText("List payment history \n");
        this.paymentList.append("ID           Status          Order ID         Price         Restaurant address         Driver phone\n");
        for (Object obj: tables) {
            PaymentModel payment = (PaymentModel) obj;
            paymentList.append(payment.getPaymentID() + "  ");
            paymentList.append(payment.getPaymentStatus() + "  ");
            paymentList.append(payment.getOrderID() + "  ");
            paymentList.append(payment.getPaymentPrice() + "  ");
            paymentList.append(payment.getRestaurantAddress() + "  ");
            paymentList.append(payment.getDriverPhone() + "  \n");
        }
    }

    private void loadResults(Object[] objects) {
        StringBuilder message = new StringBuilder();
        StringBuilder messageS = new StringBuilder();
        StringBuilder messageF = new StringBuilder();
        StringBuilder messageO = new StringBuilder();

        message.append("Number of payments: \n").append("Successful    Failed    Processing\n");
        boolean found = false;
        for (Object result: objects) {
            found = true;
            Object[] row = (Object[]) result;
            int number = (int) row[1];
            switch ((String) row[0]) {
                case "success": messageS.append(number).append("                    ");break;
                case "failure": messageF.append(number).append("                    ");break;
                case "ongoing": messageO.append(number);break;
            }
        }
        if (found) {
            message.append(messageS).append(messageF).append(messageO);
            JOptionPane.showMessageDialog(PaymentPage.this, message.toString());
        } else {
            JOptionPane.showMessageDialog(PaymentPage.this, "No payments can be found");
        }

    }

    private class PaymentButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Go back to orders")) {
                cardLayout.show(cardPanel, "Order & PaymentPage");
            } else if (source.getText().equals("Find the numbers of payment status")) {
                Object[] aggMessage = appController.service("paymentBasic", "aggregationGroupBy"
                        ,new PaymentBasicInfoModel(null,null,null));
                loadResults(aggMessage);
            } else if (source.getText().equals("Make new payments")) {
                PaymentInsert dialog = new PaymentInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage1 = (String[]) appController.service("paymentAdvanced", "insert", dialog.getAdvancedPaymentModel());
                    String[] insertMessage2 = (String[]) appController.service("paymentBasic", "insert", dialog.getBasicPaymentModel());
                    JOptionPane.showMessageDialog(PaymentPage.this, MainPage.checkInput(insertMessage2[0]));
                    loadTables();
                } else {
                    loadTables();
                }
            } else {
                JOptionPane.showMessageDialog(PaymentPage.this, source.getText() + " button clicked");
            }

        }
    }
}

package ui.InputWindows;

import model.CustomersModel;
import model.PaymentAdvancedInfoModel;
import model.PaymentBasicInfoModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentInsert extends JDialog {    JTextField name = new JTextField(20);
    JTextField paymentID= new JTextField(20);
    JTextField paymentStatus= new JTextField(20);
    JTextField orderID = new JTextField(20);
    JTextField paymentPrice= new JTextField(20);
    JTextField restaurantAddress = new JTextField(20);
    JTextField driverPhone = new JTextField(20);

    private boolean validated = false;

    JTextField[] textFields = {paymentID,paymentStatus,orderID,paymentPrice,restaurantAddress, driverPhone};


    public PaymentInsert(Frame parent) {
        super(parent, "Make new payments", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Make new payments", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Payment ID:", "*Payment Status:", "*Order ID:", "*Price:", "*Restaurant address:", "*Driver phone:"};
        for (String attr: attrNames) {
            attrPanel.add(new JLabel(attr));
        }

        JPanel inputPanel = new JPanel(new GridLayout(0,1));
        for (JTextField tf: textFields) {
            inputPanel.add(tf);
        }
        add(attrPanel, BorderLayout.WEST);
        add(inputPanel,BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        registerButton.addActionListener(new InsertButtonClickListener());
        cancelButton.addActionListener(new InsertButtonClickListener());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

    }

    public PaymentBasicInfoModel getBasicPaymentModel() {
        return new PaymentBasicInfoModel(Integer.parseInt(paymentID.getText()),paymentStatus.getText(),Integer.parseInt(orderID.getText()));
    }

    public PaymentAdvancedInfoModel getAdvancedPaymentModel() {
        return new PaymentAdvancedInfoModel(Integer.parseInt(orderID.getText()),Double.parseDouble(paymentPrice.getText()),
                restaurantAddress.getText(),driverPhone.getText());
    }

    public boolean isValidated() {
        return this.validated;
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Add")) {
                if (paymentID.getText().isEmpty() || paymentStatus.getText().isEmpty() || orderID.getText().isEmpty() || paymentPrice.getText().isEmpty() ||
                        restaurantAddress.getText().isEmpty() || driverPhone.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(PaymentInsert.this, "Some values can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Integer.parseInt(paymentID.getText());
                        Integer.parseInt(orderID.getText());
                        Double.parseDouble(paymentPrice.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(PaymentInsert.this, "Payment ID, order ID, and price must be numbers");
                        success = false;
                    }

                    if (success) {
                        validated = true;
                        dispose();
                    }
                }
            } else {
                validated = false;
                dispose();
            }

        }
    }
}


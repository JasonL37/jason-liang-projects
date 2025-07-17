package ui.InputWindows;

import model.CustomersModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerUpdate extends JDialog {
    JTextField name = new JTextField(20);
    JTextField address= new JTextField(20);
    String phone;
    JTextField email = new JTextField(20);
    JTextField account= new JTextField(20);
    JTextField payment = new JTextField(20);
    JTextField membership = new JTextField(20);
    JTextField points= new JTextField(20);
    JTextField discount= new JTextField(20);

    boolean validated = false;

    JTextField[] textFields = {name,address,email,account, payment,membership,points,discount};
    public CustomerUpdate(Frame parent, String phone) {
        super(parent, "Update account information", true);
        this.phone = phone;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Update account (" + phone + ") information", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Name:", "*Address:", "*Email:", "*Customer account:", "*Credit card:", "Membership:",
                "*Points:", "Discount:"};
        for (String attr: attrNames) {
            attrPanel.add(new JLabel(attr));
        }

        JPanel inputPanel = new JPanel(new GridLayout(0,1));
        for (JTextField tf: textFields) {
            inputPanel.add(tf);
        }

        add(attrPanel, BorderLayout.WEST);
        add(inputPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");

        updateButton.addActionListener(new RegisterButtonClickListener());
        cancelButton.addActionListener(new RegisterButtonClickListener());

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);



    }

    public boolean isValidated() {
        return validated;
    }

    public CustomersModel getCustomerModel() {
        return new CustomersModel(phone,name.getText(),address.getText(),email.getText(),account.getText()
                , payment.getText(),membership.getText(), Integer.parseInt(points.getText()),discount.getText());
    }

    private class RegisterButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Update")) {
                if (name.getText().isEmpty() || address.getText().isEmpty() || email.getText().isEmpty() ||
                        account.getText().isEmpty() || payment.getText().isEmpty() || points.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(CustomerUpdate.this, "Some values can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Integer.parseInt(points.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(CustomerUpdate.this, "Points must be integer");
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

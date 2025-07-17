package ui.InputWindows;

import model.CustomersModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerInsert extends JDialog {
    JTextField name = new JTextField(20);
    JTextField address= new JTextField(20);
    JTextField phone= new JTextField(20);
    JTextField email = new JTextField(20);
    JTextField account= new JTextField(20);
    JTextField payment = new JTextField(20);
    JTextField membership = new JTextField(20);
    JTextField points= new JTextField(20);
    JTextField discount= new JTextField(20);

    private boolean validated = false;

    JTextField[] textFields = {phone,name,address,email,account, payment,membership,points,discount};

    public CustomerInsert(Frame parent) {
        super(parent, "Register new customer", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Register new customer", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Phone:","*Name:", "*Address:",  "*Email:", "*Customer account:", "*Credit card:", "Membership:",
                "*Points:", "Discount:"};
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
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        registerButton.addActionListener(new InsertButtonClickListener());
        cancelButton.addActionListener(new InsertButtonClickListener());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);



    }

    public CustomersModel getCustomerModel() {
        return new CustomersModel(phone.getText(),name.getText(),address.getText(),email.getText(),account.getText()
                    ,payment.getText(),membership.getText(), Integer.parseInt(points.getText()),discount.getText());
    }

    public boolean isValidated() {
        return this.validated;
    }


    private class InsertButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            boolean success = true;
            if (source.getText().equals("Register")) {
                if (phone.getText().isEmpty() || name.getText().isEmpty() || address.getText().isEmpty() || email.getText().isEmpty() ||
                        account.getText().isEmpty() || payment.getText().isEmpty() || points.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(CustomerInsert.this, "Some values can not be empty");
                } else {
                    try {
                        Integer.parseInt(points.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(CustomerInsert.this, "Points must be integer");
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


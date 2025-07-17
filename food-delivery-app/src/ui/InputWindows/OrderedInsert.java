package ui.InputWindows;

import model.OrderedModel;
import model.OrdersModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class OrderedInsert extends JDialog {
    JTextField foodName = new JTextField(20);
    JTextField orderNumber = new JTextField(20);
    JTextField quantity = new JTextField(20);

    private boolean validated = false;

    JTextField[] textFields = {foodName, orderNumber, quantity};

    public OrderedInsert(Frame parent) {
        super(parent, "Add food to orders", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Add food to orders", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Food name:","*Order ID:","*Quantity:"};
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

    public OrderedModel getOrderedModel() {
        return new OrderedModel(foodName.getText(), Integer.parseInt(orderNumber.getText()),Integer.parseInt(quantity.getText()));
    }

    public boolean isValidated() {
        return this.validated;
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Add")) {
                if (foodName.getText().isEmpty() || orderNumber.getText().isEmpty() || quantity.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(OrderedInsert.this, "Some values can not be empty");
                } else {
                    boolean success = true;

                    try {
                        Integer.parseInt(orderNumber.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(OrderedInsert.this, "Order ID must be integer");
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


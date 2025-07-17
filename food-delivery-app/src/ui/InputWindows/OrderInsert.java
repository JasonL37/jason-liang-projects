package ui.InputWindows;

import model.CustomersModel;
import model.OrdersModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class OrderInsert extends JDialog {
    JTextField ID = new JTextField(20);
    JTextField cusPhone= new JTextField(20);
    JTextField restAddress= new JTextField(20);
    JTextField driverPhone = new JTextField(20);
    JTextField price= new JTextField(20);
    JTextField time = new JTextField(20);

    private boolean validated = false;

    JTextField[] textFields = {ID,price,time,cusPhone,restAddress, driverPhone};

    public OrderInsert(Frame parent) {
        super(parent, "Add new orders", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Add new orders", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Order ID","*Total price:","*Time of arrival:","*Customer phone","*Restaurant address","*Driver phone" };
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

    public OrdersModel getOrderModel() {
        return new OrdersModel(Integer.parseInt(ID.getText()), Double.parseDouble(price.getText()), Timestamp.valueOf(time.getText()),
                cusPhone.getText(),restAddress.getText(),driverPhone.getText());
    }

    public boolean isValidated() {
        return this.validated;
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            boolean success = true;
            if (source.getText().equals("Add")) {
                if (ID.getText().isEmpty() || price.getText().isEmpty() || time.getText().isEmpty() || cusPhone.getText().isEmpty() ||
                        driverPhone.getText().isEmpty() || restAddress.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(OrderInsert.this, "Some values can not be empty");
                } else {
                    try {
                        Timestamp.valueOf(time.getText());
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(OrderInsert.this, "Incorrect time stamp format: must be yyyy-mm-dd hh:mm:ss");
                        success =false;
                    }
                    try {
                        Integer.parseInt(ID.getText());
                        Double.parseDouble(price.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(OrderInsert.this, "Points and price must be a number");
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


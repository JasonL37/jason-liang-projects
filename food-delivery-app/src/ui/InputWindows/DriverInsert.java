package ui.InputWindows;

import model.DriversModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverInsert extends JDialog {
    JTextField name = new JTextField(20);
    JTextField phone= new JTextField(20);
    JTextField account = new JTextField(20);
    JTextField vehicle = new JTextField(20);


    private boolean validated = false;

    JTextField[] textFields = {phone,name,account, vehicle};
    public DriverInsert(Frame parent) {
        super(parent, "Add new drivers", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Add new drivers", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Phone:", "*Name:", "*Driver account:", "*Vehicle:"};
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

    public DriversModel getDriver() {
        return new DriversModel(phone.getText(),name.getText(),account.getText(),vehicle.getText());
    }

    public boolean isValidated() {
        return validated;
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Register")) {
                if (phone.getText().isEmpty() || name.getText().isEmpty() || account.getText().isEmpty() || vehicle.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(DriverInsert.this, "Some values can not be empty");
                } else {
                    validated = true;
                    dispose();
                }
            } else {
                validated = false;
                dispose();
            }

        }
    }
}


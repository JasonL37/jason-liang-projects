package ui.InputWindows;

import model.DriversModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverUpdate extends JDialog {

    JTextField name = new JTextField(20);
    String phone;
    JTextField account = new JTextField(20);
    JTextField vehicle = new JTextField(20);


    private boolean validated = false;

    JTextField[] textFields = {name,account, vehicle};

    public DriverUpdate(Frame parent, String phone) {
        super(parent, "Update driver information", true);
        this.phone = phone;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Update driver information", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Name:", "*Driver account:", "*Vehicle:"};
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
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");

        updateButton.addActionListener(new RegisterButtonClickListener());
        cancelButton.addActionListener(new RegisterButtonClickListener());

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);



    }
    public DriversModel getDriver() {
        return new DriversModel(phone,name.getText(),account.getText(),vehicle.getText());
    }

    public boolean isValidated() {
        return validated;
    }
    private class RegisterButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Update")) {
                if (name.getText().isEmpty() || account.getText().isEmpty() || vehicle.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(DriverUpdate.this, "Some values can not be empty");
                } else {
                    validated = true;
                    dispose();
                }
            } else {
                validated = false;
                dispose();
            }

        }    }
}

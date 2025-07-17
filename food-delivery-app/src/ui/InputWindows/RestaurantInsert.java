package ui.InputWindows;

import model.CustomersModel;
import model.RestaurantsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestaurantInsert extends JDialog {
    JTextField address = new JTextField(20);
    JTextField name= new JTextField(20);
    JTextField openHours= new JTextField(20);
    JTextField account = new JTextField(20);
    private boolean validated = false;
    JTextField[] textFields = {address,name,openHours,account};

    public RestaurantInsert(Frame parent) {
        super(parent, "Register new restaurant", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Register new restaurant", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Address:", "*Name:", "*Open Hours:", "*Restaurant account:"};
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
    public RestaurantsModel getRestModel() {
        return new RestaurantsModel(address.getText(),name.getText(),openHours.getText(),account.getText());
    }

    public boolean isValidated() {
        return this.validated;
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Register")) {
                if (address.getText().isEmpty() || name.getText().isEmpty() || openHours.getText().isEmpty() || account.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(RestaurantInsert.this, "Some values can not be empty");
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

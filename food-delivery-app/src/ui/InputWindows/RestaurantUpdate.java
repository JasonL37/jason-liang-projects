package ui.InputWindows;

import model.RestaurantsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestaurantUpdate extends JDialog {
    String address;
    JTextField name= new JTextField(20);
    JTextField openHours= new JTextField(20);
    JTextField account = new JTextField(20);
    private boolean validated = false;
    JTextField[] textFields = {name,openHours,account};

    public RestaurantUpdate(Frame parent, String address) {
        super(parent, "Update restaurant information", true);
        this.address = address;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Update restaurant information", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Name:", "*Open Hours:", "*Restaurant account:"};
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
    public RestaurantsModel getRestModel() {
        return new RestaurantsModel(address,name.getText(),openHours.getText(),account.getText());
    }

    public boolean isValidated() {
        return this.validated;
    }

    private class RegisterButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Update")) {
                if (name.getText().isEmpty() || openHours.getText().isEmpty() || account.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(RestaurantUpdate.this, "Some values can not be empty");
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


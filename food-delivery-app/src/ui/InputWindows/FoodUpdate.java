package ui.InputWindows;

import model.FoodAdvancedInfoModel;
import model.FoodBasicInfoModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoodUpdate extends JDialog {
    String name;
    JTextField price= new JTextField(20);
    JTextField vegan= new JTextField(20);
    private boolean validated = false;

    JTextField[] textFields = {price};

    public FoodUpdate(Frame parent, String name) {
        super(parent, "Update food information", true);
        this.name = name;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Update food information", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Price:"};
        for (String attr: attrNames) {
            attrPanel.add(new JLabel(attr));
        }

        JPanel inputPanel = new JPanel(new GridLayout(0,1));
        for (JTextField tf: textFields) {
            inputPanel.add(tf);
        }
        add(attrPanel, BorderLayout.WEST);
        add(inputPanel,BorderLayout.EAST);


        add(inputPanel, BorderLayout.CENTER);

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
        return this.validated;
    }



    public Double getPrice() {
        return Double.parseDouble(price.getText());
    }


    private class RegisterButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Update")) {
                if (price.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(FoodUpdate.this, "Some values can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Double.parseDouble(price.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(FoodUpdate.this, "Food price must be number");
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

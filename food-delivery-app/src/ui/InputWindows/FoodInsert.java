package ui.InputWindows;

import model.FoodAdvancedInfoModel;
import model.FoodBasicInfoModel;
import model.MenuAdvancedInfoModel;
import model.MenuBasicInfoModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class FoodInsert extends JDialog {
    JTextField name = new JTextField(20);
    JTextField price= new JTextField(20);
    JTextField type= new JTextField(20);
    JTextField vegan= new JTextField(20);
    private boolean validated = false;

    JTextField[] textFields = {name,price,type,vegan};

    public FoodInsert(Frame parent) {
        super(parent, "Add food to menu", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Add food to menu", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Name:", "*Price:", "*Type:", "*Is it vegan:"};
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
    public boolean isValidated() {
        return this.validated;
    }

    public FoodAdvancedInfoModel getFoodAdv() {
        return new FoodAdvancedInfoModel(type.getText(), vegan.getText());
    }
    public FoodBasicInfoModel getFodBasic() {
        return new FoodBasicInfoModel(name.getText(),Double.parseDouble(price.getText()), type.getText());
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Add")) {
                if (name.getText().isEmpty() || price.getText().isEmpty()|| type.getText().isEmpty()|| vegan.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(FoodInsert.this, "Some values can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Double.parseDouble(price.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(FoodInsert.this, "Food price must be number");
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


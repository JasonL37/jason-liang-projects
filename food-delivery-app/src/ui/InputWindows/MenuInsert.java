package ui.InputWindows;

import model.MenuAdvancedInfoModel;
import model.MenuBasicInfoModel;
import model.MenuModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInsert extends JDialog {
    JTextField ID = new JTextField(20);
    JTextField name= new JTextField(20);
    JTextField cuisine= new JTextField(20);
    private boolean validated = false;

    JTextField[] textFields = {ID,name,cuisine};

    public MenuInsert(Frame parent) {
        super(parent, "Add new menus to restaurants", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Add new menus to restaurants", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Menu ID", "*Name:", "Cuisine:"};
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

    public MenuAdvancedInfoModel getMenuAdv() {
        return new MenuAdvancedInfoModel(name.getText(), cuisine.getText());
    }
    public MenuBasicInfoModel getMenuBasic() {
        return new MenuBasicInfoModel(Integer.parseInt(ID.getText()), name.getText());
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Add")) {
                if (ID.getText().isEmpty() || name.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(MenuInsert.this, "Some values can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Integer.parseInt(ID.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(MenuInsert.this, "ID must be integer");
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

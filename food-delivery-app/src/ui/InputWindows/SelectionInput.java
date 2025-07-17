package ui.InputWindows;

import model.DriversModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SelectionInput extends JDialog{
    JTextArea input = new JTextArea("Example: driver_name == \"John\" && driver_phone == \"111\" || driver_vehicle == \"Tesla Model Y\"");

    private boolean validated = false;

    public SelectionInput(Frame parent) {
        super(parent, "Pick attributes for search", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Pick driver_phone, driver_name, driver_account, and driver_vehicle and input string to search", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        add(input,BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Search");
        JButton cancelButton = new JButton("Cancel");

        registerButton.addActionListener(new SelectionListener());
        cancelButton.addActionListener(new SelectionListener());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);



    }

    public DriversModel getDriver() {
        String search = input.getText();
        search = search.replace("Example:", " ");
        search = search.replace("&&","AND");
        search = search.replace("||", "OR");
        search = search.replace("\"", "'");
        search = search.replace("==", "=");
        return new DriversModel("",search,"","");
    }

    public boolean isValidated() {
        return validated;
    }

    private class SelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Search")) {
                if (input.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(SelectionInput.this, "Input can not be empty");
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

package ui.InputWindows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverDelete extends JDialog {
    JTextField phone = new JTextField(20);
    Boolean validated = false;
    public DriverDelete(Frame parent) {
        super(parent, "Delete driver", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Delete driver", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("*Phone:"));
        inputPanel.add(phone);
        inputPanel.add(new JLabel(" "));
        inputPanel.add(new JLabel(" "));
        inputPanel.add(new JLabel(" "));
        inputPanel.add(new JLabel(" "));
        inputPanel.add(new JLabel(" "));
        inputPanel.add(new JLabel(" "));

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");
        deleteButton.addActionListener(new DeleteButtonClickListener());
        cancelButton.addActionListener(new DeleteButtonClickListener());

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getPhone() {
        return phone.getText();
    }

    public Boolean isValidated() {
        return validated;
    }

    private class DeleteButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Delete")) {
                if (phone.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(DriverDelete.this, "Phone number can not be empty");
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

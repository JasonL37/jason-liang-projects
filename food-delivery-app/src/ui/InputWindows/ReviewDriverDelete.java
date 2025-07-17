package ui.InputWindows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class ReviewDriverDelete extends JDialog {
    JTextField phone = new JTextField(20);
    JTextField timeStamp = new JTextField(20);
    Boolean validated = false;
    public ReviewDriverDelete(Frame parent) {
        super(parent, "Delete reviews", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Delete reviews", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("*Customer phone:"));
        inputPanel.add(phone);
        inputPanel.add(new JLabel("*Time stamp "));
        inputPanel.add(timeStamp);
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

    public Timestamp getTimeStamp() {
        return Timestamp.valueOf(timeStamp.getText());
    }

    public Boolean isValidated() {
        return validated;
    }

    private class DeleteButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Delete")) {
                if (phone.getText().isEmpty()|| timeStamp.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(ReviewDriverDelete.this, "Can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Timestamp.valueOf(timeStamp.getText());
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(ReviewDriverDelete.this, "Incorrect time stamp format: must be yyyy-mm-dd hh:mm:ss");
                        success =false;
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



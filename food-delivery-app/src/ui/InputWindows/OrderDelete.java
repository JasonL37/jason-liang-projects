package ui.InputWindows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderDelete extends JDialog {
    JTextField ID = new JTextField(20);
    Boolean validated = false;
    public OrderDelete(Frame parent) {
        super(parent, "Delete orders", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Delete orders", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("*Order ID:"));
        inputPanel.add(ID);
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
    public int getID() {
        return Integer.parseInt(ID.getText());
    }

    public Boolean isValidated() {
        return validated;
    }
    private class DeleteButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Delete")) {
                if (ID.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(OrderDelete.this, "Order ID can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Integer.parseInt(ID.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(OrderDelete.this, "Order ID must be a integer");
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


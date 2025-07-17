package ui;
import controller.AppController;
import ui.InputWindows.ProjectionInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectionPage extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextArea textArea;
    private JComboBox<String> comboBox;
    private AppController appController;

    public ProjectionPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Information Page", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        comboBox = new JComboBox<>(new String[]{"customer", "restaurant",
                "menuAdvanced", "menuBasic", "foodAdvanced", "foodBasic", "order", "driver",
                "reviewAdvanced", "reviewBasic", "foodReview", "driverReview", "paymentAdvanced"
                , "paymentBasic", "own", "contain", "ordered"});
        comboBox.addActionListener(new ComboBoxActionListener());
        add(comboBox, BorderLayout.NORTH);

        textArea = new JTextArea("Pick one table from drop-down list to see its data");
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton checkButton = new JButton("Filter");
        checkButton.addActionListener(new CheckButtonActionListener());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cardLayout.show(cardPanel, "MainPanel"));

        buttonPanel.add(checkButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class ComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateTextArea();
        }
    }

    private void loadReviews(Object[] results) {
        StringBuilder message = new StringBuilder();
        boolean found = false;
        for (Object result: results) {
            Object[] row = (Object[]) result;
            for (int i = 0; i < row.length; i++) {
                message.append( row[i]).append("    ");
            }
            found = true;
            message.append("\n");
        }
        if (!found) {
            JOptionPane.showMessageDialog(ProjectionPage.this, "No result");
        } else {;
            textArea.append(String.valueOf(message));
        }
    }

    private class CheckButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Filter")) {
                ProjectionInput dialog = new ProjectionInput((Frame) SwingUtilities.getWindowAncestor(ProjectionPage.this), (String) comboBox.getSelectedItem());
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] projection =  appController.service((String) comboBox.getSelectedItem(), "projection", dialog.selectModel());
                    updateTextArea();
                    textArea.append("\nFiltered data: \n");
                    loadReviews(projection);
                }
            }


        }
    }
    private void loadTables() {
        ProjectionInput dialog = new ProjectionInput((Frame) SwingUtilities.getWindowAncestor(ProjectionPage.this), (String) comboBox.getSelectedItem());
        Object[] projection =  appController.service((String) comboBox.getSelectedItem(), "projection", dialog.loadTables());
        textArea.append("Original data: \n");
        loadReviews(projection);
    }

    private void updateTextArea() {
        String selectedOption = (String) comboBox.getSelectedItem();

        switch (selectedOption) {
            case "customer" : textArea.setText("Search results from Customer\n");loadTables(); break;
            case "restaurant" : textArea.setText("Search results from Restaurant\n");loadTables();break;
            case "menuAdvanced" : textArea.setText("Search results from Menu advanced information\n");loadTables();break;
            case "menuBasic" : textArea.setText("Search results from Menu basic information\n");loadTables();break;
            case "foodAdvanced" : textArea.setText("Search results from Food advanced information\n");loadTables();break;
            case "foodBasic" : textArea.setText("Search results from Food basic information\n");loadTables();break;
            case "order" : textArea.setText("Search results from Orders\n");loadTables();break;
            case "driver" : textArea.setText("Search results from Driver\n");loadTables();break;
            case "reviewAdvanced" : textArea.setText("Search results from Review advanced information\n");loadTables();break;
            case "reviewBasic" : textArea.setText("Search results from Review basic information\n");loadTables();break;
            case "foodReview" : textArea.setText("Search results from Food review\n");loadTables();break;
            case "driverReview" : textArea.setText("Search results from Driver review\n");loadTables();break;
            case "paymentAdvanced" : textArea.setText("Search results from Payment advanced information\n");loadTables();break;
            case "paymentBasic" : textArea.setText("Search results from Payment basic information\n");loadTables();break;
            case "own" : textArea.setText("Search results from Own relation\n");loadTables();break;
            case "contain" : textArea.setText("Search results from Contain relation\n");loadTables();break;
            case "ordered" : textArea.setText("Search results from Ordered relation\n");loadTables();break;
        }

    }

}


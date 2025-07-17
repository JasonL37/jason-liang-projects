package ui;

import controller.AppController;
import model.*;
import ui.InputWindows.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoodPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextArea foodList = new JTextArea("");
    private AppController appController;
    private JTextArea nameInput = new JTextArea("");

    public FoodPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Foods", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        this.foodList = new JTextArea("List menus and their foods");
        JScrollPane scrollPane = new JScrollPane(foodList);
        add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new GridLayout(6, 1));
        String[] foodButtonNames = {"Add food to menu", "Delete food from menu"};
        for (String name : foodButtonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new FoodButtonClickListener());
            buttonPanel.add(button);
        }

        JPanel updatePanel = new JPanel(new BorderLayout());
        nameInput.setPreferredSize(new Dimension(200, 50));
        Border border = new LineBorder(Color.BLACK, 2, true);
        nameInput.setBorder(border);
        JButton updateButton = new JButton("Update food price");
        updateButton.addActionListener(new FoodButtonClickListener());
        updatePanel.add(nameInput, BorderLayout.WEST);
        updatePanel.add(updateButton, BorderLayout.CENTER);
        buttonPanel.add(updatePanel);

        String[] foodButtonNames2 = { "Find the cheapest price for popular food types", "Go back to menus",
                "Go back to main page"};
        for (String name : foodButtonNames2) {
            JButton button = new JButton(name);
            button.addActionListener(new FoodButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }

    private void loadTables() {
        Object[] tables = appController.requestTable("food");
        this.foodList.setText("List foods \n");
        this.foodList.append("Name          Price        Type        Vegan diet \n");
        for (Object obj: tables) {
            FoodModel food = (FoodModel) obj;
            foodList.append(food.getFoodName() + "  ");
            foodList.append(food.getFoodPrice() + "  ");
            foodList.append(food.getFoodType() + "  ");
            foodList.append(food.getFoodHasVeganDiet() + "  \n");
        }
    }

    private void loadResults(Object[] results) {
        StringBuilder message = new StringBuilder();
        message.append("Find the cheapest price for popular food types\n");
        message.append("Food types       Cheapest price\n");
        boolean found = false;
        for (Object result: results) {
            Object[] row = (Object[]) result;
            String type = (String) row[0];
            double price = (double) row[1];
            found = true;
            message.append(type).append("                       ").append(price).append("\n");

        }

        if (!found) {
            JOptionPane.showMessageDialog(FoodPage.this, "There are no food");
        } else {
            JOptionPane.showMessageDialog(FoodPage.this, message.toString());
        }
    }

    private class FoodButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Go back to menus")) {
                cardLayout.show(cardPanel, "MenuPage");
            } else if (source.getText().equals("Find the cheapest price for popular food types")) {
                Object[] nestedAggResult = appController.service("foodBasic", "nestedAggregation",
                        new FoodBasicInfoModel("", (double) 0,""));
                loadResults(nestedAggResult);
            } else if (source.getText().equals("Add food to menu")) {
                FoodInsert dialog = new FoodInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage1 = (String[]) appController.service("foodAdvanced", "insert", dialog.getFoodAdv());
                    String[] insertMessage2 = (String[]) appController.service("foodBasic", "insert", dialog.getFodBasic());
                    JOptionPane.showMessageDialog(FoodPage.this, MainPage.checkInput(insertMessage2[0]));
                    loadTables();
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Delete food from menu")) {
                FoodDelete dialog = new FoodDelete((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] tables = appController.requestTable("food");
                    boolean found = false;
                    for (Object obj: tables) {
                        FoodModel food = (FoodModel) obj;
                        if (food.getFoodName().equals(dialog.getfName())) {
                            String[] deleteMessage1 = (String[]) appController.service("foodBasic", "delete",
                                    new FoodBasicInfoModel(food.getFoodName(),food.getFoodPrice(), food.getFoodType()));
                            String[] deleteMessage2 = (String[]) appController.service("foodAdvanced", "delete",
                                    new FoodAdvancedInfoModel(food.getFoodType(),food.getFoodHasVeganDiet()));
                            JOptionPane.showMessageDialog(FoodPage.this, deleteMessage2[0]);
                            found = true;
                            loadTables();
                            break;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(FoodPage.this, "Can not find this food");
                    }
                }
            } else if (source.getText().equals("Update food price")) {
                boolean found = false;
                String type = "";
                Object[] tables = appController.requestTable("food");
                for (Object obj: tables) {
                    FoodModel food = (FoodModel) obj;
                    if (food.getFoodName().equals(nameInput.getText())) {
                        found = true;
                        type = food.getFoodType();
                        break;
                    }
                }
                if (found) {
                    FoodUpdate dialog = new FoodUpdate((Frame) SwingUtilities.getWindowAncestor(this), nameInput.getText());
                    dialog.setVisible(true);
                    if (dialog.isValidated()) {
                        String[] updateMessage2 = (String[]) appController.service("foodBasic", "update", new FoodBasicInfoModel(nameInput.getText(),dialog.getPrice(),type));
                        JOptionPane.showMessageDialog(FoodPage.this, updateMessage2);
                    }
                    loadTables();
                } else {
                    JOptionPane.showMessageDialog(FoodPage.this, "Can not find this food");
                }
                nameInput.setText("");
            } else {
                JOptionPane.showMessageDialog(FoodPage.this, source.getText() + " button clicked");
            }

        }
    }

}


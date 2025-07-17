package ui;

import controller.AppController;
import model.CustomersModel;
import model.MenuAdvancedInfoModel;
import model.MenuModel;
import ui.InputWindows.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MenuPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private AppController appController;
    private JTextArea menuList;

    public MenuPage(CardLayout cardLayout, JPanel cardPanel,AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Menu", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        this.menuList = new JTextArea("List restaurants and their menus");
        JScrollPane scrollPane = new JScrollPane(menuList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel foodPanel = new JPanel();
        foodPanel.setLayout(new BoxLayout(foodPanel, BoxLayout.Y_AXIS));
        JButton foodButton = new JButton("Check food list");
        foodButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        foodButton.addActionListener(new MenuButtonClickListener());


        foodPanel.add(foodButton);
        foodPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        add(foodPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        String[] menuButtonNames = {"Add new menus to restaurants", "Delete menus from restaurant",
                "Go back to restaurants", "Go back to main page"};
        for (String name : menuButtonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new MenuButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }
    private void loadTables() {
        Object[] tables = appController.requestTable("menu");
        this.menuList.setText("List menus \n");
        this.menuList.append("Menu ID           Name            Cuisine \n");
        for (Object obj: tables) {
            MenuModel menu = (MenuModel) obj;
            menuList.append(menu.getMenuID() + "        ");
            menuList.append(menu.getMenuName() + "          ");
            menuList.append(menu.getMenuCuisine() + "  \n");
        }
    }

    private class MenuButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Go back to restaurants")) {
                cardLayout.show(cardPanel, "RestaurantPage");
            } else if (source.getText().equals("Check food list")) {
                cardLayout.show(cardPanel, "FoodPage");
            } else if (source.getText().equals("Add new menus to restaurants")) {
                MenuInsert dialog = new MenuInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage1 = (String[]) appController.service("menuAdvanced", "insert", dialog.getMenuAdv());
                    String[] insertMessage2 = (String[]) appController.service("menuBasic", "insert", dialog.getMenuBasic());
                    JOptionPane.showMessageDialog(MenuPage.this, MainPage.checkInput(insertMessage2[0]));
                    loadTables();
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Delete menus from restaurant")) {
                MenuDelete dialog = new MenuDelete((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] tables = appController.requestTable("menu");
                    boolean found = false;
                    for (Object obj: tables) {
                        MenuModel menu = (MenuModel) obj;
                        if (Objects.equals(menu.getMenuID(), dialog.getID())) {
                            String[] deleteMessage = (String[]) appController.service("menuAdvanced", "delete",
                                    new MenuAdvancedInfoModel(menu.getMenuName(),menu.getMenuCuisine()));
                            JOptionPane.showMessageDialog(MenuPage.this, deleteMessage[0]);
                            found = true;
                            loadTables();
                            break;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(MenuPage.this, "Can not find menu");
                    }
                } else {
                    loadTables();
                }

            } else {
                JOptionPane.showMessageDialog(MenuPage.this, source.getText() + " button clicked");
            }

        }
    }
}


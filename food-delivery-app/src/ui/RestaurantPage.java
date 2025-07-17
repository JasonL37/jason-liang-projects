package ui;

import controller.AppController;
import model.CustomersModel;
import model.RestaurantsModel;
import ui.InputWindows.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestaurantPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JTextArea addressInput = new JTextArea("");
    private JTextArea restaurantList = new JTextArea("");
    private AppController appController;

    public RestaurantPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Restaurants", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        this.restaurantList = new JTextArea("List all restaurants");
        JScrollPane scrollPane = new JScrollPane(restaurantList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JButton menuButton = new JButton("Show Restaurant's menu");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.addActionListener(new RestaurantButtonClickListener());

        Border border = new LineBorder(Color.BLACK, 2, true);



        menuPanel.add(menuButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        add(menuPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        String[] buttonNames = {"Register new restaurant", "Delete restaurant"};
        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new RestaurantButtonClickListener());
            buttonPanel.add(button);
        }

        JPanel updatePanel = new JPanel(new BorderLayout());
        addressInput.setPreferredSize(new Dimension(200, 50));
        addressInput.setBorder(border);
        JButton updateButton = new JButton("Update restaurant information by address");
        updateButton.addActionListener(new RestaurantButtonClickListener());
        updatePanel.add(addressInput, BorderLayout.WEST);
        updatePanel.add(updateButton, BorderLayout.CENTER);
        buttonPanel.add(updatePanel);

        JButton button = new JButton("Go back to main page");
        button.addActionListener(new RestaurantButtonClickListener());
        buttonPanel.add(button);


        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }

    private void loadTables() {
        Object[] tables = appController.requestTable("restaurant");
        this.restaurantList.setText("List restaurants \n");
        this.restaurantList.append("Address          Name        Open Hours        Account \n");
        for (Object obj: tables) {
            RestaurantsModel rest = (RestaurantsModel) obj;
            restaurantList.append(rest.getRestaurantAddress() + "  ");
            restaurantList.append(rest.getRestaurantName() + "  ");
            restaurantList.append(rest.getRestaurantOpenHours() + "  ");
            restaurantList.append(rest.getRestaurantAccount() + "  \n");
        }
    }

    private class RestaurantButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Show Restaurant's menu")) {
                cardLayout.show(cardPanel, "MenuPage");
            } else if (source.getText().equals("Register new restaurant")) {
                RestaurantInsert dialog = new RestaurantInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage = (String[]) appController.service("restaurant", "insert", dialog.getRestModel());
                    JOptionPane.showMessageDialog(RestaurantPage.this, MainPage.checkInput(insertMessage[0]));
                    loadTables();
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Delete restaurant")) {
                RestaurantDelete dialog = new RestaurantDelete((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] tables = appController.requestTable("restaurant");
                    boolean found = false;
                    for (Object obj: tables) {
                        RestaurantsModel restaurant = (RestaurantsModel) obj;
                        if (restaurant.getRestaurantAddress().equals(dialog.getAddress())) {
                            String[] deleteMessage = (String[]) appController.service("restaurant", "delete", restaurant);
                            JOptionPane.showMessageDialog(RestaurantPage.this, deleteMessage[0]);
                            found = true;
                            loadTables();
                            break;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(RestaurantPage.this, "Can not find restaurant with this address");
                    }
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Update restaurant information by address")) {
                boolean found = false;

                Object[] tables = appController.requestTable("restaurant");
                for (Object obj: tables) {
                    RestaurantsModel restaurant = (RestaurantsModel) obj;
                    if (restaurant.getRestaurantAddress().equals(addressInput.getText())) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    RestaurantUpdate dialog = new RestaurantUpdate((Frame) SwingUtilities.getWindowAncestor(this), addressInput.getText());
                    dialog.setVisible(true);
                    if (dialog.isValidated()) {
                        String[] updateMessage = (String[]) appController.service("restaurant", "update", dialog.getRestModel());
                        JOptionPane.showMessageDialog(RestaurantPage.this, updateMessage[0]);
                    }
                    loadTables();
                } else {
                    JOptionPane.showMessageDialog(RestaurantPage.this, "Can not find restaurant with this address");
                }
                addressInput.setText("");
            } else {
                JOptionPane.showMessageDialog(RestaurantPage.this, source.getText() + " button clicked");
            }

        }
    }
}


package ui;

import controller.AppController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainPage extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private AppController appController;

    public MainPage(AppController appController) {
        this.appController = appController;
        setTitle("Food delivery system");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);


        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeImage = new JLabel(new ImageIcon("src/ui/Image/welcomeImage.jpg"));
        mainPanel.add(welcomeImage, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        String[] buttonNames = {"Customer Account", "Restaurant", "Order & Payment", "Reviews", "Driver", "Admin"};
        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new MainButtonListener());
            buttonPanel.add(button);
        }
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        cardPanel.add(mainPanel, "MainPanel");


        for (String name : buttonNames) {
            switch (name) {
                case "Customer Account" -> cardPanel.add(new CustomerAccountPage(cardLayout, cardPanel,this.appController), name + "Page");
                case "Restaurant" -> cardPanel.add(new RestaurantPage(cardLayout, cardPanel,this.appController), name + "Page");
                case "Order & Payment" -> cardPanel.add(new OrderPage(cardLayout, cardPanel,this.appController), name + "Page");
                case "Reviews" -> cardPanel.add(new ReviewPage(cardLayout, cardPanel,this.appController), name + "Page");
                case "Driver" -> cardPanel.add(new DriverPage(cardLayout, cardPanel,this.appController), name + "Page");
                case "Admin" -> cardPanel.add(new ProjectionPage(cardLayout, cardPanel,this.appController), name + "Page");
                default -> {
                    JPanel backgroundPanel = new JPanel();
                    backgroundPanel.setBackground(Color.WHITE);
                    cardPanel.add(backgroundPanel, name + "Page");
                }
            }
        }

        cardPanel.add(new MenuPage(cardLayout, cardPanel,this.appController), "MenuPage");
        cardPanel.add(new FoodPage(cardLayout, cardPanel,this.appController), "FoodPage");
        cardPanel.add(new PaymentPage(cardLayout, cardPanel,this.appController), "PaymentPage");
        cardPanel.add(new ReviewRestPage(cardLayout, cardPanel,this.appController), "RestaurantReviewPage");
        cardPanel.add(new ReviewDriverPage(cardLayout, cardPanel,this.appController), "DriverReviewPage");


        add(cardPanel);
    }

    // Check Inputs for frontend //
    public static String checkInput(String message) {
        String result = message;
        if (message.contains("parent key not found")) {
            result = "Operation failed: please check the entity that the input relates to exist.";
        } else if (message.contains("unique constraint")) {
            result = "Operation failed: Key input value already exist in current data";
        }
        return result;
    }

    private class MainButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            cardLayout.show(cardPanel, source.getText() + "Page");
        }
    }

    //Main page should be called from AppController, this main method is just for frontend test without login.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainPage mainPage = new MainPage(new AppController());
            mainPage.setVisible(true);
        });
    }
}


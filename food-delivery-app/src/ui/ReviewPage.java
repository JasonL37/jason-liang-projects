package ui;

import controller.AppController;
import model.CustomersModel;
import model.DriversModel;
import model.RestaurantsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviewPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private AppController appController;
    private JTextArea restList;
    private JTextArea driverList;

    public ReviewPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Review", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel reviewsPanel = new JPanel(new GridLayout(1, 2));

        this.restList = new JTextArea("List restaurants ");
        JScrollPane restaurantScrollPane = new JScrollPane(restList);
        reviewsPanel.add(restaurantScrollPane);

        this.driverList = new JTextArea("list drivers");
        JScrollPane driverScrollPane = new JScrollPane(driverList);
        reviewsPanel.add(driverScrollPane);

        add(reviewsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JPanel checkRestPanel = new JPanel(new BorderLayout());
        JButton checkRestButton = new JButton("Check restaurant's review");
        checkRestButton.addActionListener(new ReviewButtonClickListener());
        checkRestPanel.add(checkRestButton, BorderLayout.CENTER);

        JPanel checkDriverPanel = new JPanel(new BorderLayout());

        JButton checkDriverButton = new JButton("Check driver's review");
        checkDriverButton.addActionListener(new ReviewButtonClickListener());
        checkDriverPanel.add(checkDriverButton, BorderLayout.CENTER);

        buttonPanel.add(checkRestPanel);
        buttonPanel.add(checkDriverPanel);


        JButton backButton = new JButton("Go back to main page");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, backButton.getPreferredSize().height));
        backButton.addActionListener(new ReviewButtonClickListener());
        buttonPanel.add(backButton, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }
    private void loadTables() {
        Object[] projectionRest =  appController.service("restaurant", "projection", new RestaurantsModel("y","y","",""));
        restList.setText("Restaurant phone  Restaurant name\n");
        restList.append(String.valueOf(loadReviews(projectionRest)));
        Object[] projectionDriver =  appController.service("driver", "projection", new DriversModel("y","y","",""));
        driverList.setText("Driver phone           Driver name\n");
        driverList.append(String.valueOf(loadReviews(projectionDriver)));
    }

    private StringBuilder loadReviews(Object[] results) {
        StringBuilder message = new StringBuilder();
        for (Object result: results) {
            Object[] row = (Object[]) result;
            for (int i = 0; i < row.length; i++) {
                message.append( row[i]).append("      ");
            }
            message.append("\n");
        }
            return message;

    }

    private class ReviewButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Check restaurant's review")) {
                cardLayout.show(cardPanel, "RestaurantReviewPage");
            } else if (source.getText().equals("Check driver's review")) {
                cardLayout.show(cardPanel, "DriverReviewPage");
            } else {
                JOptionPane.showMessageDialog(ReviewPage.this, source.getText() + " button clicked");
            }

        }
    }
}


package ui;

import controller.AppController;
import model.FoodReviewsModel;
import ui.InputWindows.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class ReviewRestPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private AppController appController;

    private JTextArea restReviewList;

    public ReviewRestPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Restaurant Review", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        this.restReviewList = new JTextArea("");
        JScrollPane scrollPane = new JScrollPane(restReviewList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        String[] buttonNames = {"Make new reviews", "Delete reviews","Find restaurant with food rating greater than 3",
                "Go back to main page"};
        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new ReviewButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }
    private void loadTables() {
        Object[] tables = appController.requestTable("foodReview");
        this.restReviewList.setText("List restaurants and food reviews \n");
        this.restReviewList.append("Customer phone          Time stamp        Title         Image       Comment         Rating type         " +
                "Restaurant address          Food quality rating      Portion size rating\n");
        for (Object obj: tables) {
            FoodReviewsModel review = (FoodReviewsModel) obj;
            restReviewList.append(review.getCustomerPhone() + "  ");
            restReviewList.append(review.getReviewTimestamp() + "  ");
            restReviewList.append(review.getReviewTitle() + "  ");
            restReviewList.append(review.getReviewImage() + "  ");
            restReviewList.append(review.getReviewComment() + "  ");
            restReviewList.append(review.getReviewRatingType() + "  ");
            restReviewList.append(review.getRestaurantAddress() + "  ");
            restReviewList.append(review.getFoodReviewFoodQualityRating() + "  ");
            restReviewList.append(review.getFoodReviewPortionSizeRating() + "  \n");
        }
    }

    private void loadReviews(Object[] results) {
        StringBuilder message = new StringBuilder();
        message.append("Restaurant with food rating greater than 3\n");
        message.append("Restaurant address       Highest food rating\n");
        boolean found = false;
        for (Object result: results) {
            Object[] row = (Object[]) result;
            String address = (String) row[0];
            int rating = (int) row[1];
            found = true;
            message.append(address).append("                             ").append(rating).append("\n");

        }
        if (!found) {
            JOptionPane.showMessageDialog(ReviewRestPage.this, "No such restaurant exist");
        } else {
            JOptionPane.showMessageDialog(ReviewRestPage.this, message.toString());
        }
    }

    private class ReviewButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Find restaurant with food rating greater than 3")) {
                Object[] aggMessage = appController.service("foodReview", "aggregationHaving",
                        new FoodReviewsModel("", Timestamp.valueOf("2024-12-01 00:00:00"),"","","","","",0,0));
                loadReviews(aggMessage);
            } else if (source.getText().equals("Make new reviews")) {
                ReviewRestInsert dialog = new ReviewRestInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage = (String[]) appController.service("foodReview", "insert", dialog.getFoodReviewModel());
                    JOptionPane.showMessageDialog(ReviewRestPage.this, MainPage.checkInput(insertMessage[0]));
                    loadTables();
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Delete reviews")) {
                ReviewRestDelete dialog = new ReviewRestDelete((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] tables = appController.requestTable("foodReview");
                    boolean found = false;
                    for (Object obj: tables) {
                        FoodReviewsModel foodReview = (FoodReviewsModel) obj;
                        if (foodReview.getCustomerPhone().equals(dialog.getPhone()) && foodReview.getReviewTimestamp().equals(dialog.getTimeStamp())) {
                            String[] deleteMessage = (String[]) appController.service("foodReview", "delete", foodReview);
                            JOptionPane.showMessageDialog(ReviewRestPage.this, deleteMessage[0]);
                            found = true;
                            loadTables();
                            break;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(ReviewRestPage.this, "Can not find this review");
                    }
                } else {
                    loadTables();
                }

            } else {
                JOptionPane.showMessageDialog(ReviewRestPage.this, source.getText() + " button clicked");
            }

        }
    }
}


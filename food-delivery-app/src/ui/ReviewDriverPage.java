package ui;

import controller.AppController;
import model.DriverReviewsModel;
import ui.InputWindows.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviewDriverPage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private AppController appController;
    private JTextArea driverReviewList;

    public ReviewDriverPage(CardLayout cardLayout, JPanel cardPanel, AppController appController) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.appController = appController;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Driver Review", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        this.driverReviewList = new JTextArea("List drivers and their reviews");
        JScrollPane scrollPane = new JScrollPane(driverReviewList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        String[] buttonNames = {"Make new reviews", "Delete reviews",
                "Go back to main page"};
        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.addActionListener(new ReviewDriverPage.ReviewButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.SOUTH);
        loadTables();
    }
    private void loadTables() {
        Object[] tables = appController.requestTable("driverReview");
        this.driverReviewList.setText("List Driver and food reviews \n");
        this.driverReviewList.append("Customer phone       Time stamp     Title      Image     Comment         Rating type         " +
                "Restaurant address          Package handling rating      Delivery time rating\n");
        for (Object obj: tables) {
            DriverReviewsModel review = (DriverReviewsModel) obj;
            driverReviewList.append(review.getCustomerPhone() + "  ");
            driverReviewList.append(review.getReviewTimestamp() + "  ");
            driverReviewList.append(review.getReviewTitle() + "  ");
            driverReviewList.append(review.getReviewImage() + "  ");
            driverReviewList.append(review.getReviewComment() + "  ");
            driverReviewList.append(review.getReviewRatingType() + "  ");
            driverReviewList.append(review.getDriverPhone() + "  ");
            driverReviewList.append(review.getDriverReviewPackageHandlingRating() + "  ");
            driverReviewList.append(review.getDriverReviewDeliveryTimeRating() + "  \n");
        }
    }

    private class ReviewButtonClickListener extends Component implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Go back to main page")) {
                cardLayout.show(cardPanel, "MainPanel");
            } else if (source.getText().equals("Make new reviews")) {
                ReviewDriverInsert dialog = new ReviewDriverInsert((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    String[] insertMessage = (String[]) appController.service("driverReview", "insert", dialog.getDriverReviewModel());
                    JOptionPane.showMessageDialog(ReviewDriverPage.this, MainPage.checkInput(insertMessage[0]));
                    loadTables();
                } else {
                    loadTables();
                }

            } else if (source.getText().equals("Delete reviews")) {
                ReviewDriverDelete dialog = new ReviewDriverDelete((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                if (dialog.isValidated()) {
                    Object[] tables = appController.requestTable("driverReview");
                    boolean found = false;
                    for (Object obj: tables) {
                        DriverReviewsModel driverReview = (DriverReviewsModel) obj;
                        if (driverReview.getCustomerPhone().equals(dialog.getPhone()) && driverReview.getReviewTimestamp().equals(dialog.getTimeStamp())) {
                            String[] deleteMessage = (String[]) appController.service("driverReview", "delete", driverReview);
                            JOptionPane.showMessageDialog(ReviewDriverPage.this, deleteMessage[0]);
                            found = true;
                            loadTables();
                            break;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(ReviewDriverPage.this, "Can not find this review");
                    }
                } else {
                    loadTables();
                }

            } else {
                JOptionPane.showMessageDialog(ReviewDriverPage.this, source.getText() + " button clicked");
            }

        }
    }
}

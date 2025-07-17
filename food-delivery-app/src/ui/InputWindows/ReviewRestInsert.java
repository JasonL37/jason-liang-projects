package ui.InputWindows;

import model.FoodReviewsModel;
import model.ReviewAdvancedInfoModel;
import model.ReviewBasicInfoModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class ReviewRestInsert extends JDialog {
    JTextField cusPhone = new JTextField(20);
    JTextField timeStamp = new JTextField(20);
    private JTextField Title = new JTextField(20);
    private JTextField Image= new JTextField(20);
    private JTextField Comment= new JTextField(20);
    private JTextField RatingType= new JTextField(20);
    JTextField restAddress = new JTextField(20);
    JTextField foodQuality = new JTextField(20);
    JTextField portionSize = new JTextField(20);

    private boolean validated = false;

    JTextField[] textFields = {cusPhone, timeStamp,Title,Image,Comment,RatingType, restAddress, foodQuality, portionSize};

    public ReviewRestInsert(Frame parent) {
        super(parent, "Make new reviews", true);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(getParent());

        JLabel title = new JLabel("Make new reviews", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);


        JPanel attrPanel = new JPanel(new GridLayout(0, 1));
        String[] attrNames = {"*Customer phone","*Time Stamp:", "*Title:", "Image:", "*Comment:", "Rating type", "*Restaurant address", "Food quality:", "Portion size:"};
        for (String attr: attrNames) {
            attrPanel.add(new JLabel(attr));
        }

        JPanel inputPanel = new JPanel(new GridLayout(0,1));
        for (JTextField tf: textFields) {
            inputPanel.add(tf);
        }
        add(attrPanel, BorderLayout.WEST);
        add(inputPanel,BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        registerButton.addActionListener(new InsertButtonClickListener());
        cancelButton.addActionListener(new InsertButtonClickListener());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);



    }
    public ReviewAdvancedInfoModel getReviewAdvModel() {
        return new ReviewAdvancedInfoModel(Comment.getText(), RatingType.getText());
    }
    public ReviewBasicInfoModel getReviewBasicModel() {
        return new ReviewBasicInfoModel(cusPhone.getText(), Timestamp.valueOf(timeStamp.getText()),Title.getText(),Image.getText(),Comment.getText());
    }
    public FoodReviewsModel getFoodReviewModel() {
        return new FoodReviewsModel(cusPhone.getText(), Timestamp.valueOf(timeStamp.getText()),Title.getText(),Image.getText(),Comment.getText()
                ,RatingType.getText(),restAddress.getText(), Integer.parseInt(foodQuality.getText()),Integer.parseInt(portionSize.getText()));
    }

    public boolean isValidated() {
        return this.validated;
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Add")) {
                if (cusPhone.getText().isEmpty() || Title.getText().isEmpty() || Comment.getText().isEmpty() || restAddress.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(ReviewRestInsert.this, "Some values can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Timestamp.valueOf(timeStamp.getText());
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(ReviewRestInsert.this, "Incorrect time stamp format: must be yyyy-mm-dd hh:mm:ss");
                        success =false;
                    }
                    try {
                        Integer.parseInt(foodQuality.getText());
                        Integer.parseInt(portionSize.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(ReviewRestInsert.this, "Food quality rating and portion size rating must be integer");
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


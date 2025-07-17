package ui.InputWindows;

import model.DriverReviewsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class ReviewDriverInsert extends JDialog {
    JTextField cusPhone = new JTextField(20);
    JTextField timeStamp = new JTextField(20);
    private JTextField Title = new JTextField(20);
    private JTextField Image= new JTextField(20);
    private JTextField Comment= new JTextField(20);
    private JTextField RatingType= new JTextField(20);
    JTextField driverPhone = new JTextField(20);
    JTextField packageHandle = new JTextField(20);
    JTextField deliveryTime = new JTextField(20);

    private boolean validated = false;

    JTextField[] textFields = {cusPhone, timeStamp,Title,Image,Comment,RatingType, driverPhone, packageHandle, deliveryTime};

    public ReviewDriverInsert(Frame parent) {
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
        String[] attrNames = {"*Customer phone","*Time Stamp:", "*Title:", "Image:", "*Comment:", "Rating type", "*Driver phone", "Package handling", "Delivery time:"};
        for (String attr: attrNames) {
            attrPanel.add(new JLabel(attr));
        }

        JPanel inputPanel = new JPanel(new GridLayout(0,1));
        for (JTextField tf: textFields) {
            inputPanel.add(tf);
        }
        add(attrPanel, BorderLayout.WEST);
        add(inputPanel,BorderLayout.EAST);


        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        registerButton.addActionListener(new InsertButtonClickListener());
        cancelButton.addActionListener(new InsertButtonClickListener());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);



    }
    public DriverReviewsModel getDriverReviewModel() {
        return new DriverReviewsModel(cusPhone.getText(), Timestamp.valueOf(timeStamp.getText()),Title.getText(),Image.getText(),Comment.getText()
                ,RatingType.getText(), driverPhone.getText(), Integer.parseInt(packageHandle.getText()),Integer.parseInt(deliveryTime.getText()));
    }

    public boolean isValidated() {
        return this.validated;
    }

    private class InsertButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().equals("Add")) {
                if (timeStamp.getText().isEmpty() || cusPhone.getText().isEmpty() || Title.getText().isEmpty() || Comment.getText().isEmpty() || driverPhone.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(ReviewDriverInsert.this, "Some values can not be empty");
                } else {
                    boolean success = true;
                    try {
                        Timestamp.valueOf(timeStamp.getText());
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(ReviewDriverInsert.this, "Incorrect time stamp format: must be yyyy-mm-dd hh:mm:ss");
                        success =false;
                    }
                    try {
                        Integer.parseInt(packageHandle.getText());
                        Integer.parseInt(deliveryTime.getText());
                    }catch (Exception err) {
                        JOptionPane.showMessageDialog(ReviewDriverInsert.this, "Package handling rating and delivery time rating must be integer");
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


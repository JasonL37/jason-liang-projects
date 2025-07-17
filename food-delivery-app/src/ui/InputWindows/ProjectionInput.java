package ui.InputWindows;
import delegates.ServiceModelDelegate;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class ProjectionInput extends JDialog {

    private String selectedOption;
    private JTextField[] inputFields;
    private String[] attrStrings;
    private boolean validated = false;

    public ProjectionInput(Frame parent, String selectedOption) {
        super(parent, "Select attributes", true);
        this.selectedOption = selectedOption;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Type yes on attributes you wish to show, leave blank on others", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        this.attrStrings = switch (selectedOption) {
            case "customer" -> new String[]{"Phone:","Name:", "Address:", "Email:", "Customer account:", "Credit card:", "Membership:", "Points:", "Discount:"};
            case "restaurant" -> new String[]{"Address:","Name:",  "Open Hours:", "Restaurant account:"};
            case "menuAdvanced" -> new String[]{"Name:", "Cuisine:"};
            case "menuBasic" -> new String[]{"Menu ID:", "Name:"};
            case "foodAdvanced" -> new String[]{"Food type:", "Is it vegan:"};
            case "foodBasic" -> new String[]{"Food name:", "Food price:", "Food type:"};
            case "driver" -> new String[]{"Phone:", "Name:", "Driver account:", "Vehicle:"};
            case "order" -> new String[]{"Order ID:", "Total price:", "Time arrival:", "Customer phone:", "Restaurant address:", "Driver phone:"};
            case "paymentAdvanced" -> new String[]{"Order ID:", "Price:", "Restaurant address:", "Driver phone:"};
            case "paymentBasic" -> new String[]{"Payment ID:", "Status:", "Order ID:"};
            case "reviewAdvanced" -> new String[]{"Comment:", "Rating type:"};
            case "reviewBasic" -> new String[]{"Customer phone:", "Time stamp:", "Title:", "Image:", "Comment:"};
            case "foodReview" -> new String[]{"Customer phone:", "Time stamp:",  "Restaurant address", "Food quality rating:", "Portion size rating:"};
            case "driverReview" -> new String[]{"Customer phone:", "Time stamp:", "Driver phone:", "Package handling rating:", "Delivery time rating:"};
            case "own" -> new String[]{"Restaurant address:", "Menu ID:"};
            case "contain" -> new String[]{"Menu ID:", "Food name:"};
            case "ordered" -> new String[]{"Food name:", "Order ID:", "Quantity:"};


            default -> attrStrings = new String[]{};

        };

        inputFields = new JTextField[attrStrings.length];
        for (int i = 0; i < attrStrings.length; i++) {
            formPanel.add(new JLabel(attrStrings[i]));
            inputFields[i] = new JTextField();
            formPanel.add(inputFields[i]);
        }

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        confirmButton.addActionListener(new ConfirmButtonActionListener());
        cancelButton.addActionListener(new ConfirmButtonActionListener());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(getParent());
    }

    public ServiceModelDelegate loadTables() {
        switch (selectedOption) {
            case "customer" : return new CustomersModel("y","y","y","y","y","y","y",1,"y");
            case "restaurant" : return new RestaurantsModel("y","y","y","y");
            case "menuAdvanced" : return new MenuAdvancedInfoModel("y","y");
            case "menuBasic" : return new MenuBasicInfoModel(1,"y");
            case "foodAdvanced" : return new FoodAdvancedInfoModel("y","y");
            case "foodBasic" : return new FoodBasicInfoModel("y",1.0,"y");
            case "order" : return new OrdersModel(1,2.0,Timestamp.valueOf("2002-02-02 00:00:00"),"y","y","y");
            case "driver" : return new DriversModel("y","y","y","y");
            case "reviewAdvanced" : return new ReviewAdvancedInfoModel("y","y");
            case "reviewBasic" : return new ReviewBasicInfoModel("y",Timestamp.valueOf("2002-02-02 00:00:00"),"y","y","y");
            case "foodReview" : return new FoodReviewsModel("y",Timestamp.valueOf("2002-02-02 00:00:00"),"y","y","y","y","y",1,1);
            case "driverReview" : return new DriverReviewsModel("y",Timestamp.valueOf("2002-02-02 00:00:00"),"y","y","y","y","y",1,1);
            case "paymentAdvanced" : return new PaymentAdvancedInfoModel(1,1.0,"y","y");
            case "paymentBasic" : return new PaymentBasicInfoModel(1,"y",1);
            case "own" : return new OwnModel("y",1);
            case "contain" : return new ContainModel(1,"y");
            case "ordered" : return new OrderedModel("y",1,1);
        }
        return null;
    }

    public ServiceModelDelegate selectModel() {
        switch (selectedOption) {
            case "customer" : return getCustomerModel();
            case "restaurant" : return getRestModel();
            case "menuAdvanced" : return getMenuAdvanceModel();
            case "menuBasic" : return getMenuBasicModel();
            case "foodAdvanced" : return getFoodAdvanceModel();
            case "foodBasic" : return getFoodBasicModel();
            case "order" : return getOrderModel();
            case "driver" : return getDriverModel();
            case "reviewAdvanced" : return getReviewAdvanceModel();
            case "reviewBasic" : return getReviewBasicModel();
            case "foodReview" : return getFoodReviewModel();
            case "driverReview" : return getDriverReviewModel();
            case "paymentAdvanced" : return getPayAdvanceModel();
            case "paymentBasic" : return getPayBasicModel();
            case "own" : return getOwnModel();
            case "contain" : return getContainModel();
            case "ordered" : return getOrderedModel();


        }
        return null;
    }
    public CustomersModel getCustomerModel() {
        int i = 1;
        CustomersModel query = new CustomersModel("","","","","","","",null,"");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setCustomerPhone("yes"); break;
                    case 2: query.setCustomerName("yes");break;
                    case 3: query.setCustomerAddress("yes");break;
                    case 4: query.setCustomerEmail("yes");break;
                    case 5: query.setCustomerAccount("yes");break;
                    case 6: query.setCustomerPaymentInfo("yes");break;
                    case 7: query.setCustomerMembership("yes");break;
                    case 8: query.setCustomerPoints(101);break;
                    case 9: query.setCustomerDiscount("yes");break;
                }
            }
            i++;
        }
        return query;
    }

    public RestaurantsModel getRestModel() {
        int i = 1;
        RestaurantsModel query = new RestaurantsModel("","","","");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setRestaurantAddress("yes");break;
                    case 2: query.setRestaurantName("yes");break;
                    case 3: query.setRestaurantOpenHours("yes");break;
                    case 4: query.setRestaurantAccount("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public MenuAdvancedInfoModel getMenuAdvanceModel() {
        int i = 1;
        MenuAdvancedInfoModel query = new MenuAdvancedInfoModel("","");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setMenuName("yes");break;
                    case 2: query.setMenuCuisine("yes");break;
                }
            }
            i++;
        }
        return query;
    }

    public MenuBasicInfoModel getMenuBasicModel() {
        int i = 1;
        MenuBasicInfoModel query = new MenuBasicInfoModel(null,"");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setMenuID(101);break;
                    case 2: query.setMenuName("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public FoodAdvancedInfoModel getFoodAdvanceModel() {
        int i = 1;
        FoodAdvancedInfoModel query = new FoodAdvancedInfoModel("","");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setFoodType("yes");break;
                    case 2: query.setFoodHasVeganDiet("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public FoodBasicInfoModel getFoodBasicModel() {
        int i = 1;
        FoodBasicInfoModel query = new FoodBasicInfoModel("",null,"");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setFoodName("yes");break;
                    case 2: query.setFoodPrice(101.0);break;
                    case 3: query.setFoodType("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public DriversModel getDriverModel() {
        int i = 1;
        DriversModel query = new DriversModel("","","","");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setDriverPhone("yes");break;
                    case 2: query.setDriverName("yes");break;
                    case 3: query.setDriverAccount("yes");break;
                    case 4: query.setDriverVehicle("yes");break;
                }
            }
            i++;
        }
        return query;
    }

    public OrdersModel getOrderModel() {
        int i = 1;
        OrdersModel query = new OrdersModel(null,null,null,"","","");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setOrderID(101);break;
                    case 2: query.setOrderTotalPrice(1.0);break;
                    case 3: query.setOrderTimeArrival(Timestamp.valueOf("2000-01-01 00:00:00"));break;
                    case 4: query.setCustomerPhone("yes");break;
                    case 5: query.setRestaurantAddress("yes");break;
                    case 6: query.setDriverPhone("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public PaymentAdvancedInfoModel getPayAdvanceModel() {
        int i = 1;
        PaymentAdvancedInfoModel query = new PaymentAdvancedInfoModel(null,null,"","");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setOrderID(1);break;
                    case 2: query.setPaymentPrice(1.0);break;
                    case 3: query.setRestaurantAddress("yes");break;
                    case 4: query.setDriverPhone("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public PaymentBasicInfoModel getPayBasicModel() {
        int i = 1;
        PaymentBasicInfoModel query = new PaymentBasicInfoModel(null,"",null);
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setPaymentID(1);break;
                    case 2: query.setPaymentStatus("yes");break;
                    case 3: query.setOrderID(1);break;
                }
            }
            i++;
        }
        return query;
    }
    public ReviewAdvancedInfoModel getReviewAdvanceModel() {
        int i = 1;
        ReviewAdvancedInfoModel query = new ReviewAdvancedInfoModel("","");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setReviewComment("yes");break;
                    case 2: query.setReviewRatingType("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public ReviewBasicInfoModel getReviewBasicModel() {
        int i = 1;
        ReviewBasicInfoModel query = new ReviewBasicInfoModel("",null,"","","");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setCustomerPhone("yes");break;
                    case 2: query.setReviewTimestamp(Timestamp.valueOf("2000-01-01 00:00:00"));break;
                    case 3: query.setReviewTitle("yes");break;
                    case 4: query.setReviewImage("yes");break;
                    case 5: query.setReviewComment("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public FoodReviewsModel getFoodReviewModel() {
        int i = 1;
        FoodReviewsModel query = new FoodReviewsModel("",null,"","","",
                "","",null,null);
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setCustomerPhone("yes");break;
                    case 2: query.setReviewTimestamp(Timestamp.valueOf("2000-01-01 00:00:00"));break;
                    case 3: query.setRestaurantAddress("yes");break;
                    case 4: query.setFoodReviewFoodQualityRating(101);break;
                    case 5: query.setFoodReviewPortionSizeRating(1);break;
                }
            }
            i++;
        }
        return query;
    }
    public DriverReviewsModel getDriverReviewModel() {
        int i = 1;
        DriverReviewsModel query = new DriverReviewsModel("",null,"","","",
                "","",null,null);
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setCustomerPhone("yes");break;
                    case 2: query.setReviewTimestamp(Timestamp.valueOf("2000-01-01 00:00:00"));break;
                    case 3: query.setDriverPhone("yes");break;
                    case 4: query.setDriverReviewPackageHandlingRating(101);break;
                    case 5: query.setDriverReviewDeliveryTimeRating(1);break;
                }
            }
            i++;
        }
        return query;
    }
    public OwnModel getOwnModel() {
        int i = 1;
        OwnModel query = new OwnModel("",null);
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setRestaurantAddress("yes");break;
                    case 2: query.setMenuID(1);break;
                }
            }
            i++;
        }
        return query;
    }

    public ContainModel getContainModel() {
        int i = 1;
        ContainModel query = new ContainModel(null,"");
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setMenuID(1);break;
                    case 2: query.setFoodName("yes");break;
                }
            }
            i++;
        }
        return query;
    }
    public OrderedModel getOrderedModel() {
        int i = 1;
        OrderedModel query = new OrderedModel("",null,null);
        for (JTextField input: inputFields) {
            if (!input.getText().isEmpty()) {
                switch (i) {
                    case 1: query.setFoodName("yes");break;
                    case 2: query.setOrderID(1);break;
                    case 3: query.setOrderedQuantity(1);break;
                }
            }
            i++;
        }
        return query;
    }


    public boolean isValidated() {
        return validated;
    }


    private class ConfirmButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            boolean noInput = true;
            if (source.getText().equals("Confirm")) {
                for (JTextField input: inputFields) {
                    if (!input.getText().isEmpty()) {
                        noInput = false;
                    }
                }
                if (noInput) {
                    JOptionPane.showMessageDialog(ProjectionInput.this, "Please select at least one attribute");
                }else {
                    validated = true;
                    dispose();
                }

            } else {
                validated = false;
                dispose();
            }

        }
    }
}

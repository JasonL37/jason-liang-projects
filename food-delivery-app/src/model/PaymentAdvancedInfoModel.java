package model;

import delegates.ServiceModelDelegate;

public class PaymentAdvancedInfoModel implements ServiceModelDelegate {
    private Integer orderID;
    private Double paymentPrice;
    private String restaurantAddress;
    private String driverPhone;

    public PaymentAdvancedInfoModel(Integer orderID,
                                    Double price,
                                    String restaurantAddress,
                                    String driverPhone) {
        this.orderID = orderID;
        this.paymentPrice = price;
        this.restaurantAddress = restaurantAddress;
        this.driverPhone = driverPhone;
    }

    public Integer getOrderID() { return orderID; }
    public Double getPaymentPrice() { return paymentPrice; }
    public String getRestaurantAddress() { return restaurantAddress; }
    public String getDriverPhone() { return driverPhone; }

    public void setOrderID(Integer orderID) { this.orderID = orderID; }
    public void setPaymentPrice(Double price) { this.paymentPrice = price; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
}

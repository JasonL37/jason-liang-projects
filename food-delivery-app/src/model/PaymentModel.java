package model;

public class PaymentModel {
    private Integer paymentID;
    private String paymentStatus;
    private Integer orderID;
    private Double paymentPrice;
    private String restaurantAddress;
    private String driverPhone;

    public PaymentModel(Integer paymentID,
                        String status,
                        Integer orderID,
                        Double price,
                        String restaurantAddress,
                        String driverPhone) {
        this.paymentID = paymentID;
        this.paymentStatus = status;
        this.orderID = orderID;
        this.paymentPrice = price;
        this.restaurantAddress = restaurantAddress;
        this.driverPhone = driverPhone;
    }

    public Integer getPaymentID() { return paymentID; }
    public String getPaymentStatus() { return paymentStatus; }
    public Integer getOrderID() { return orderID; }
    public Double getPaymentPrice() { return paymentPrice; }
    public String getRestaurantAddress() { return restaurantAddress; }
    public String getDriverPhone() { return driverPhone; }

    public void setPaymentID(Integer paymentID) { this.paymentID = paymentID; }
    public void setPaymentStatus(String status) { this.paymentStatus = status; }
    public void setOrderID(Integer orderID) { this.orderID = orderID; }
    public void setPaymentPrice(Double price) { this.paymentPrice = price; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
}

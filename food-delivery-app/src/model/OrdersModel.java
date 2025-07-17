package model;

import delegates.ServiceModelDelegate;

import java.sql.Timestamp;

public class OrdersModel implements ServiceModelDelegate {
    private Integer orderID;
    private Double orderTotalPrice;
    private Timestamp orderTimeArrival;
    private String customerPhone;
    private String driverPhone;
    private String restaurantAddress;

    public OrdersModel(Integer orderID,
                       Double totalPrice,
                       Timestamp timeArrival,
                       String customerPhone,
                       String restaurantAddress,
                       String driverPhone) {
        this.orderID = orderID;
        this.orderTotalPrice = totalPrice;
        this.orderTimeArrival = timeArrival;
        this.customerPhone = customerPhone;
        this.restaurantAddress = restaurantAddress;
        this.driverPhone = driverPhone;
    }

    public Integer getOrderID() { return orderID; }
    public Double getTotalPrice() { return orderTotalPrice; }
    public Timestamp getTimeArrival() { return orderTimeArrival; }
    public String getCustomerPhone() { return customerPhone; }
    public String getRestaurantAddress() { return restaurantAddress; }
    public String getDriverPhone() { return driverPhone; }

    public void setOrderID(Integer orderID) { this.orderID = orderID; }
    public void setOrderTotalPrice(Double totalPrice) { this.orderTotalPrice = totalPrice; }
    public void setOrderTimeArrival(Timestamp timeArrival) { this.orderTimeArrival = timeArrival; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
}

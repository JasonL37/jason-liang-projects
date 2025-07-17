package model;

import delegates.ServiceModelDelegate;

public class PaymentBasicInfoModel implements ServiceModelDelegate {
    private Integer paymentID;
    private String paymentStatus;
    private Integer orderID;

    public PaymentBasicInfoModel(Integer paymentID,
                                 String status,
                                 Integer orderID) {
        this.paymentID = paymentID;
        this.paymentStatus = status;
        this.orderID = orderID;
    }

    public Integer getPaymentID() { return paymentID; }
    public String getPaymentStatus() { return paymentStatus; }
    public Integer getOrderID() { return orderID; }

    public void setPaymentID(Integer paymentID) { this.paymentID = paymentID; }
    public void setPaymentStatus(String status) { this.paymentStatus = status; }
    public void setOrderID(Integer orderID) { this.orderID = orderID; }
}

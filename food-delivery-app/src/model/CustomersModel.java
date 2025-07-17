package model;

import delegates.ServiceModelDelegate;

public class CustomersModel implements ServiceModelDelegate {
    private String customerPhone;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String customerAccount;
    private String customerPaymentInfo;
    private String customerMembership;
    private Integer customerPoints;
    private String customerDiscount;

    public CustomersModel (String phone,
                           String name,
                           String address,
                           String email,
                           String account,
                           String paymentInfo,
                           String membership,
                           Integer points,
                           String discount) {
        this.customerPhone = phone;
        this.customerName = name;
        this.customerAddress = address;
        this.customerEmail = email;
        this.customerAccount = account;
        this.customerPaymentInfo = paymentInfo;
        if (membership != null) {
            this.customerMembership = membership;
        }
        this.customerPoints = points;
        this.customerDiscount = discount;
    }

    public String getCustomerPhone() { return customerPhone; }
    public String getCustomerName() { return customerName; }
    public String getCustomerAddress() { return customerAddress; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerAccount() { return customerAccount; }
    public String getCustomerPaymentInfo() { return customerPaymentInfo; }
    public String getCustomerMembership() { return customerMembership; }
    public Integer getCustomerPoints() { return customerPoints; }
    public String getCustomerDiscount() { return customerDiscount; }

    public void setCustomerPhone(String phone) { this.customerPhone = phone; }
    public void setCustomerName(String name) { this.customerName = name; }
    public void setCustomerAddress(String address) { this.customerAddress = address; }
    public void setCustomerEmail(String email) { this.customerEmail = email; }
    public void setCustomerAccount(String account) { this.customerAccount = account; }
    public void setCustomerPaymentInfo(String paymentInfo) { this.customerPaymentInfo = paymentInfo; }
    public void setCustomerMembership(String membership) { this.customerMembership = membership; }
    public void setCustomerPoints(Integer points) { this.customerPoints = points; }
    public void setCustomerDiscount(String discount) { this.customerDiscount = discount; }
}

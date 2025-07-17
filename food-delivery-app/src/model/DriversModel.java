package model;

import delegates.ServiceModelDelegate;

public class DriversModel implements ServiceModelDelegate {
    private String driverPhone;
    private String driverName;
    private String driverAccount;
    private String driverVehicle;

    public DriversModel(String phone,
                        String name,
                        String account,
                        String vehicle) {
        this.driverPhone = phone;
        this.driverName = name;
        this.driverAccount = account;
        this.driverVehicle = vehicle;
    }

    public String getDriverPhone() { return driverPhone; }
    public String getDriverName() { return driverName; }
    public String getDriverAccount() { return driverAccount; }
    public String getDriverVehicle() { return driverVehicle; }

    public void setDriverPhone(String phone) { this.driverPhone = phone; }
    public void setDriverName(String name) { this.driverName = name; }
    public void setDriverAccount(String account) { this.driverAccount = account; }
    public void setDriverVehicle(String vehicle) { this.driverVehicle = vehicle; }
}

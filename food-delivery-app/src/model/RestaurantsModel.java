package model;

import delegates.ServiceModelDelegate;

public class RestaurantsModel implements ServiceModelDelegate {
    private String restaurantAddress;
    private String restaurantName;
    private String restaurantOpenHours;
    private String restaurantAccount;

    public RestaurantsModel(String address,
                            String name,
                            String openHours,
                            String account) {
        this.restaurantAddress = address;
        this.restaurantName = name;
        this.restaurantOpenHours = openHours;
        this.restaurantAccount = account;
    }

    public String getRestaurantAddress() { return restaurantAddress; }
    public String getRestaurantName() { return restaurantName; }

    public String getRestaurantOpenHours() { return restaurantOpenHours; }
    public String getRestaurantAccount() { return restaurantAccount; }

    public void setRestaurantAddress(String address) { this.restaurantAddress = address; }
    public void setRestaurantName(String name) { this.restaurantName = name; }
    public void setRestaurantOpenHours(String openHours) { this.restaurantOpenHours = openHours; }
    public void setRestaurantAccount(String account) { this.restaurantAccount = account; }
}

package model;

import delegates.ServiceModelDelegate;

public class OwnModel implements ServiceModelDelegate {
    private String restaurantAddress;
    private Integer menuID;

    public OwnModel(String restaurantAddress,
                    Integer menuID) {
        this.restaurantAddress = restaurantAddress;
        this.menuID = menuID;
    }

    public String getRestaurantAddress() { return restaurantAddress; }
    public Integer getMenuID() { return menuID; }

    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }
    public void setMenuID(Integer menuID) { this.menuID = menuID; }
}

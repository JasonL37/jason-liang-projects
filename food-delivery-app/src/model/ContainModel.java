package model;

import delegates.ServiceModelDelegate;

public class ContainModel implements ServiceModelDelegate {
    private Integer menuID;
    private String foodName;

    public ContainModel(Integer menuID,
                        String foodName) {
        this.menuID = menuID;
        this.foodName = foodName;
    }

    public Integer getMenuID() { return menuID; }
    public String getFoodName() { return foodName; }

    public void setMenuID(Integer menuID) { this.menuID = menuID; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
}

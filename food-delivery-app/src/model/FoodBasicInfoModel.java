package model;

import delegates.ServiceModelDelegate;

public class FoodBasicInfoModel implements ServiceModelDelegate {
    private String foodName;
    private Double foodPrice;
    private String foodType;

    public FoodBasicInfoModel(String foodName,
                              Double foodPrice,
                              String foodType) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodType = foodType;
    }

    public String getFoodName() { return foodName; }
    public Double getFoodPrice() { return foodPrice; }
    public String getFoodType() { return foodType; }

    public void setFoodName(String foodName) { this.foodName = foodName; }
    public void setFoodPrice(Double foodPrice) { this.foodPrice = foodPrice; }
    public void setFoodType(String foodType) { this.foodType = foodType; }
}

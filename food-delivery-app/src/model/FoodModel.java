package model;

import delegates.ServiceModelDelegate;

public class FoodModel implements ServiceModelDelegate {
    private String foodName;
    private Double foodPrice;
    private String foodType;
    private String foodHasVeganDiet;

    public FoodModel(String foodName,
                     Double foodPrice,
                     String foodType,
                     String foodHasVeganDiet) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodType = foodType;
        this.foodHasVeganDiet = foodHasVeganDiet;
    }

    public String getFoodName() { return foodName; }
    public Double getFoodPrice() { return foodPrice; }
    public String getFoodType() { return foodType; }
    public String getFoodHasVeganDiet() { return foodHasVeganDiet; }

    public void setFoodName(String foodName) { this.foodName = foodName; }
    public void setFoodPrice(Double foodPrice) { this.foodPrice = foodPrice; }
    public void setFoodType(String foodType) { this.foodType = foodType; }
    public void setFoodHasVeganDiet(String foodHasVeganDiet) { this.foodHasVeganDiet = foodHasVeganDiet; }
}

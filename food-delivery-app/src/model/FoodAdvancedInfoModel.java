package model;

import delegates.ServiceModelDelegate;

public class FoodAdvancedInfoModel implements ServiceModelDelegate {
    private String foodType;
    private String foodHasVeganDiet;

    public FoodAdvancedInfoModel(String foodType,
                                 String foodHasVeganDiet) {
        this.foodType = foodType;
        this.foodHasVeganDiet = foodHasVeganDiet;
    }

    public String getFoodType() { return foodType; }
    public String getFoodHasVeganDiet() { return foodHasVeganDiet; }

    public void setFoodType(String foodType) { this.foodType = foodType; }
    public void setFoodHasVeganDiet(String foodHasVeganDiet) { this.foodHasVeganDiet = foodHasVeganDiet; }
}

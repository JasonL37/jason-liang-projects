package model;

import delegates.ServiceModelDelegate;

public class OrderedModel implements ServiceModelDelegate {
    private String foodName;
    private Integer orderID;
    private Integer orderedQuantity;

    public OrderedModel(String foodName,
                        Integer orderID,
                        Integer orderedQuantity) {
        this.foodName = foodName;
        this.orderID = orderID;
        this.orderedQuantity = orderedQuantity;
    }

    public String getFoodName() { return foodName; }
    public Integer getOrderID() { return orderID; }
    public Integer getOrderedQuantity() { return orderedQuantity; }

    public void setFoodName(String foodName) { this.foodName = foodName; }
    public void setOrderID(Integer orderID) { this.orderID = orderID; }
    public void setOrderedQuantity(Integer orderedQuantity) { this.orderedQuantity = orderedQuantity; }
}

package model;

import delegates.ServiceModelDelegate;

import java.sql.Timestamp;

public class FoodReviewsModel implements ServiceModelDelegate {
    // Attributes from parent entity
    private String customerPhone;
    private Timestamp reviewTimestamp;
    private String reviewTitle;
    private String reviewImage;
    private String reviewComment;
    private String reviewRatingType;

    // Attributes as child entity
    private String restaurantAddress;
    private Integer foodReviewFoodQualityRating;
    private Integer foodReviewPortionSizeRating;

    public FoodReviewsModel(String customerPhone,
                            Timestamp timestamp,
                            String title,
                            String image,
                            String comment,
                            String ratingType,
                            String restaurantAddress,
                            Integer foodQualityRating,
                            Integer portionSizeRating) {
        this.customerPhone = customerPhone;
        this.reviewTimestamp = timestamp;
        this.reviewTitle = title;
        this.reviewImage = image;
        this.reviewComment = comment;
        this.reviewRatingType = ratingType;
        this.restaurantAddress = restaurantAddress;
        this.foodReviewFoodQualityRating = foodQualityRating;
        this.foodReviewPortionSizeRating = portionSizeRating;
    }

    public String getCustomerPhone() { return customerPhone; }
    public Timestamp getReviewTimestamp() { return reviewTimestamp; }
    public String getReviewTitle() { return reviewTitle; }
    public String getReviewImage() { return reviewImage; }
    public String getReviewComment() { return reviewComment; }
    public String getReviewRatingType() { return reviewRatingType; }
    public String getRestaurantAddress() { return restaurantAddress; }
    public Integer getFoodReviewFoodQualityRating() { return foodReviewFoodQualityRating; }
    public Integer getFoodReviewPortionSizeRating() { return foodReviewPortionSizeRating; }

    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setReviewTimestamp(Timestamp timestamp) { this.reviewTimestamp = timestamp; }
    public void setReviewTitle(String title) { this.reviewTitle = title; }
    public void setReviewImage(String image) { this.reviewImage = image; }
    public void setReviewComment(String comment) { this.reviewComment = comment; }
    public void setReviewRatingType(String ratingType) { this.reviewRatingType = ratingType; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }
    public void setFoodReviewFoodQualityRating(Integer foodQualityRating) { this.foodReviewFoodQualityRating = foodQualityRating; }
    public void setFoodReviewPortionSizeRating(Integer portionSizeRating) { this.foodReviewPortionSizeRating = portionSizeRating; }
}

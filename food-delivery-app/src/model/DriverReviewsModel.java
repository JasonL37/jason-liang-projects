package model;

import delegates.ServiceModelDelegate;

import java.sql.Timestamp;

public class DriverReviewsModel implements ServiceModelDelegate {
    // Attributes from parent entity
    private String customerPhone;
    private Timestamp reviewTimestamp;
    private String reviewTitle;
    private String reviewImage;
    private String reviewComment;
    private String reviewRatingType;

    // Attributes as child entity
    private String driverPhone;
    private Integer driverReviewPackageHandlingRating;
    private Integer driverReviewDeliveryTimeRating;

    public DriverReviewsModel(String customerPhone,
                              Timestamp timestamp,
                              String title,
                              String image,
                              String comment,
                              String ratingType,
                              String driverPhone,
                              Integer packageHandlingRating,
                              Integer deliveryTimeRating) {
        this.customerPhone = customerPhone;
        this.reviewTimestamp = timestamp;
        this.reviewTitle = title;
        this.reviewImage = image;
        this.reviewComment = comment;
        this.reviewRatingType = ratingType;
        this.driverPhone = driverPhone;
        this.driverReviewPackageHandlingRating = packageHandlingRating;
        this.driverReviewDeliveryTimeRating = deliveryTimeRating;
    }

    public String getCustomerPhone() { return customerPhone; }
    public Timestamp getReviewTimestamp() { return reviewTimestamp; }
    public String getReviewTitle() { return reviewTitle; }
    public String getReviewImage() { return reviewImage; }
    public String getReviewComment() { return reviewComment; }
    public String getReviewRatingType() { return reviewRatingType; }
    public String getDriverPhone() { return driverPhone; }
    public Integer getDriverReviewPackageHandlingRating() { return driverReviewPackageHandlingRating; }
    public Integer getDriverReviewDeliveryTimeRating() { return driverReviewDeliveryTimeRating; }

    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setReviewTimestamp(Timestamp timestamp) { this.reviewTimestamp = timestamp; }
    public void setReviewTitle(String title) { this.reviewTitle = title; }
    public void setReviewImage(String image) { this.reviewImage = image; }
    public void setReviewComment(String comment) { this.reviewComment = comment; }
    public void setReviewRatingType(String ratingType) { this.reviewRatingType = ratingType; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
    public void setDriverReviewPackageHandlingRating(Integer packageHandlingRating) { this.driverReviewPackageHandlingRating = packageHandlingRating; }
    public void setDriverReviewDeliveryTimeRating(Integer deliveryTimeRating) { this.driverReviewDeliveryTimeRating = deliveryTimeRating; }
}

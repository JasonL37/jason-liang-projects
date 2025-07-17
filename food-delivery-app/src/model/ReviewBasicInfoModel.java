package model;

import delegates.ServiceModelDelegate;
import java.sql.Timestamp;

public class ReviewBasicInfoModel implements ServiceModelDelegate {
    private String customerPhone;
    private Timestamp reviewTimestamp;
    private String reviewTitle;
    private String reviewImage;
    private String reviewComment;

    public ReviewBasicInfoModel(String customerPhone,
                                Timestamp timestamp,
                                String title,
                                String image,
                                String comment) {
        this.customerPhone = customerPhone;
        this.reviewTimestamp = timestamp;
        this.reviewTitle = title;
        this.reviewImage = image;
        this.reviewComment = comment;
    }

    public String getCustomerPhone() { return customerPhone; }
    public Timestamp getReviewTimestamp() { return reviewTimestamp; }
    public String getReviewTitle() { return reviewTitle; }
    public String getReviewImage() { return reviewImage; }
    public String getReviewComment() { return reviewComment; }

    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setReviewTimestamp(Timestamp timestamp) { this.reviewTimestamp = timestamp; }
    public void setReviewTitle(String title) { this.reviewTitle = title; }
    public void setReviewImage(String image) { this.reviewImage = image; }
    public void setReviewComment(String comment) { this.reviewComment = comment; }
}

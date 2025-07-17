package model;

import delegates.ServiceModelDelegate;

public class ReviewAdvancedInfoModel implements ServiceModelDelegate {
    private String reviewComment;
    private String reviewRatingType;

    public ReviewAdvancedInfoModel(String comment,
                                   String ratingType) {
        this.reviewComment = comment;
        this.reviewRatingType = ratingType;
    }

    public String getReviewComment() { return reviewComment; }
    public String getReviewRatingType() { return reviewRatingType; }

    public void setReviewComment(String comment) { this.reviewComment = comment; }
    public void setReviewRatingType(String ratingType) { this.reviewRatingType = ratingType; }
}

package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.FoodReviewsModel;

import java.util.Objects;

/**
 * This is the service class that will implement the foodReview operation.
 */
public class FoodReviewService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from FoodReview Service Layer";

	public FoodReviewService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which foodReview operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((FoodReviewsModel) model);
			case "delete":
				return delete((FoodReviewsModel) model);
			case "projection":
				return project((FoodReviewsModel) model);
			case "aggregationHaving":
				return aggregationHaving();
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "FoodReview operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new foodReview,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(FoodReviewsModel model) {
		String parentAdvancedQuery = "INSERT INTO ReviewAdvancedInfo VALUES (?,?)";
		String parentBasicQuery = "INSERT INTO ReviewBasicInfo VALUES (?,?,?,?,?)";
		String childQuery = "INSERT INTO FoodReviews VALUES (?,?,?,?,?)";
		return dbHandler.insertFoodReview(model, parentAdvancedQuery, parentBasicQuery, childQuery);
    }

    /**
	 * Creates the query for deleting an existing foodReview,
	 * and then implement the connection to the DOA layer.
	 */ 
    public String[] delete(FoodReviewsModel model) {
		String query = "DELETE FROM FoodReviews WHERE customer_phone = ? AND review_timeStamp = ?";
		return dbHandler.deleteFoodReview(model, query);
    }

	/*
	 * Creates the query for PROJECTION,
	 * The user can determine what attributes to show.
	 * Input: (not selected attributes must follow, "" means user does not enter anything for that text box)
	 *  	Selected attributes		: For String type: anything other than ""	; For Other types (Integer/Double/Timestamp): any instance other than null
	 * 		Not selected attributes	: For String type: ""						; For Other types: null
	 * 		We use a FoodReviewsModel here, so when calling this operation,
	 * 		it is ok to input any valid value for those parent attributes,
	 * 		which is not used here for showing only the FoodReviews table.
	 * 		Parent Attributes are: reviewTitle, reviewImage, reviewComment, reviewRatingType
	 *
	 * Returns an array of object, where each object refers to another object array.
	 */
	public Object[] project(FoodReviewsModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getCustomerPhone(), ""))
			query.append(" customer_phone,");
		if (model.getReviewTimestamp() != null)
			query.append(" review_timeStamp,");
		if (!Objects.equals(model.getRestaurantAddress(), ""))
			query.append(" restaurant_address,");
		if (model.getFoodReviewFoodQualityRating() != null)
			query.append(" foodReviews_foodQualityRating,");
		if (model.getFoodReviewPortionSizeRating() != null)
			query.append(" foodReviews_portionSizeRating,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM FoodReviews " +
				"ORDER BY customer_phone, review_timeStamp");

		return dbHandler.projectFoodReviews(model, query.toString());
	}

	/**
	 * Creates the query for AGGREGATION HAVING operation:
	 * find the restaurant addresses with the highest food quality rating over 3.
	 */
	public Object[] aggregationHaving() {
		String query = "SELECT restaurant_address, MAX(foodReviews_foodQualityRating) AS highest_food_quality_rating " +
				"FROM FoodReviews " +
				"GROUP BY restaurant_address " +
				"HAVING MAX(foodReviews_foodQualityRating) > 3 " +
				"ORDER BY restaurant_address";
		return dbHandler.aggregationHavingFoodReviews(query);
	}

	/**
	 * Creates the query for showing the FoodReviews table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public FoodReviewsModel[] showTable() {
		String query = "SELECT * " +
				"FROM ReviewBasicInfo RB, ReviewAdvancedInfo RA, FoodReviews FR " +
				"WHERE RB.review_comment = RA.review_comment AND " +
				"FR.customer_phone = RB.customer_phone AND FR.review_timeStamp = RB.review_timeStamp " +
				"ORDER BY FR.customer_phone, FR.review_timeStamp";
		return dbHandler.getFoodReviewsTable(query);
	}

}

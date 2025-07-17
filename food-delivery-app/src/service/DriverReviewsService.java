package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.DriverReviewsModel;

import java.util.Objects;

/**
 * This is the service class that will implement the DriverReview operation.
 */
public class DriverReviewsService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from DriverReview Service Layer";

	public DriverReviewsService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which DriverReview operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((DriverReviewsModel) model);
			case "delete":
				return delete((DriverReviewsModel) model);
			case "projection":
				return project((DriverReviewsModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "DriverReview operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new DriverReview,
	 * and then implement the connection to the DOA layer.
	 */
	public String[] insert(DriverReviewsModel model) {
		String parentAdvancedQuery = "INSERT INTO ReviewAdvancedInfo VALUES (?,?)";
		String parentBasicQuery = "INSERT INTO ReviewBasicInfo VALUES (?,?,?,?,?)";
		String childQuery = "INSERT INTO DriverReviews VALUES (?,?,?,?,?)";
		return dbHandler.insertDriverReview(model, parentAdvancedQuery, parentBasicQuery, childQuery);
	}

	/**
	 * Creates the query for deleting an existing DriverReview,
	 * and then implement the connection to the DOA layer.
	 */
	public String[] delete(DriverReviewsModel model) {
		String query = "DELETE FROM DriverReviews WHERE customer_phone = ? AND review_timeStamp = ?";
		return dbHandler.deleteDriverReview(model, query);
	}

	/*
	 * Creates the query for PROJECTION,
	 * The user can determine what attributes to show.
	 * Input: (not selected attributes must follow, "" means user does not enter anything for that text box)
	 *  	Selected attributes		: For String type: anything other than ""	; For Other types (Integer/Double/Timestamp): any instance other than null
	 * 		Not selected attributes	: For String type: ""						; For Other types: null
	 * 		We use a DriverReviewsModel here, so when calling this operation,
	 * 		it is ok to input any valid value for those parent attributes,
	 * 		which is not used here for showing only the DriverReviews table.
	 * 		Parent Attributes are: reviewTitle, reviewImage, reviewComment, reviewRatingType
	 *
	 * Returns an array of object, where each object refers to another object array.
	 */
	public Object[] project(DriverReviewsModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getCustomerPhone(), ""))
			query.append(" customer_phone,");
		if (model.getReviewTimestamp() != null)
			query.append(" review_timeStamp,");
		if (!Objects.equals(model.getDriverPhone(), ""))
			query.append(" driver_phone,");
		if (model.getDriverReviewPackageHandlingRating() != null)
			query.append(" driverReviews_packageHandlingRating,");
		if (model.getDriverReviewDeliveryTimeRating() != null)
			query.append(" driverReviews_deliveryTimeRating,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM DriverReviews " +
				"ORDER BY customer_phone, review_timeStamp");

		return dbHandler.projectDriverReviews(model, query.toString());
	}

	/**
	 * Creates the query for showing the DriverReviews table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public DriverReviewsModel[] showTable() {
		String query = "SELECT * " +
				"FROM ReviewBasicInfo RB, ReviewAdvancedInfo RA, DriverReviews DR " +
				"WHERE RB.review_comment = RA.review_comment AND " +
				"DR.customer_phone = RB.customer_phone AND DR.review_timeStamp = RB.review_timeStamp " +
				"ORDER BY DR.customer_phone, DR.review_timeStamp";
		return dbHandler.getDriverReviewsTable(query);
	}

}

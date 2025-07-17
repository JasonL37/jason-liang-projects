package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.ReviewBasicInfoModel;

import java.util.Objects;


/**
 * This is the service class that will implement the ReviewBasicInfo operation.
 */
public class ReviewBasicService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from ReviewBasicInfo Service Layer";

	public ReviewBasicService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which ReviewBasicInfo operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "projection":
				return project((ReviewBasicInfoModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "ReviewBasicInfo operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/*
	 * Creates the query for PROJECTION,
	 * The user can determine what attributes to show.
	 * Input: (not selected attributes must follow, "" means user does not enter anything for that text box)
	 *  	Selected attributes		: For String type: anything other than ""	; For Other types (Integer/Double/Timestamp): any instance other than null
	 * 		Not selected attributes	: For String type: ""						; For Other types: null
	 *
	 * Returns an array of object, where each object refers to another object array.
	 */
	public Object[] project(ReviewBasicInfoModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getCustomerPhone(), ""))
			query.append(" customer_phone,");
		if (model.getReviewTimestamp() != null)
			query.append(" review_timeStamp,");
		if (!Objects.equals(model.getReviewTitle(), ""))
			query.append(" review_title,");
		if (!Objects.equals(model.getReviewImage(), ""))
			query.append(" review_image,");
		if (!Objects.equals(model.getReviewComment(), ""))
			query.append(" review_comment,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM ReviewBasicInfo " +
				"ORDER BY customer_phone, review_timeStamp");

		return dbHandler.projectReviewBasicInfo(model, query.toString());
	}
}

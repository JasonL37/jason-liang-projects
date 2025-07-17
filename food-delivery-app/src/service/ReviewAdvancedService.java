package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.ReviewAdvancedInfoModel;

import java.util.Objects;


/**
 * This is the service class that will implement the ReviewAdvancedInfo operation.
 */
public class ReviewAdvancedService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from ReviewAdvancedInfo Service Layer";

	public ReviewAdvancedService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which ReviewAdvancedInfo operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "projection":
				return project((ReviewAdvancedInfoModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "ReviewAdvancedInfo operation selection went wrong.");
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
	public Object[] project(ReviewAdvancedInfoModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getReviewComment(), ""))
			query.append(" review_comment,");
		if (!Objects.equals(model.getReviewRatingType(), ""))
			query.append(" review_ratingType,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM ReviewAdvancedInfo " +
				"ORDER BY review_comment");

		return dbHandler.projectReviewAdvancedInfo(model, query.toString());
	}
}

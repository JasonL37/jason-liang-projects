package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.PaymentAdvancedInfoModel;

import java.util.Objects;


/**
 * This is the service class that will implement the paymentAdvancedInfo operation.
 */
public class PaymentAdvancedService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from PaymentAdvancedInfo Service Layer";

	public PaymentAdvancedService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which paymentAdvancedInfo operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((PaymentAdvancedInfoModel) model);
			case "projection":
				return project((PaymentAdvancedInfoModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "PaymentAdvancedInfo operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new PaymentAdvancedInfo,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(PaymentAdvancedInfoModel model) {
		String query = "INSERT INTO PaymentAdvancedInfo VALUES (?,?,?,?)";
		return dbHandler.insertPaymentAdvancedInfo(model, query);
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
	public Object[] project(PaymentAdvancedInfoModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (model.getOrderID() != null)
			query.append(" order_id,");
		if (model.getPaymentPrice() != null)
			query.append(" payment_price,");
		if (!Objects.equals(model.getRestaurantAddress(), ""))
			query.append(" restaurant_address,");
		if (!Objects.equals(model.getDriverPhone(), ""))
			query.append(" driver_phone,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM PaymentAdvancedInfo " +
				"ORDER BY order_id");

		return dbHandler.projectPaymentAdvancedInfo(model, query.toString());
	}
}

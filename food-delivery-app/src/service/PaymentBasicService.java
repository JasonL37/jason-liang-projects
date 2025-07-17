package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.PaymentBasicInfoModel;

import java.util.Objects;

/**
 * This is the service class that will implement the paymentBasicInfo operation.
 */
public class PaymentBasicService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from PaymentBasicInfo Service Layer";

	public PaymentBasicService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which paymentBasicInfo operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((PaymentBasicInfoModel) model);
			case "projection":
				return project((PaymentBasicInfoModel) model);
			case "aggregationGroupBy":
				return aggregationGroupBy();
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "PaymentBasicInfo operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new PaymentBasicInfo,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(PaymentBasicInfoModel model) {
		String query = "INSERT INTO PaymentBasicInfo VALUES (?,?,?)";
		return dbHandler.insertPaymentBasicInfo(model, query);
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
	public Object[] project(PaymentBasicInfoModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (model.getPaymentID() != null)
			query.append(" payment_id,");
		if (!Objects.equals(model.getPaymentStatus(), ""))
			query.append(" payment_status,");
		if (model.getOrderID() != null)
			query.append(" order_id,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM PaymentBasicInfo " +
				"ORDER BY payment_id");

		return dbHandler.projectPaymentBasicInfo(model, query.toString());
	}

	/**
	 * Creates the query for AGGREGATION GROUP BY operation:
	 * find how many success payments made
	 */
	public Object[] aggregationGroupBy() {
		String query = "SELECT payment_status, COUNT(payment_id) AS number_of_payments " +
				"FROM PaymentBasicInfo " +
				"GROUP BY payment_status";
		return dbHandler.aggregationGroupByPaymentBasicInfo(query);
	}


}

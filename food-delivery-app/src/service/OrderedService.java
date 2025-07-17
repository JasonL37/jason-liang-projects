package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.OrderedModel;

import java.util.Objects;

/**
 * This is the service class that will implement the Ordered operation.
 */
public class OrderedService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from Ordered Service Layer";

	public OrderedService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which Ordered operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((OrderedModel) model);
			case "projection":
				return project((OrderedModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "Ordered operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new Ordered (adding food to a certain order),
	 * and then implement the connection to the DOA layer.
	 */
	public String[] insert(OrderedModel model) {
		String query = "INSERT INTO Ordered VALUES (?,?,?)";
		return dbHandler.insertOrdered(model, query);
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
	public Object[] project(OrderedModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getFoodName(), ""))
			query.append(" food_name,");
		if (model.getOrderID() != null)
			query.append(" order_id,");
		if (model.getOrderedQuantity() != null)
			query.append(" orderedQuantity,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM Ordered " +
				"ORDER BY order_id, food_name");

		return dbHandler.projectOrdered(model, query.toString());
	}
}

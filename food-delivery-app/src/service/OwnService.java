package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.OwnModel;

import java.util.Objects;

/**
 * This is the service class that will implement the Own operation.
 */
public class OwnService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from Own Service Layer";

	public OwnService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which Own operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((OwnModel) model);
			case "projection":
				return project((OwnModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "Own operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new Own (adding a menu to a certain restaurant),
	 * and then implement the connection to the DOA layer.
	 */
	public String[] insert(OwnModel model) {
		String query = "INSERT INTO Own VALUES (?,?)";
		return dbHandler.insertOwn(model, query);
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
	public Object[] project(OwnModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getRestaurantAddress(), ""))
			query.append(" restaurant_address,");
		if (model.getMenuID() != null)
			query.append(" menu_id,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM Own " +
				"ORDER BY restaurant_address, menu_id");

		return dbHandler.projectOwn(model, query.toString());
	}
}

package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.ContainModel;

import java.util.Objects;

/**
 * This is the service class that will implement the Contain operation.
 */
public class ContainService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from Contain Service Layer";

	public ContainService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which Contain operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((ContainModel) model);
			case "projection":
				return project((ContainModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "Contain operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new Contain (adding food to a certain menu),
	 * and then implement the connection to the DOA layer.
	 */
	public String[] insert(ContainModel model) {
		String query = "INSERT INTO Contain VALUES (?,?)";
		return dbHandler.insertContain(model, query);
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
	public Object[] project(ContainModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (model.getMenuID() != null)
			query.append(" menu_id,");
		if (!Objects.equals(model.getFoodName(), ""))
			query.append(" food_name,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM Contain " +
				"ORDER BY menu_id, food_name");

		return dbHandler.projectContain(model, query.toString());
	}
}

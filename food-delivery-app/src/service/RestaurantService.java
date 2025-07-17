package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.RestaurantsModel;

import java.util.Objects;


/**
 * This is the service class that will implement the restaurant operation.
 */
public class RestaurantService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from Restaurant Service Layer";

	public RestaurantService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which restaurant operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((RestaurantsModel) model);
			case "delete":
				return delete((RestaurantsModel) model);
			case "update":
				return update((RestaurantsModel) model);
			case "projection":
				return project((RestaurantsModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "Restaurant operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new restaurant,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(RestaurantsModel model) {
		String query = "INSERT INTO Restaurants VALUES (?,?,?,?)";
		return dbHandler.insertRestaurant(model, query);
    }

    /**
	 * Creates the query for deleting an existing restaurant,
	 * and then implement the connection to the DOA layer.
	 */ 
    public String[] delete(RestaurantsModel model) {
		String query = "DELETE FROM Restaurants WHERE restaurant_address = ?";
		return dbHandler.deleteRestaurant(model, query);
    }
    
    /**
	 * Creates the query for updating an existing restaurant,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] update(RestaurantsModel model) {

		StringBuilder query = new StringBuilder("UPDATE Restaurants SET");

		if (!Objects.equals(model.getRestaurantName(), ""))
			query.append(" restaurant_name = ?,");
		if (!Objects.equals(model.getRestaurantOpenHours(), ""))
			query.append(" restaurant_openHours = ?,");
		if (!Objects.equals(model.getRestaurantAccount(), ""))
			query.append(" restaurant_account = ?,");

		// Delete the last comma before WHERE
		query.deleteCharAt(query.length() - 1);
		query.append(" WHERE restaurant_address = ?");

		return dbHandler.updateRestaurant(model, query.toString());
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
	public Object[] project(RestaurantsModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getRestaurantAddress(), ""))
			query.append(" restaurant_address,");
		if (!Objects.equals(model.getRestaurantName(), ""))
			query.append(" restaurant_name,");
		if (!Objects.equals(model.getRestaurantOpenHours(), ""))
			query.append(" restaurant_openHours,");
		if (!Objects.equals(model.getRestaurantAccount(), ""))
			query.append(" restaurant_account,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM Restaurants " +
				"ORDER BY restaurant_address, restaurant_name");

		return dbHandler.projectRestaurant(model, query.toString());
	}

	/**
	 * Creates the query for showing the Restaurants table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public RestaurantsModel[] showTable() {
		String query = "SELECT * FROM Restaurants " +
				"ORDER BY restaurant_address, restaurant_name";
		return dbHandler.getRestaurantsTable(query);
	}

}

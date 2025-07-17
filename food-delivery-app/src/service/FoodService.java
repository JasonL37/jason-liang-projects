package service;

import database.AppDOA;
import model.FoodModel;


/**
 * This is the service class that will implement the customer operation.
 */
public class FoodService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from CustomerAccount Service Layer";

	public FoodService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Creates the query for showing the customer table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public FoodModel[] showTable() {
		String query = "SELECT * " +
				"FROM FoodBasicInfo FB, FoodAdvancedInfo FA " +
				"WHERE FB.food_type = FA.food_type " +
				"ORDER BY FB.food_type, FB.food_name";
		return dbHandler.getFoodTable(query);
	}

}

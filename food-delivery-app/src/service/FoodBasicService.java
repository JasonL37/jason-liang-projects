package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.FoodBasicInfoModel;

import java.util.Objects;

/**
 * This is the service class that will implement the foodBasicInfo operation.
 */
public class FoodBasicService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from FoodBasicInfo Service Layer";

	public FoodBasicService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which foodBasicInfo operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((FoodBasicInfoModel) model);
			case "delete":
				return delete((FoodBasicInfoModel) model);
			case "update":
				return update((FoodBasicInfoModel) model);
			case "projection":
				return project((FoodBasicInfoModel) model);
			case "nestedAggregation":
				return nestedAggregation();
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "FoodBasicInfo operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new foodBasicInfo,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(FoodBasicInfoModel model) {
		String query = "INSERT INTO FoodBasicInfo VALUES (?,?,?)";
		return dbHandler.insertFoodBasicInfo(model, query);
    }

    /**
	 * Creates the query for deleting an existing foodBasicInfo,
	 * and then implement the connection to the DOA layer.
	 */ 
    public String[] delete(FoodBasicInfoModel model) {
		String query = "DELETE FROM FoodBasicInfo WHERE food_name = ?";
		return dbHandler.deleteFoodBasicInfo(model, query);
    }
    
    /**
	 * Creates the query for updating an existing foodBasicInfo,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] update(FoodBasicInfoModel model) {
		StringBuilder query = new StringBuilder("UPDATE FoodBasicInfo SET");

		if (model.getFoodPrice() != null)
			query.append(" food_price = ?,");
		if (!Objects.equals(model.getFoodType(), ""))
			query.append(" food_type = ?,");

		// Delete the last comma before WHERE
		query.deleteCharAt(query.length() - 1);
		query.append(" WHERE food_name = ?");

		return dbHandler.updateFoodBasicInfo(model, query.toString());
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
	public Object[] project(FoodBasicInfoModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getFoodName(), ""))
			query.append(" food_name,");
		if (model.getFoodPrice() != null)
			query.append(" food_price,");
		if (!Objects.equals(model.getFoodType(), ""))
			query.append(" food_type,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM FoodBasicInfo " +
				"ORDER BY food_type, food_name");

		return dbHandler.projectFoodBasicInfo(model, query.toString());
	}

	/**
	 * Creates the query for AGGREGATION HAVING operation:
	 * find the cheapest food price for popular food types with large order.
	 */
	public Object[] nestedAggregation() {
		String query = "SELECT FB.food_type, MIN(FB.food_price) AS lowest_food_price " +
				"FROM FoodBasicInfo FB " +
				"WHERE FB.food_name IN (SELECT DISTINCT O.food_name " +
					"FROM Ordered O " +
					"WHERE O.orderedQuantity > 5) " +
				"GROUP BY FB.food_type " +
				"ORDER BY FB.food_type";
		return dbHandler.nestedAggregationFoodBasicInfo(query);
	}

}

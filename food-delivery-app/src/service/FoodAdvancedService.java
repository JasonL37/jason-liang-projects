package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.FoodAdvancedInfoModel;

import java.util.Objects;


/**
 * This is the service class that will implement the FoodAdvancedInfo operation.
 */
public class FoodAdvancedService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from FoodAdvancedInfo Service Layer";

	public FoodAdvancedService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which FoodAdvancedInfo operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((FoodAdvancedInfoModel) model);
			case "delete":
				return delete((FoodAdvancedInfoModel) model);
			case "update":
				return update((FoodAdvancedInfoModel) model);
			case "projection":
				return project((FoodAdvancedInfoModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "FoodAdvancedInfo operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new FoodAdvancedInfo,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(FoodAdvancedInfoModel model) {
		String query = "INSERT INTO FoodAdvancedInfo VALUES (?,?)";
		return dbHandler.insertFoodAdvancedInfo(model, query);
    }

    /**
	 * Creates the query for deleting an existing FoodAdvancedInfo,
	 * and then implement the connection to the DOA layer.
	 */ 
    public String[] delete(FoodAdvancedInfoModel model) {
		String query = "DELETE FROM FoodAdvancedInfo WHERE food_type = ?";
		return dbHandler.deleteFoodAdvancedInfo(model, query);
    }
    
    /**
	 * Creates the query for updating an existing FoodAdvancedInfo,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] update(FoodAdvancedInfoModel model) {
		StringBuilder query = new StringBuilder("UPDATE FoodAdvancedInfo SET");

		if (!Objects.equals(model.getFoodHasVeganDiet(), ""))
			query.append(" food_hasVeganDiet = ?,");

		// Delete the last comma before WHERE
		query.deleteCharAt(query.length() - 1);
		query.append(" WHERE food_type = ?");

		return dbHandler.updateFoodAdvancedInfo(model, query.toString());
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
	public Object[] project(FoodAdvancedInfoModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getFoodType(), ""))
			query.append(" food_type,");
		if (!Objects.equals(model.getFoodHasVeganDiet(), ""))
			query.append(" food_hasVeganDiet,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM FoodAdvancedInfo " +
				"ORDER BY food_type");

		return dbHandler.projectFoodAdvancedInfo(model, query.toString());
	}

}

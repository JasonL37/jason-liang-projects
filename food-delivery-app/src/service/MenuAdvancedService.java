package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.MenuAdvancedInfoModel;

import java.util.Objects;


/**
 * This is the service class that will implement the MenuAdvancedInfo operation.
 */
public class MenuAdvancedService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from MenuAdvancedInfo Service Layer";

	public MenuAdvancedService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which MenuAdvancedInfo operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((MenuAdvancedInfoModel) model);
			case "delete":
				return delete((MenuAdvancedInfoModel) model);
			case "projection":
				return project((MenuAdvancedInfoModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "MenuAdvancedInfo operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new MenuAdvancedInfo,
	 * and then implement the connection to the DOA layer.
	 */
	public String[] insert(MenuAdvancedInfoModel model) {
		String query = "INSERT INTO MenuAdvancedInfo VALUES (?,?)";
		return dbHandler.insertMenuAdvancedInfo(model, query);
	}

	/**
	 * Creates the query for deleting an existing MenuAdvancedInfo,
	 * and then implement the connection to the DOA layer.
	 */
	public String[] delete(MenuAdvancedInfoModel model) {
		String query = "DELETE FROM MenuAdvancedInfo WHERE menu_name = ?";
		return dbHandler.deleteMenuAdvancedInfo(model, query);
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
	public Object[] project(MenuAdvancedInfoModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getMenuName(), ""))
			query.append(" menu_name,");
		if (!Objects.equals(model.getMenuCuisine(), ""))
			query.append(" menu_cuisine,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM MenuAdvancedInfo " +
				"ORDER BY menu_name");

		return dbHandler.projectMenuAdvancedInfo(model, query.toString());
	}

}

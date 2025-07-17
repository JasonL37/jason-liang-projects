package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.MenuBasicInfoModel;

import java.util.Objects;


/**
 * This is the service class that will implement the MenuBasicInfo operation.
 */
public class MenuBasicService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from MenuBasicInfo Service Layer";

	public MenuBasicService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which MenuBasicInfo operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((MenuBasicInfoModel) model);
			case "delete":
				return delete((MenuBasicInfoModel) model);
			case "projection":
				return project((MenuBasicInfoModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "MenuBasicInfo operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new MenuBasicInfo,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(MenuBasicInfoModel model) {
		String query = "INSERT INTO MenuBasicInfo VALUES (?,?)";
		return dbHandler.insertMenuBasicInfo(model, query);
    }

    /**
	 * Creates the query for deleting an existing MenuBasicInfo,
	 * and then implement the connection to the DOA layer.
	 */ 
    public String[] delete(MenuBasicInfoModel model) {
		String query = "DELETE FROM MenuBasicInfo WHERE menu_id = ?";
		return dbHandler.deleteMenuBasicInfo(model, query);
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
	public Object[] project(MenuBasicInfoModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (model.getMenuID() != null)
			query.append(" menu_id,");
		if (!Objects.equals(model.getMenuName(), ""))
			query.append(" menu_name,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM MenuBasicInfo " +
				"ORDER BY menu_id");

		return dbHandler.projectMenuBasicInfo(model, query.toString());
	}

}

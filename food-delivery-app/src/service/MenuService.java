package service;

import database.AppDOA;
import model.MenuModel;

import java.util.Objects;


/**
 * This is the service class that will implement the customer operation.
 */
public class MenuService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from CustomerAccount Service Layer";

	public MenuService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Creates the query for showing the customer table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public MenuModel[] showTable() {
		String query = "SELECT * " +
				"FROM MenuBasicInfo MB, MenuAdvancedInfo MA " +
				"WHERE MB.menu_name = MA.menu_name " +
				"ORDER BY MB.menu_id";
		return dbHandler.getMenuTable(query);
	}

}

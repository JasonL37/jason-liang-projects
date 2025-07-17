package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.DriversModel;

import java.util.Objects;


/**
 * This is the service class that will implement the driver operation.
 */
public class DriverService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from Drivers Service Layer";

	public DriverService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which driver operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((DriversModel) model);
			case "delete":
				return delete((DriversModel) model);
			case "update":
				return update((DriversModel) model);
			case "projection":
				return project((DriversModel) model);
			case "selection":
				return select((DriversModel) model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "Driver operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new driver,
	 * and then implement the connection to the DOA layer.
	 */
	public String[] insert(DriversModel model) {
		String query = "INSERT INTO Drivers VALUES (?,?,?,?)";
		return dbHandler.insertDriver(model, query);
	}

	/**
	 * Creates the query for deleting an existing driver,
	 * and then implement the connection to the DOA layer.
	 */
	public String[] delete(DriversModel model) {
		String query = "DELETE FROM Drivers WHERE driver_phone = ?";
		return dbHandler.deleteDriver(model, query);
	}

	/**
	 * Creates the query for updating an existing driver,
	 * and then implement the connection to the DOA layer.
	 */
	public String[] update(DriversModel model) {

		StringBuilder query = new StringBuilder("UPDATE Drivers SET");

		if (!Objects.equals(model.getDriverName(), ""))
			query.append(" driver_name = ?,");
		if (!Objects.equals(model.getDriverAccount(), ""))
			query.append(" driver_account = ?,");
		if (!Objects.equals(model.getDriverVehicle(), ""))
			query.append(" driver_vehicle = ?,");

		// Delete the last comma before WHERE
		query.deleteCharAt(query.length() - 1);
		query.append(" WHERE driver_phone = ?");

		return dbHandler.updateDriver(model, query.toString());
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
	public Object[] project(DriversModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getDriverPhone(), ""))
			query.append(" driver_phone,");
		if (!Objects.equals(model.getDriverName(), ""))
			query.append(" driver_name,");
		if (!Objects.equals(model.getDriverAccount(), ""))
			query.append(" driver_account,");
		if (!Objects.equals(model.getDriverVehicle(), ""))
			query.append(" driver_vehicle,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM Drivers " +
				"ORDER BY driver_name, driver_phone");

		return dbHandler.projectDriver(model, query.toString());
	}


	/*
	 * Creates the query for specifying filtering conditions for drivers
	 */
	public Object[] select(DriversModel model) {
		String query = "SELECT * " +
				"FROM Drivers " +
				"WHERE " + model.getDriverName() +
				" ORDER BY driver_name, driver_phone";
		return dbHandler.selectDriver(query);
	}

	/**
	 * Creates the query for showing the Drivers table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public DriversModel[] showTable() {
		String query = "SELECT * FROM Drivers " +
				"ORDER BY driver_name, driver_phone";
		return dbHandler.getDriversTable(query);
	}

}

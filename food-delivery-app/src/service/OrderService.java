package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.DriversModel;
import model.OrdersModel;

import java.util.Objects;

/**
 * This is the service class that will implement the order operation.
 */
public class OrderService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from Order Service Layer";

	public OrderService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which order operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((OrdersModel) model);
			case "delete":
				return delete((OrdersModel) model);
			case "projection":
				return project((OrdersModel) model);
			case "join":
				return join();
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "Order operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new order,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(OrdersModel model) {
		String query = "INSERT INTO Orders VALUES (?,?,?,?,?,?)";
		return dbHandler.insertOrder(model, query);
    }

    /**
	 * Creates the query for deleting an existing order,
	 * and then implement the connection to the DOA layer.
	 */ 
    public String[] delete(OrdersModel model) {
		String query = "DELETE FROM Orders WHERE order_id = ?";
		return dbHandler.deleteOrder(model, query);
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
	public Object[] project(OrdersModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getOrderID(), ""))
			query.append(" order_id,");
		if (!Objects.equals(model.getTotalPrice(), ""))
			query.append(" order_totalPrice,");
		if (!Objects.equals(model.getTimeArrival(), ""))
			query.append(" order_timeArrival,");
		if (!Objects.equals(model.getCustomerPhone(), ""))
			query.append(" customer_phone,");
		if (!Objects.equals(model.getRestaurantAddress(), ""))
			query.append(" restaurant_address,");
		if (!Objects.equals(model.getDriverPhone(), ""))
			query.append(" driver_phone,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM Orders " +
				"ORDER BY order_id");

		return dbHandler.projectOrder(model, query.toString());
	}

	/**
	 * Creates the query for JOIN operation:
	 * find the information of drivers with expensive orders
	 */
	public DriversModel[] join() {
		String query = "SELECT DISTINCT D.driver_phone, D.driver_name, D.driver_account, D.driver_vehicle " +
				"FROM Orders O, Drivers D " +
				"WHERE O.driver_phone = D.driver_phone AND O.order_totalPrice > 50.00 " +
				"ORDER BY D.driver_name, D.driver_phone";
		return dbHandler.joinOrder(query);
	}

	/**
	 * Creates the query for showing the Orders table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public OrdersModel[] showTable() {
		String query = "SELECT * FROM Orders " +
				"ORDER BY order_id";
		return dbHandler.getOrdersTable(query);
	}

}

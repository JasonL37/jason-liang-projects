package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;
import model.CustomersModel;

import java.util.Objects;


/**
 * This is the service class that will implement the customer operation.
 */
public class CustomerAccountService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from CustomerAccount Service Layer";

	public CustomerAccountService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Handles which customer operation to access determined by the pass values from the main service layer.
	 */
	public Object[] operationHandler(String mode, ServiceModelDelegate model) {
		switch (mode) {
			case "insert":
				return insert((CustomersModel) model);
			case "delete":
				return delete((CustomersModel) model);
			case "update":
				return update((CustomersModel) model);
			case "projection":
				return project((CustomersModel) model);
			case "division":
				return division();
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "Customer operation selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
	}

	/**
	 * Creates the query for inserting a new customer,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] insert(CustomersModel model) {
		String query = "INSERT INTO Customers VALUES (?,?,?,?,?,?,?,?,?)";
		return dbHandler.insertCustomer(model, query);
    }

    /**
	 * Creates the query for deleting an existing customer,
	 * and then implement the connection to the DOA layer.
	 */ 
    public String[] delete(CustomersModel model) {
		String query = "DELETE FROM Customers WHERE customer_phone = ?";
		return dbHandler.deleteCustomer(model, query);
    }
    
    /**
	 * Creates the query for updating an existing customer,
	 * and then implement the connection to the DOA layer.
	 */
    public String[] update(CustomersModel model) {
		StringBuilder query = new StringBuilder("UPDATE Customers SET");

		if (!Objects.equals(model.getCustomerName(), ""))
			query.append(" customer_name = ?,");
		if (!Objects.equals(model.getCustomerAddress(), ""))
			query.append(" customer_address = ?,");
		if (!Objects.equals(model.getCustomerEmail(), ""))
			query.append(" customer_email = ?,");
		if (!Objects.equals(model.getCustomerAccount(), ""))
			query.append(" customer_account = ?,");
		if (!Objects.equals(model.getCustomerPaymentInfo(), ""))
			query.append(" customer_paymentInfo = ?,");
		if (!Objects.equals(model.getCustomerMembership(), ""))
			query.append(" customer_membership = ?,");
		if (model.getCustomerPoints() != null)
			query.append(" customer_points = ?,");
		if (!Objects.equals(model.getCustomerDiscount(), ""))
			query.append(" customer_discount = ?,");

		// Delete the last comma before WHERE
		query.deleteCharAt(query.length() - 1);
		query.append(" WHERE customer_phone = ?");

		return dbHandler.updateCustomer(model, query.toString());
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
	public Object[] project(CustomersModel model) {

		StringBuilder query = new StringBuilder("SELECT");

		if (!Objects.equals(model.getCustomerPhone(), ""))
			query.append(" customer_phone,");
		if (!Objects.equals(model.getCustomerName(), ""))
			query.append(" customer_name,");
		if (!Objects.equals(model.getCustomerAddress(), ""))
			query.append(" customer_address,");
		if (!Objects.equals(model.getCustomerEmail(), ""))
			query.append(" customer_email,");
		if (!Objects.equals(model.getCustomerAccount(), ""))
			query.append(" customer_account,");
		if (!Objects.equals(model.getCustomerPaymentInfo(), ""))
			query.append(" customer_paymentInfo,");
		if (!Objects.equals(model.getCustomerMembership(), ""))
			query.append(" customer_membership,");
		if (model.getCustomerPoints() != null)
			query.append(" customer_points,");
		if (!Objects.equals(model.getCustomerDiscount(), ""))
			query.append(" customer_discount,");

		// Delete the last comma before FROM
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM Customers " +
				"ORDER BY customer_name, customer_phone");

		return dbHandler.projectCustomer(model, query.toString());
	}

	/**
	 * Creates the query for DIVISION operation:
	 * find the customers that ordered all the restaurants.
	 */
	public Object[] division() {
		String query = "SELECT C.customer_phone, C.customer_name " +
				"FROM Customers C " +
				"WHERE NOT EXISTS (SELECT R.restaurant_address " +
					"FROM Restaurants R " +
					"WHERE NOT EXISTS (SELECT O.customer_phone " +
						"FROM Orders O " +
						"WHERE R.restaurant_address = O.restaurant_address AND O.customer_phone = C.customer_phone)) " +
				"ORDER BY C.customer_name, C.customer_phone";
		return dbHandler.divisionCustomer(query);
	}

	/**
	 * Creates the query for showing the Customers table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public CustomersModel[] showTable() {
		String query = "SELECT * FROM Customers " +
				"ORDER BY customer_name, customer_phone";
		return dbHandler.getCustomersTable(query);
	}

}

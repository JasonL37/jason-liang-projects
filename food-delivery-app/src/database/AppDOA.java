package database;

import model.*;
import util.PrintablePreparedStatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class handles all database related transactions
 */
public class AppDOA {
	// Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
	// Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final String SUCCESS_MESSAGE = "Operation Success";
	private static final String FAIL_MESSAGE = "Operation Failed - ";

	private Connection connection = null;

	public AppDOA() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	/**
	 * This is the login function implementation,
	 * receives username and password from the service layer, then have connection with the database.
	 * Returns true if connection success; false if connection fails.
	 */
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}

			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);

			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	/**
	 * Function for closing the connection when quiting the application.
	 */
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}



	// CUSTOMER SERVICE //

	/**
	 * Function for the implementation of customer insert query,
	 * connects the database and insert a new customer.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertCustomer(CustomersModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (!Objects.equals(model.getCustomerPhone(), ""))
				ps.setString(1, model.getCustomerPhone());
			else
				ps.setNull(1, Types.VARCHAR);

			if (!Objects.equals(model.getCustomerName(), ""))
				ps.setString(2, model.getCustomerName());
			else
				ps.setNull(2, Types.VARCHAR);

			if (!Objects.equals(model.getCustomerAddress(), ""))
				ps.setString(3, model.getCustomerAddress());
			else
				ps.setNull(3, Types.VARCHAR);

			if (!Objects.equals(model.getCustomerEmail(), ""))
				ps.setString(4, model.getCustomerEmail());
			else
				ps.setNull(4, Types.VARCHAR);

			if (!Objects.equals(model.getCustomerAccount(), ""))
				ps.setString(5, model.getCustomerAccount());
			else
				ps.setNull(5, Types.VARCHAR);

			if (!Objects.equals(model.getCustomerPaymentInfo(), ""))
				ps.setString(6, model.getCustomerPaymentInfo());
			else
				ps.setNull(6, Types.VARCHAR);

			// Membership has default value
			if (Objects.equals(model.getCustomerMembership(), ""))
				ps.setString(7, "No");
			else
				ps.setString(7, model.getCustomerMembership());

			if (model.getCustomerPoints() != null)
				ps.setInt(8, model.getCustomerPoints());
			else
				ps.setNull(8, Types.INTEGER);

			if (!Objects.equals(model.getCustomerDiscount(), ""))
				ps.setString(9, model.getCustomerDiscount());
			else
				ps.setNull(9, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of customer delete query,
	 * connects the database and delete an existing customer.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteCustomer(CustomersModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, model.getCustomerPhone());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " Customer with Customer Phone " + model.getCustomerPhone() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of customer update query,
	 * connects the database and delete an existing customer.
	 * Returns a message for successful or failed operation.
	 */
	public String[] updateCustomer(CustomersModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			int parameterIndex = 1;

			if (!Objects.equals(model.getCustomerName(), "")) {
				ps.setString(parameterIndex, model.getCustomerName());
				parameterIndex++;
			}

			if (!Objects.equals(model.getCustomerAddress(), "")) {
				ps.setString(parameterIndex, model.getCustomerAddress());
				parameterIndex++;
			}

			if (!Objects.equals(model.getCustomerEmail(), "")) {
				ps.setString(parameterIndex, model.getCustomerEmail());
				parameterIndex++;
			}

			if (!Objects.equals(model.getCustomerAccount(), "")) {
				ps.setString(parameterIndex, model.getCustomerAccount());
				parameterIndex++;
			}

			if (!Objects.equals(model.getCustomerPaymentInfo(), "")) {
				ps.setString(parameterIndex, model.getCustomerPaymentInfo());
				parameterIndex++;
			}

			if (!Objects.equals(model.getCustomerMembership(), "")) {
				ps.setString(parameterIndex, model.getCustomerMembership());
				parameterIndex++;
			}

			if (model.getCustomerPoints() != null) {
				ps.setInt(parameterIndex, model.getCustomerPoints());
				parameterIndex++;
			}

			if (!Objects.equals(model.getCustomerDiscount(), "")) {
				ps.setString(parameterIndex, model.getCustomerDiscount());
				parameterIndex++;
			}

			if (!Objects.equals(model.getCustomerPhone(), "")) {
				ps.setString(parameterIndex, model.getCustomerPhone());
			}

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " Customer with Customer Phone " + model.getCustomerPhone() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectCustomer(CustomersModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getCustomerPhone(), ""))
					tuple.add(rs.getString("customer_phone"));
				if (!Objects.equals(model.getCustomerName(), ""))
					tuple.add(rs.getString("customer_name"));
				if (!Objects.equals(model.getCustomerAddress(), ""))
					tuple.add(rs.getString("customer_address"));
				if (!Objects.equals(model.getCustomerEmail(), ""))
					tuple.add(rs.getString("customer_email"));
				if (!Objects.equals(model.getCustomerAccount(), ""))
					tuple.add(rs.getString("customer_account"));
				if (!Objects.equals(model.getCustomerPaymentInfo(), ""))
					tuple.add(rs.getString("customer_paymentInfo"));
				if (!Objects.equals(model.getCustomerMembership(), ""))
					tuple.add(rs.getString("customer_membership"));
				if (model.getCustomerPoints() != null)
					tuple.add(rs.getInt("customer_points"));
				if (!Objects.equals(model.getCustomerDiscount(), ""))
					tuple.add(rs.getString("customer_discount"));
				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}

	/**
	 * Function for the implementation of customer DIVISION query,
	 * Returns an array of object arrays, each object array contains customer_phone as first element,
	 * customer_name as second element.
	 */
	public Object[] divisionCustomer(String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();
				tuple.add(rs.getString("customer_phone"));
				tuple.add(rs.getString("customer_name"));
				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// DRIVER SERVICE //

	/**
	 * Function for the implementation of driver insert query,
	 * connects the database and insert a new driver.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertDriver(DriversModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (!Objects.equals(model.getDriverPhone(), ""))
				ps.setString(1, model.getDriverPhone());
			else
				ps.setNull(1, Types.VARCHAR);

			if (!Objects.equals(model.getDriverName(), ""))
				ps.setString(2, model.getDriverName());
			else
				ps.setNull(2, Types.VARCHAR);

			if (!Objects.equals(model.getDriverAccount(), ""))
				ps.setString(3, model.getDriverAccount());
			else
				ps.setNull(3, Types.VARCHAR);

			if (!Objects.equals(model.getDriverVehicle(), ""))
				ps.setString(4, model.getDriverVehicle());
			else
				ps.setNull(4, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of driver delete query,
	 * connects the database and delete an existing driver.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteDriver(DriversModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, model.getDriverPhone());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " Driver with Driver Phone " + model.getDriverPhone() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of driver update query,
	 * connects the database and delete an existing driver.
	 * Returns a message for successful or failed operation.
	 */
	public String[] updateDriver(DriversModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			int parameterIndex = 1;

			if (!Objects.equals(model.getDriverName(), "")) {
				ps.setString(parameterIndex, model.getDriverName());
				parameterIndex++;
			}

			if (!Objects.equals(model.getDriverAccount(), "")) {
				ps.setString(parameterIndex, model.getDriverAccount());
				parameterIndex++;
			}

			if (!Objects.equals(model.getDriverVehicle(), "")) {
				ps.setString(parameterIndex, model.getDriverVehicle());
				parameterIndex++;
			}

			if (!Objects.equals(model.getDriverPhone(), "")) {
				ps.setString(parameterIndex, model.getDriverPhone());
			}

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " Driver with Driver Phone " + model.getDriverPhone() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectDriver(DriversModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getDriverPhone(), ""))
					tuple.add(rs.getString("driver_phone"));
				if (!Objects.equals(model.getDriverName(), ""))
					tuple.add(rs.getString("driver_name"));
				if (!Objects.equals(model.getDriverAccount(), ""))
					tuple.add(rs.getString("driver_account"));
				if (!Objects.equals(model.getDriverVehicle(), ""))
					tuple.add(rs.getString("driver_vehicle"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}

	/**
	 * Function for the implementation of the SELECTION query
	 */
	public Object[] selectDriver(String query) {
		ArrayList<Object> resultList = new ArrayList<>();
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();
				tuple.add(rs.getString("driver_phone"));
				tuple.add(rs.getString("driver_name"));
				tuple.add(rs.getString("driver_account"));
				tuple.add(rs.getString("driver_vehicle"));
				resultList.add(tuple.toArray(new Object[tuple.size()]));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return resultList.toArray(new Object[resultList.size()]);
	}



	// RESTAURANT SERVICE //

	/**
	 * Function for the implementation of restaurant insert query,
	 * connects the database and insert a new restaurant.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertRestaurant(RestaurantsModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (!Objects.equals(model.getRestaurantAddress(), ""))
				ps.setString(1, model.getRestaurantAddress());
			else
				ps.setNull(1, Types.VARCHAR);

			if (!Objects.equals(model.getRestaurantName(), ""))
				ps.setString(2, model.getRestaurantName());
			else
				ps.setNull(2, Types.VARCHAR);

			if (!Objects.equals(model.getRestaurantOpenHours(), ""))
				ps.setString(3, model.getRestaurantOpenHours());
			else
				ps.setNull(3, Types.VARCHAR);

			if (!Objects.equals(model.getRestaurantAccount(), ""))
				ps.setString(4, model.getRestaurantAccount());
			else
				ps.setNull(4, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of restaurant delete query,
	 * connects the database and delete an existing restaurant.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteRestaurant(RestaurantsModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, model.getRestaurantAddress());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " Restaurant with Restaurant Address " + model.getRestaurantAddress() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of restaurant update query,
	 * connects the database and delete an existing restaurant.
	 * Returns a message for successful or failed operation.
	 */
	public String[] updateRestaurant(RestaurantsModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			int parameterIndex = 1;

			if (!Objects.equals(model.getRestaurantName(), "")) {
				ps.setString(parameterIndex, model.getRestaurantName());
				parameterIndex++;
			}

			if (!Objects.equals(model.getRestaurantOpenHours(), "")) {
				ps.setString(parameterIndex, model.getRestaurantOpenHours());
				parameterIndex++;
			}

			if (!Objects.equals(model.getRestaurantAccount(), "")) {
				ps.setString(parameterIndex, model.getRestaurantAccount());
				parameterIndex++;
			}

			if (!Objects.equals(model.getRestaurantAddress(), "")) {
				ps.setString(parameterIndex, model.getRestaurantAddress());
			}

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " Restaurant with Restaurant Address " + model.getRestaurantAddress() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectRestaurant(RestaurantsModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getRestaurantAddress(), ""))
					tuple.add(rs.getString("restaurant_address"));
				if (!Objects.equals(model.getRestaurantName(), ""))
					tuple.add(rs.getString("restaurant_name"));
				if (!Objects.equals(model.getRestaurantOpenHours(), ""))
					tuple.add(rs.getString("restaurant_openHours"));
				if (!Objects.equals(model.getRestaurantAccount(), ""))
					tuple.add(rs.getString("restaurant_account"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// Food_Basic_Info SERVICE //

	/**
	 * Function for the implementation of FoodBasicInfo insert query,
	 * connects the database and insert a new FoodBasicInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertFoodBasicInfo(FoodBasicInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (!Objects.equals(model.getFoodName(), ""))
				ps.setString(1, model.getFoodName());
			else
				ps.setNull(1, Types.VARCHAR);

			if (model.getFoodPrice() != null)
				ps.setDouble(2, model.getFoodPrice());
			else
				ps.setNull(2, Types.DOUBLE);

			if (!Objects.equals(model.getFoodType(), ""))
				ps.setString(3, model.getFoodType());
			else
				ps.setNull(3, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of FoodBasicInfo delete query,
	 * connects the database and delete an existing FoodBasicInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteFoodBasicInfo(FoodBasicInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, model.getFoodName());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " FoodBasicInfo with Food Name " + model.getFoodName() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of FoodBasicInfo update query,
	 * connects the database and delete an existing FoodBasicInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] updateFoodBasicInfo(FoodBasicInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			int parameterIndex = 1;

			if (model.getFoodPrice() != null) {
				ps.setDouble(parameterIndex, model.getFoodPrice());
				parameterIndex++;
			}

			if (!Objects.equals(model.getFoodType(), "")) {
				ps.setString(parameterIndex, model.getFoodType());
				parameterIndex++;
			}

			if (!Objects.equals(model.getFoodName(), "")) {
				ps.setString(parameterIndex, model.getFoodName());
			}

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " FoodBasicInfo with Food Name " + model.getFoodName() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectFoodBasicInfo(FoodBasicInfoModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getFoodName(), ""))
					tuple.add(rs.getString("food_name"));
				if (model.getFoodPrice() != null)
					tuple.add(rs.getDouble("food_price"));
				if (!Objects.equals(model.getFoodType(), ""))
					tuple.add(rs.getString("food_type"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}

	/**
	 * Function for the implementation of FoodReviews NESTED AGGREGATION query,
	 * Returns an array of object arrays, each object array contains restaurant_address as first element,
	 * highest_food_quality_rating as second element.
	 */
	public Object[] nestedAggregationFoodBasicInfo(String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();
				tuple.add(rs.getString("food_type"));
				tuple.add(rs.getDouble("lowest_food_price"));
				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// Food_Advanced_Info SERVICE //

	/**
	 * Function for the implementation of FoodAdvancedInfo insert query,
	 * connects the database and insert a new FoodAdvancedInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertFoodAdvancedInfo(FoodAdvancedInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (!Objects.equals(model.getFoodType(), ""))
				ps.setString(1, model.getFoodType());
			else
				ps.setNull(1, Types.VARCHAR);

			if (!Objects.equals(model.getFoodHasVeganDiet(), ""))
				ps.setString(2, model.getFoodHasVeganDiet());
			else
				ps.setNull(2, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of FoodAdvancedInfo delete query,
	 * connects the database and delete an existing FoodAdvancedInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteFoodAdvancedInfo(FoodAdvancedInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, model.getFoodType());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " FoodAdvancedInfo with Food Type " + model.getFoodType() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of FoodAdvancedInfo update query,
	 * connects the database and delete an existing FoodAdvancedInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] updateFoodAdvancedInfo(FoodAdvancedInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			int parameterIndex = 1;

			if (!Objects.equals(model.getFoodType(), "")) {
				ps.setString(parameterIndex, model.getFoodType());
				parameterIndex++;
			}

			if (!Objects.equals(model.getFoodHasVeganDiet(), "")) {
				ps.setString(parameterIndex, model.getFoodHasVeganDiet());
			}

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " FoodAdvancedInfo with Food Type " + model.getFoodType() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectFoodAdvancedInfo(FoodAdvancedInfoModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getFoodType(), ""))
					tuple.add(rs.getString("food_type"));
				if (!Objects.equals(model.getFoodHasVeganDiet(), ""))
					tuple.add(rs.getString("food_hasVeganDiet"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// Menu_Basic_Info SERVICE //

	/**
	 * Function for the implementation of MenuBasicInfo insert query,
	 * connects the database and insert a new MenuBasicInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertMenuBasicInfo(MenuBasicInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (model.getMenuID() != null)
				ps.setInt(1, model.getMenuID());
			else
				ps.setNull(1, Types.INTEGER);

			if (!Objects.equals(model.getMenuName(), ""))
				ps.setString(2, model.getMenuName());
			else
				ps.setNull(2, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}


	/**
	 * Function for the implementation of MenuBasicInfo delete query,
	 * connects the database and delete an existing MenuBasicInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteMenuBasicInfo(MenuBasicInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setInt(1, model.getMenuID());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " MenuBasicInfo with MenuID " + model.getMenuID() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectMenuBasicInfo(MenuBasicInfoModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (model.getMenuID() != null)
					tuple.add(rs.getInt("menu_id"));
				if (!Objects.equals(model.getMenuName(), ""))
					tuple.add(rs.getString("menu_name"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// Menu_Advanced_Info SERVICE //

	/**
	 * Function for the implementation of MenuAdvancedInfo insert query,
	 * connects the database and insert a new MenuAdvancedInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertMenuAdvancedInfo(MenuAdvancedInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (!Objects.equals(model.getMenuName(), ""))
				ps.setString(1, model.getMenuName());
			else
				ps.setNull(1, Types.VARCHAR);

			if (!Objects.equals(model.getMenuCuisine(), ""))
				ps.setString(2, model.getMenuCuisine());
			else
				ps.setNull(2, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of MenuAdvancedInfo delete query,
	 * connects the database and delete an existing MenuAdvancedInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteMenuAdvancedInfo(MenuAdvancedInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, model.getMenuName());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " MenuAdvancedInfo with Menu Name " + model.getMenuName() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectMenuAdvancedInfo(MenuAdvancedInfoModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getMenuName(), ""))
					tuple.add(rs.getString("menu_name"));
				if (!Objects.equals(model.getMenuCuisine(), ""))
					tuple.add(rs.getString("menu_cuisine"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}






	// ORDER SERVICE //

	/**
	 * Function for the implementation of order insert query,
	 * connects the database and insert a new order.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertOrder(OrdersModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (model.getOrderID() != null)
				ps.setInt(1, model.getOrderID());
			else
				ps.setNull(1, Types.INTEGER);

			if (model.getTotalPrice() != null)
				ps.setDouble(2, model.getTotalPrice());
			else
				ps.setNull(2, Types.DOUBLE);

			if (model.getTimeArrival() != null)
				ps.setTimestamp(3, model.getTimeArrival());
			else
				ps.setNull(3, Types.TIMESTAMP);

			if (!Objects.equals(model.getCustomerPhone(), ""))
				ps.setString(4, model.getCustomerPhone());
			else
				ps.setNull(4, Types.VARCHAR);

			if (!Objects.equals(model.getRestaurantAddress(), ""))
				ps.setString(5, model.getRestaurantAddress());
			else
				ps.setNull(5, Types.VARCHAR);

			if (!Objects.equals(model.getDriverPhone(), ""))
				ps.setString(6, model.getDriverPhone());
			else
				ps.setNull(6, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of order delete query,
	 * connects the database and delete an existing order.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteOrder(OrdersModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setInt(1, model.getOrderID());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " Order with OrderID " + model.getOrderID() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectOrder(OrdersModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (model.getOrderID() != null)
					tuple.add(rs.getInt("order_id"));
				if (model.getTotalPrice() != null)
					tuple.add(rs.getDouble("order_totalPrice"));
				if (model.getTimeArrival() != null)
					tuple.add(rs.getTimestamp("order_timeArrival"));
				if (!Objects.equals(model.getCustomerPhone(), ""))
					tuple.add(rs.getString("customer_phone"));
				if (!Objects.equals(model.getRestaurantAddress(), ""))
					tuple.add(rs.getString("restaurant_address"));
				if (!Objects.equals(model.getDriverPhone(), ""))
					tuple.add(rs.getString("driver_phone"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}

	/**
	 * Function for the implementation of order JOIN query,
	 * Returns the driver model with the all information of driver who handles the orderID selected by the user.
	 */
	public DriversModel[] joinOrder(String query) {
		ArrayList<DriversModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				DriversModel tuple = new DriversModel(rs.getString("driver_phone"),
						rs.getString("driver_name"),
						rs.getString("driver_account"),
						rs.getString("driver_vehicle"));
				result.add(tuple);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new DriversModel[result.size()]);
	}



	// PAYMENT_BASIC_INFO SERVICE //

	/**
	 * Function for the implementation of PaymentBasicInfo insert query,
	 * connects the database and insert a new PaymentBasicInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertPaymentBasicInfo(PaymentBasicInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (model.getPaymentID() != null)
				ps.setInt(1, model.getPaymentID());
			else
				ps.setNull(1, Types.INTEGER);

			if (!Objects.equals(model.getPaymentStatus(), ""))
				ps.setString(2, model.getPaymentStatus());
			else
				ps.setNull(2, Types.VARCHAR);

			if (model.getOrderID() != null)
				ps.setInt(3, model.getOrderID());
			else
				ps.setNull(3, Types.INTEGER);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectPaymentBasicInfo(PaymentBasicInfoModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (model.getPaymentID() != null)
					tuple.add(rs.getInt("payment_id"));
				if (!Objects.equals(model.getPaymentStatus(), ""))
					tuple.add(rs.getString("payment_status"));
				if (model.getOrderID() != null)
					tuple.add(rs.getInt("order_id"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}

	/**
	 * Function for the implementation of PaymentBasicInfo AGGREGATION GROUP BY query,
	 * Returns an array of object arrays, each object array contains payment_status as first element,
	 * number_of_successful_payments as second element.
	 */
	public Object[] aggregationGroupByPaymentBasicInfo(String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();
				tuple.add(rs.getString("payment_status"));
				tuple.add(rs.getInt("number_of_payments"));
				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// PAYMENT_ADVANCED_INFO SERVICE //

	/**
	 * Function for the implementation of PaymentAdvancedInfo insert query,
	 * connects the database and insert a new PaymentAdvancedInfo.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertPaymentAdvancedInfo(PaymentAdvancedInfoModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (model.getOrderID() != null)
				ps.setInt(1, model.getOrderID());
			else
				ps.setNull(1, Types.INTEGER);

			if (model.getPaymentPrice() != null)
				ps.setDouble(2, model.getPaymentPrice());
			else
				ps.setNull(2, Types.DOUBLE);

			if (!Objects.equals(model.getRestaurantAddress(), ""))
				ps.setString(3, model.getRestaurantAddress());
			else
				ps.setNull(3, Types.VARCHAR);

			if (!Objects.equals(model.getDriverPhone(), ""))
				ps.setString(4, model.getDriverPhone());
			else
				ps.setNull(4, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectPaymentAdvancedInfo(PaymentAdvancedInfoModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (model.getOrderID() != null)
					tuple.add(rs.getInt("order_id"));
				if (model.getPaymentPrice() != null)
					tuple.add(rs.getDouble("payment_price"));
				if (!Objects.equals(model.getRestaurantAddress(), ""))
					tuple.add(rs.getString("restaurant_address"));
				if (!Objects.equals(model.getDriverPhone(), ""))
					tuple.add(rs.getString("driver_phone"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}


	// REVIEW_BASIC_INFO SERVICE //

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectReviewBasicInfo(ReviewBasicInfoModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getCustomerPhone(), ""))
					tuple.add(rs.getString("customer_phone"));
				if (model.getReviewTimestamp() != null)
					tuple.add(rs.getTimestamp("review_timeStamp"));
				if (!Objects.equals(model.getReviewTitle(), ""))
					tuple.add(rs.getString("review_title"));
				if (!Objects.equals(model.getReviewImage(), ""))
					tuple.add(rs.getString("review_image"));
				if (!Objects.equals(model.getReviewComment(), ""))
					tuple.add(rs.getString("review_comment"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// REVIEW_ADVANCED_INFO SERVICE //

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectReviewAdvancedInfo(ReviewAdvancedInfoModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getReviewComment(), ""))
					tuple.add(rs.getString("review_comment"));
				if (!Objects.equals(model.getReviewRatingType(), ""))
					tuple.add(rs.getString("review_ratingType"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// FOOD_REVIEW SERVICE //

	/**
	 * Function for the implementation of FoodReview insert query,
	 * connects the database and insert a new FoodReview.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertFoodReview(FoodReviewsModel model, String parentAdvancedQuery, String parentBasicQuery, String childQuery) {

		try {
			// Insert into ReviewAdvancedInfo Table (Parent Table)
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(parentAdvancedQuery), parentAdvancedQuery, false);

			if (!Objects.equals(model.getReviewComment(), ""))
				ps.setString(1, model.getReviewComment());
			else
				ps.setNull(1, Types.VARCHAR);

			if (!Objects.equals(model.getReviewRatingType(), ""))
				ps.setString(2, model.getReviewRatingType());
			else
				ps.setNull(2, Types.VARCHAR);

			ps.executeUpdate();

			// Insert into ReviewBasicInfo Table (Parent Table)
			ps = new PrintablePreparedStatement(connection.prepareStatement(parentBasicQuery), parentBasicQuery, false);

			if (!Objects.equals(model.getCustomerPhone(), ""))
				ps.setString(1, model.getCustomerPhone());
			else
				ps.setNull(1, Types.VARCHAR);

			if (model.getReviewTimestamp() != null)
				ps.setTimestamp(2, model.getReviewTimestamp());
			else
				ps.setNull(2, Types.TIMESTAMP);

			if (!Objects.equals(model.getReviewTitle(), ""))
				ps.setString(3, model.getReviewTitle());
			else
				ps.setNull(3, Types.VARCHAR);

			if (!Objects.equals(model.getReviewImage(), ""))
				ps.setString(4, model.getReviewImage());
			else
				ps.setNull(4, Types.VARCHAR);

			if (!Objects.equals(model.getReviewComment(), ""))
				ps.setString(5, model.getReviewComment());
			else
				ps.setNull(5, Types.VARCHAR);

			ps.executeUpdate();

			// Insert into FoodReviews Table (Child Table)
			ps = new PrintablePreparedStatement(connection.prepareStatement(childQuery), childQuery, false);

			if (!Objects.equals(model.getCustomerPhone(), ""))
				ps.setString(1, model.getCustomerPhone());
			else
				ps.setNull(1, Types.VARCHAR);

			if (model.getReviewTimestamp() != null)
				ps.setTimestamp(2, model.getReviewTimestamp());
			else
				ps.setNull(2, Types.TIMESTAMP);

			if (!Objects.equals(model.getRestaurantAddress(), ""))
				ps.setString(3, model.getRestaurantAddress());
			else
				ps.setNull(3, Types.VARCHAR);

			if (model.getFoodReviewFoodQualityRating() != null)
				ps.setInt(4, model.getFoodReviewFoodQualityRating());
			else
				ps.setNull(4, Types.INTEGER);

			if (model.getFoodReviewPortionSizeRating() != null)
				ps.setInt(5, model.getFoodReviewPortionSizeRating());
			else
				ps.setNull(5, Types.INTEGER);

			ps.executeUpdate();

			/*
			 * To avoid partial operation done when error occurs
			 * Only commit after all three insert operations are completed,
			 * otherwise, rollback the whole operation.
			 */
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of FoodReview delete query,
	 * connects the database and delete an existing FoodReview.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteFoodReview(FoodReviewsModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, model.getCustomerPhone());
			ps.setTimestamp(2, model.getReviewTimestamp());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " FoodReview with customer_phone " + model.getCustomerPhone() +
						" and review_timeStamp " + model.getReviewTimestamp() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectFoodReviews(FoodReviewsModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getCustomerPhone(), ""))
					tuple.add(rs.getString("customer_phone"));
				if (model.getReviewTimestamp() != null)
					tuple.add(rs.getTimestamp("review_timeStamp"));
				if (!Objects.equals(model.getRestaurantAddress(), ""))
					tuple.add(rs.getString("restaurant_address"));
				if (model.getFoodReviewFoodQualityRating() != null)
					tuple.add(rs.getInt("foodReviews_foodQualityRating"));
				if (model.getFoodReviewPortionSizeRating() != null)
					tuple.add(rs.getInt("foodReviews_portionSizeRating"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}

	/**
	 * Function for the implementation of FoodReviews AGGREGATION HAVING query,
	 * Returns an array of object arrays, each object array contains restaurant_address as first element,
	 * highest_food_quality_rating as second element.
	 */
	public Object[] aggregationHavingFoodReviews(String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();
				tuple.add(rs.getString("restaurant_address"));
				tuple.add(rs.getInt("highest_food_quality_rating"));
				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// DRIVER_REVIEW SERVICE //

	/**
	 * Function for the implementation of DriverReview insert query,
	 * connects the database and insert a new DriverReview.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertDriverReview(DriverReviewsModel model, String parentAdvancedQuery, String parentBasicQuery, String childQuery) {
		try {
			// Insert into ReviewAdvancedInfo Table (Parent Table)
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(parentAdvancedQuery), parentAdvancedQuery, false);

			if (!Objects.equals(model.getReviewComment(), ""))
				ps.setString(1, model.getReviewComment());
			else
				ps.setNull(1, Types.VARCHAR);

			if (!Objects.equals(model.getReviewRatingType(), ""))
				ps.setString(2, model.getReviewRatingType());
			else
				ps.setNull(2, Types.VARCHAR);

			ps.executeUpdate();

			// Insert into ReviewBasicInfo Table (Parent Table)
			ps = new PrintablePreparedStatement(connection.prepareStatement(parentBasicQuery), parentBasicQuery, false);

			if (!Objects.equals(model.getCustomerPhone(), ""))
				ps.setString(1, model.getCustomerPhone());
			else
				ps.setNull(1, Types.VARCHAR);

			if (model.getReviewTimestamp() != null)
				ps.setTimestamp(2, model.getReviewTimestamp());
			else
				ps.setNull(2, Types.TIMESTAMP);

			if (!Objects.equals(model.getReviewTitle(), ""))
				ps.setString(3, model.getReviewTitle());
			else
				ps.setNull(3, Types.VARCHAR);

			if (!Objects.equals(model.getReviewImage(), ""))
				ps.setString(4, model.getReviewImage());
			else
				ps.setNull(4, Types.VARCHAR);

			if (!Objects.equals(model.getReviewComment(), ""))
				ps.setString(5, model.getReviewComment());
			else
				ps.setNull(5, Types.VARCHAR);

			ps.executeUpdate();

			// Insert into DriverReviews Table (Child Table)
			ps = new PrintablePreparedStatement(connection.prepareStatement(childQuery), childQuery, false);

			if (!Objects.equals(model.getCustomerPhone(), ""))
				ps.setString(1, model.getCustomerPhone());
			else
				ps.setNull(1, Types.VARCHAR);

			if (model.getReviewTimestamp() != null)
				ps.setTimestamp(2, model.getReviewTimestamp());
			else
				ps.setNull(2, Types.TIMESTAMP);

			if (!Objects.equals(model.getDriverPhone(), ""))
				ps.setString(3, model.getDriverPhone());
			else
				ps.setNull(3, Types.VARCHAR);

			if (model.getDriverReviewPackageHandlingRating() != null)
				ps.setInt(4, model.getDriverReviewPackageHandlingRating());
			else
				ps.setNull(4, Types.INTEGER);

			if (model.getDriverReviewDeliveryTimeRating() != null)
				ps.setInt(5, model.getDriverReviewDeliveryTimeRating());
			else
				ps.setNull(5, Types.INTEGER);

			ps.executeUpdate();

			/*
			 * To avoid partial operation done when error occurs
			 * Only commit after all three insert operations are completed,
			 * otherwise, rollback the whole operation.
			 */
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of DriverReview delete query,
	 * connects the database and delete an existing DriverReview.
	 * Returns a message for successful or failed operation.
	 */
	public String[] deleteDriverReview(DriverReviewsModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, model.getCustomerPhone());
			ps.setTimestamp(2, model.getReviewTimestamp());

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				String WARNING_MESSAGE = " DriverReview with customer_phone " + model.getCustomerPhone() +
						" and review_timeStamp " + model.getReviewTimestamp() + " does not exist!";
				System.out.println(WARNING_TAG + WARNING_MESSAGE);

				connection.commit();
				ps.close();
				return new String[]{FAIL_MESSAGE + WARNING_MESSAGE};
			}
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectDriverReviews(DriverReviewsModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getCustomerPhone(), ""))
					tuple.add(rs.getString("customer_phone"));
				if (model.getReviewTimestamp() != null)
					tuple.add(rs.getTimestamp("review_timeStamp"));
				if (!Objects.equals(model.getDriverPhone(), ""))
					tuple.add(rs.getString("driver_phone"));
				if (model.getDriverReviewPackageHandlingRating() != null)
					tuple.add(rs.getInt("driverReviews_packageHandlingRating"));
				if (model.getDriverReviewDeliveryTimeRating() != null)
					tuple.add(rs.getInt("driverReviews_deliveryTimeRating"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// OWN SERVICE //

	/**
	 * Function for the implementation of Own insert query,
	 * connects the database and insert a new Own.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertOwn(OwnModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (!Objects.equals(model.getRestaurantAddress(), ""))
				ps.setString(1, model.getRestaurantAddress());
			else
				ps.setNull(1, Types.VARCHAR);

			if (model.getMenuID() != null)
				ps.setInt(2, model.getMenuID());
			else
				ps.setNull(2, Types.INTEGER);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectOwn(OwnModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getRestaurantAddress(), ""))
					tuple.add(rs.getString("restaurant_address"));
				if (model.getMenuID() != null)
					tuple.add(rs.getInt("menu_id"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// CONTAIN SERVICE //

	/**
	 * Function for the implementation of Contain insert query,
	 * connects the database and insert a new Contain.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertContain(ContainModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (model.getMenuID() != null)
				ps.setInt(1, model.getMenuID());
			else
				ps.setNull(1, Types.INTEGER);

			if (!Objects.equals(model.getFoodName(), ""))
				ps.setString(2, model.getFoodName());
			else
				ps.setNull(2, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectContain(ContainModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (model.getMenuID() != null)
					tuple.add(rs.getInt("menu_id"));
				if (!Objects.equals(model.getFoodName(), ""))
					tuple.add(rs.getString("food_name"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// ORDERED SERVICE //

	/**
	 * Function for the implementation of Ordered insert query,
	 * connects the database and insert a new Ordered.
	 * Returns a message for successful or failed operation.
	 */
	public String[] insertOrdered(OrderedModel model, String query) {
		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			if (!Objects.equals(model.getFoodName(), ""))
				ps.setString(1, model.getFoodName());
			else
				ps.setNull(1, Types.VARCHAR);

			if (model.getOrderID() != null)
				ps.setInt(2, model.getOrderID());
			else
				ps.setNull(2, Types.INTEGER);

			if (model.getOrderedQuantity() != null)
				ps.setInt(3, model.getOrderedQuantity());
			else
				ps.setNull(3, Types.INTEGER);

			ps.executeUpdate();
			connection.commit();

			ps.close();
			return new String[]{SUCCESS_MESSAGE};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return new String[]{FAIL_MESSAGE + e.getMessage()};
		}
	}

	/**
	 * Function for the implementation of PROJECTION,
	 * Returns an array of objects for the user selected attributes.
	 */
	public Object[] projectOrdered(OrderedModel model, String query) {
		ArrayList<Object> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				ArrayList<Object> tuple = new ArrayList<>();

				if (!Objects.equals(model.getFoodName(), ""))
					tuple.add(rs.getString("food_name"));
				if (model.getOrderID() != null)
					tuple.add(rs.getInt("order_id"));
				if (model.getOrderedQuantity() != null)
					tuple.add(rs.getInt("orderedQuantity"));

				result.add(tuple.toArray(new Object[tuple.size()]));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new Object[result.size()]);
	}



	// SHOWTABLE QUERY FUNCTIONS //

	/**
	 * Show Customers Table
	 */
	public CustomersModel[] getCustomersTable(String query) {
		ArrayList<CustomersModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				CustomersModel model = new CustomersModel(rs.getString("customer_phone"),
					rs.getString("customer_name"),
					rs.getString("customer_address"),
					rs.getString("customer_email"),
					rs.getString("customer_account"),
					rs.getString("customer_paymentInfo"),
					rs.getString("customer_membership"),
					rs.getInt("customer_points"),
					rs.getString("customer_discount"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new CustomersModel[result.size()]);
	}

	/**
	 * Show Restaurants Table
	 */
	public RestaurantsModel[] getRestaurantsTable(String query) {
		ArrayList<RestaurantsModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				RestaurantsModel model = new RestaurantsModel(rs.getString("restaurant_address"),
						rs.getString("restaurant_name"),
						rs.getString("restaurant_openHours"),
						rs.getString("restaurant_account"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new RestaurantsModel[result.size()]);
	}

	/**
	 * Show Menu Tables (MenuBasicInfo + MenuAdvancedInfo)
	 */
	public MenuModel[] getMenuTable(String query) {
		ArrayList<MenuModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				MenuModel model = new MenuModel(rs.getInt("menu_id"),
						rs.getString("menu_name"),
						rs.getString("menu_cuisine"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new MenuModel[result.size()]);
	}

	/**
	 * Show Food Tables (FoodBasicInfo + FoodAdvancedInfo)
	 */
	public FoodModel[] getFoodTable(String query) {
		ArrayList<FoodModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				FoodModel model = new FoodModel(rs.getString("food_name"),
						rs.getDouble("food_price"),
						rs.getString("food_type"),
						rs.getString("food_hasVeganDiet"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new FoodModel[result.size()]);
	}

	/**
	 * Show Orders Table
	 */
	public OrdersModel[] getOrdersTable(String query) {
		ArrayList<OrdersModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				OrdersModel model = new OrdersModel(rs.getInt("order_id"),
						rs.getDouble("order_totalPrice"),
						rs.getTimestamp("order_timeArrival"),
						rs.getString("customer_phone"),
						rs.getString("restaurant_address"),
						rs.getString("driver_phone"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new OrdersModel[result.size()]);
	}

	/**
	 * Show Payment Tables (PaymentBasicInfo + PaymentAdvancedInfo)
	 */
	public PaymentModel[] getPaymentTable(String query) {
		ArrayList<PaymentModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				PaymentModel model = new PaymentModel(rs.getInt("payment_id"),
						rs.getString("payment_status"),
						rs.getInt("order_id"),
						rs.getDouble("payment_price"),
						rs.getString("restaurant_address"),
						rs.getString("driver_phone"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new PaymentModel[result.size()]);
	}

	/**
	 * Show FoodReviews Tables (ReviewBasicInfo + ReviewAdvancedInfo + FoodReviews)
	 */
	public FoodReviewsModel[] getFoodReviewsTable(String query) {
		ArrayList<FoodReviewsModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				FoodReviewsModel model = new FoodReviewsModel(rs.getString("customer_phone"),
						rs.getTimestamp("review_timeStamp"),
						rs.getString("review_title"),
						rs.getString("review_image"),
						rs.getString("review_comment"),
						rs.getString("review_ratingType"),
						rs.getString("restaurant_address"),
						rs.getInt("foodReviews_foodQualityRating"),
						rs.getInt("foodReviews_portionSizeRating"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new FoodReviewsModel[result.size()]);
	}

	/**
	 * Show DriverReviews Tables (ReviewBasicInfo + ReviewAdvancedInfo + DriverReviews)
	 */
	public DriverReviewsModel[] getDriverReviewsTable(String query) {
		ArrayList<DriverReviewsModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				DriverReviewsModel model = new DriverReviewsModel(rs.getString("customer_phone"),
						rs.getTimestamp("review_timeStamp"),
						rs.getString("review_title"),
						rs.getString("review_image"),
						rs.getString("review_comment"),
						rs.getString("review_ratingType"),
						rs.getString("driver_phone"),
						rs.getInt("driverReviews_packageHandlingRating"),
						rs.getInt("driverReviews_deliveryTimeRating"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new DriverReviewsModel[result.size()]);
	}

	/**
	 * Show Drivers Table
	 */
	public DriversModel[] getDriversTable(String query) {
		ArrayList<DriversModel> result = new ArrayList<>();

		try {
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				DriversModel model = new DriversModel(rs.getString("driver_phone"),
						rs.getString("driver_name"),
						rs.getString("driver_account"),
						rs.getString("driver_vehicle"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new DriversModel[result.size()]);
	}





	/**
	 * Rollback the operation when error happens.
	 */
	private void rollbackConnection() {
		try  {
			connection.rollback();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


	/* DEPRECATED (not required in rubrics, we just use the .sql file through SQL*Plus)
	public void databaseSetup() {}*/

}

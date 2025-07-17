package controller;

import delegates.LoginWindowDelegate;
import delegates.GUIDelegate;
import delegates.ServiceModelDelegate;
import model.CustomersModel;
import service.AppService;
import database.AppDOA;
import ui.LoginWindow;
import ui.MainPage;

/**
 * This is the main controller class that will orchestrate login, service connection, quit, and database initialization.
 */
public class AppController implements LoginWindowDelegate, GUIDelegate {
	private AppService appService = null;
	private AppDOA dbHandler = null;
	private LoginWindow loginWindow = null;
	private static final String WARNING_TAG = "[WARNING]";
	private static final String CONTROLLER_LAYER_ERROR_MESSAGE = "Operation Failed";

	public AppController() {
		dbHandler = new AppDOA();
		appService = new AppService(dbHandler);
	}
	
	private void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);

	}
	
	/**
     * Implementation of the connection to Oracle database with supplied username and password.
     */ 
	public void login(String username, String password) {
		boolean didConnect = dbHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();
			// Call front-end main menu
			MainPage mainPage = new MainPage(this);
			mainPage.setVisible(true);

			// [TEST] For Back-end test
			// backendTestMain();
			// [TEST END]

		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}



	// [TEST]: These two methods are For Back-end test, will be removed after passing the test
	public void backendTestMain() {

		// [Create customer instances]

		// For normal inputs
		//CustomersModel testCusModel = new CustomersModel("1234567801", "John Doe", "123 Broadway", "JDcool1@gmail.com", "JDcool12341", "1234123412341234", "No", 0, "None");
		//CustomersModel testCusModel = new CustomersModel("1234567802", "John Doe", "123 Broadway", "JDcool2@gmail.com", "JDcool12342", "1234123412341234", "Yes", 0, "None");

		// For inputs with default --> test ok
		//CustomersModel testCusModel = new CustomersModel("1234567803", "John Doe", "123 Broadway", "JDcool3@gmail.com", "JDcool12343", "1234123412341234", null, 0, "None");

		// For inputs with null (valid) --> test ok
		//CustomersModel testCusModel = new CustomersModel("1234567804", "John Doe", "123 Broadway", "JDcool4@gmail.com", "JDcool12344", "1234123412341234", null, 50, null);

		// For repeated inputs for PK and UNIQUE --> test ok
		//CustomersModel testCusModel = new CustomersModel("1234567801", "John Doe", "123 Broadway", "JDcool5@gmail.com", "JDcool12345", "1234123412341234", "No", 0, "None");
		//CustomersModel testCusModel = new CustomersModel("1234567805", "John Doe", "123 Broadway", "JDcool1@gmail.com", "JDcool12345", "1234123412341234", "No", 0, "None");

		// For inputs with null (not valid) --> test ok
		//CustomersModel testCusModel = new CustomersModel("1234567806", null, "123 Broadway", "JDcool6@gmail.com", "JDcool12346", "1234123412341234", "No", 0, "None");
		//CustomersModel testCusModel = new CustomersModel("1234567806", "John Doe", "123 Broadway", null, "JDcool12346", "1234123412341234", "No", 0, "None");

		// For updates --> test ok
		//CustomersModel testUpdatedCusModel = new CustomersModel("1234567801", "Kenny Lee", "456 Main Street", "KLjustHandsome@gmail.com", "KL777", "4321432143214321", "No", 50, "None");
		//CustomersModel testUpdatedCusModel = new CustomersModel("1234567810", "Kenny Lee", "456 Main Street", "KLjustCool@gmail.com", "KL888", "4321432143214321", "No", 50, "None");
		//CustomersModel testUpdatedCusModel = new CustomersModel("1234567802", null, null, null, "TEST", null, null, 1000, null);

		// [Implement customer INSERT, UPDATE, DELETE]

		//String cusInsertTestMessage = this.service("customer", "insert", testCusModel);
		//System.out.println(cusInsertTestMessage);

		//String cusUpdateTestMessage = this.service("customer", "update", testUpdatedCusModel);
		//System.out.println(cusUpdateTestMessage);

		//String cusDeleteTestMessage = this.service("customer", "delete", testUpdatedCusModel);
		//System.out.println(cusDeleteTestMessage);

		// INSERT, UPDATE, DELETE --> pass test

		// Test: Show table --> test ok
		//CustomersModel[] customerTable = (CustomersModel[]) this.requestTable("customer");
		//backendTestPrintCusTable(customerTable);

		// Test: Driver Projection
		//DriversModel testDriverModel = new DriversModel("", "", "yes", "yes");
		//Object[] driverProjectTable = this.service("driver", "projection", testDriverModel);

		/*
		// Iterate through each element in the object array
		for (Object tuple : driverProjectTable) {
			// Print each element in the sub-array
			for (Object element : (Object[]) tuple) {
				System.out.print(element + " ");
			}
			System.out.println(); // New line after each sub-array
		}*/
	}

	public void backendTestPrintCusTable(CustomersModel[] models) {
		for (int i = 0; i < models.length; i++) {
			CustomersModel model = models[i];
			// simplified output formatting; truncation may occur
			System.out.printf("%-20.20s", model.getCustomerPhone());
			System.out.printf("%-20.20s", model.getCustomerName());
			if (model.getCustomerAddress() == null) {
				System.out.printf("%-20.20s", " ");
			} else {
				System.out.printf("%-20.20s", model.getCustomerAddress());
			}
			System.out.printf("%-30.30s", model.getCustomerEmail());
			System.out.printf("%-20.20s", model.getCustomerAccount());
			System.out.printf("%-20.20s", model.getCustomerPaymentInfo());
			System.out.printf("%-10.10s", model.getCustomerMembership());
			System.out.printf("%-10.10s", model.getCustomerPoints());
			System.out.printf("%-10.10s", model.getCustomerDiscount());
			System.out.println();
		}
	}
	// [TEST END]



	/**
	 * [Method For Frontend to Call]
	 * Implementation of the connection to the application service layer.
	 * Called by the GUI instance, then pass values from the GUI instance to service layer.
	 */
	public Object[] service(String serviceType, String mode, ServiceModelDelegate model) {
		try {
			return appService.serviceHandler(serviceType, mode, model);
		} catch (IllegalArgumentException e) {
			System.out.println(WARNING_TAG + " " + e.getMessage());
			return new String[]{CONTROLLER_LAYER_ERROR_MESSAGE};
		}
    }

	/**
	 * [Method For Frontend to Call]
	 * Function for the GUI to request the customer table
	 */
	public Object[] requestTable(String serviceType) {
		try {
			return appService.showTableHandler(serviceType);
		} catch (IllegalArgumentException e) {
			System.out.println(WARNING_TAG + " " + e.getMessage());
			return null;
		}
	}


    /**
	 * [Method For Frontend to Call]
     * The GUI instance tells us that it is done with what it's doing so,
	 * this function cleans up the connection since it's no longer needed.
     */ 
    public void quit() {
    	dbHandler.close();
    	dbHandler = null;
    	
    	System.exit(0);
    }


    /* DEPRECATED (not required in rubrics, we just use the .sql file through SQL*Plus)
	public void databaseInitialize() {
		dbHandler.databaseSetup();
	}*/


	/**
	 * Main method called at launch time.
	 */
	public static void main(String args[]) {
		AppController app = new AppController();
		app.start();
	}
}

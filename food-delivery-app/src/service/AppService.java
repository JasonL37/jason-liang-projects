package service;

import database.AppDOA;
import delegates.ServiceModelDelegate;

/**
 * This is the main service class that will implement the business logic.
 */
public class AppService {
	// For showTableHandler & serviceHandler
	private CustomerAccountService customerAccountService = null;
	private RestaurantService restaurantService = null;
	private FoodService foodService = null;
	private MenuService menuService = null;
	private OrderService orderService = null;
	private PaymentService paymentService = null;
	private FoodReviewService foodReviewService = null;
	private DriverReviewsService driverReviewService = null;
	private DriverService driverService = null;
	// For serviceHandler only
	private FoodBasicService foodBasicService = null;
	private FoodAdvancedService foodAdvancedService = null;
	private MenuBasicService menuBasicService = null;
	private MenuAdvancedService menuAdvancedService = null;
	private PaymentBasicService paymentBasicService = null;
	private PaymentAdvancedService paymentAdvancedService = null;
	private ReviewBasicService reviewBasicService = null;
	private ReviewAdvancedService reviewAdvancedService = null;
	private OwnService ownService = null;
	private ContainService containService = null;
	private OrderedService orderedService = null;

	// Log messages
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Operation Failed";

	public AppService(AppDOA dbHandler) {
		// For showTableHandler & serviceHandler
		customerAccountService = new CustomerAccountService(dbHandler);
		restaurantService = new RestaurantService(dbHandler);
		menuService = new MenuService(dbHandler);
		orderService = new OrderService(dbHandler);
		paymentService = new PaymentService(dbHandler);
		foodService = new FoodService(dbHandler);
		foodReviewService = new FoodReviewService(dbHandler);
		driverReviewService = new DriverReviewsService(dbHandler);
		driverService = new DriverService(dbHandler);
		// For serviceHandler only
		foodBasicService = new FoodBasicService(dbHandler);
		foodAdvancedService = new FoodAdvancedService(dbHandler);
		menuBasicService = new MenuBasicService(dbHandler);
		menuAdvancedService = new MenuAdvancedService(dbHandler);
		paymentBasicService = new PaymentBasicService(dbHandler);
		paymentAdvancedService = new PaymentAdvancedService(dbHandler);
		reviewBasicService = new ReviewBasicService(dbHandler);
		reviewAdvancedService = new ReviewAdvancedService(dbHandler);
		ownService = new OwnService(dbHandler);
		containService = new ContainService(dbHandler);
		orderedService = new OrderedService(dbHandler);
	}

	/**
	 * Called by controller layer, handles which service to access determined by the pass values from controller layer.
	 */
	public Object[] serviceHandler(String serviceType, String mode, ServiceModelDelegate model) {
		switch (serviceType) {
			case "customer":
				return customerAccountService.operationHandler(mode, model);
			case "driver":
				return driverService.operationHandler(mode, model);
			case "restaurant":
				return restaurantService.operationHandler(mode, model);
			case "foodBasic":
				return foodBasicService.operationHandler(mode, model);
			case "foodAdvanced":
				return foodAdvancedService.operationHandler(mode, model);
			case "menuBasic":
				return menuBasicService.operationHandler(mode, model);
			case "menuAdvanced":
				return menuAdvancedService.operationHandler(mode, model);
			case "order":
				return orderService.operationHandler(mode, model);
			case "paymentBasic":
				return paymentBasicService.operationHandler(mode, model);
			case "paymentAdvanced":
				return paymentAdvancedService.operationHandler(mode, model);
			case "reviewBasic":
				return reviewBasicService.operationHandler(mode, model);
			case "reviewAdvanced":
				return reviewAdvancedService.operationHandler(mode, model);
			case "foodReview":
				return foodReviewService.operationHandler(mode, model);
			case "driverReview":
				return driverReviewService.operationHandler(mode, model);
			case "own":
				return ownService.operationHandler(mode, model);
			case "contain":
				return containService.operationHandler(mode, model);
			case "ordered":
				return orderedService.operationHandler(mode, model);
			default:
				System.out.println(SERVICE_LAYER_ERROR_TAG + "Service selection went wrong.");
				return new String[]{SERVICE_LAYER_ERROR_MESSAGE};
		}
    }

	/**
	 * Function for Showing the customer table
	 */
	public Object[] showTableHandler(String serviceType) {
			switch (serviceType) {
				case "customer":
					return customerAccountService.showTable();
				case "restaurant":
					return restaurantService.showTable();
				case "menu":
					return menuService.showTable();
				case "food":
					return foodService.showTable();
				case "order":
					return orderService.showTable();
				case "payment":
					return paymentService.showTable();
				case "foodReview":
					return foodReviewService.showTable();
				case "driverReview":
					return driverReviewService.showTable();
				case "driver":
					return driverService.showTable();
				default:
					System.out.println(SERVICE_LAYER_ERROR_TAG + "Service selection went wrong.");
					return null;
			}
	}

}

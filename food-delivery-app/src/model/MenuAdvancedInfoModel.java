package model;

import delegates.ServiceModelDelegate;

public class MenuAdvancedInfoModel implements ServiceModelDelegate {
    private String menuName;
    private String menuCuisine;

    public MenuAdvancedInfoModel(String name,
                                 String cuisine) {
        this.menuName = name;
        this.menuCuisine = cuisine;
    }

    public String getMenuName() { return menuName; }
    public String getMenuCuisine() { return menuCuisine; }

    public void setMenuName(String name) { this.menuName = name; }
    public void setMenuCuisine(String cuisine) { this.menuCuisine = cuisine; }
}

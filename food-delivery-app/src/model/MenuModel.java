package model;

import delegates.ServiceModelDelegate;

public class MenuModel implements ServiceModelDelegate {
    private Integer menuID;
    private String menuName;
    private String menuCuisine;

    public MenuModel(Integer menuID,
                     String name,
                     String cuisine) {
        this.menuID = menuID;
        this.menuName = name;
        this.menuCuisine = cuisine;
    }

    public Integer getMenuID() { return menuID; }
    public String getMenuName() { return menuName; }
    public String getMenuCuisine() { return menuCuisine; }

    public void setMenuID(Integer menuID) { this.menuID = menuID; }
    public void setMenuName(String name) { this.menuName = name; }
    public void setMenuCuisine(String cuisine) { this.menuCuisine = cuisine; }
}

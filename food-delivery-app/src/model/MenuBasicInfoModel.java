package model;

import delegates.ServiceModelDelegate;

public class MenuBasicInfoModel implements ServiceModelDelegate {
    private Integer menuID;
    private String menuName;

    public MenuBasicInfoModel(Integer menuID,
                              String name) {
        this.menuID = menuID;
        this.menuName = name;
    }

    public Integer getMenuID() { return menuID; }
    public String getMenuName() { return menuName; }

    public void setMenuID(Integer menuID) { this.menuID = menuID; }
    public void setMenuName(String name) { this.menuName = name; }
}

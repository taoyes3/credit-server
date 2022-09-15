package com.taoyes3.credit.sys.constant;

/**
 * @author taoyes3
 * @date 2022/9/15 11:
 *
 * 菜单类型
 */
public enum MenuType {
    //目录
    CATALOG(0),
    //菜单
    MENU(1),
    //按钮
    BUTTON(2);

    private int value;

    public int getValue() {
        return value;
    }

    MenuType(int value) {
        this.value = value;
    }
}

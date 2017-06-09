package ua.com.pandasushi.controller.order;

import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;

/**
 * Created by Тарас on 26.10.2016.
 */
public class OrderController implements TabController {

    MainController main;

    @Override
    public void init(MainController main) {
        this.main = main;
    }
}

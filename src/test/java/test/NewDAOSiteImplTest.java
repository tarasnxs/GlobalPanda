package test;

import junit.framework.TestCase;
import ua.com.pandasushi.database.site.dao.DAOSite;
import ua.com.pandasushi.database.site.dao.NewDAOSiteImpl;

import java.util.Calendar;

public class NewDAOSiteImplTest extends TestCase {
    DAOSite daoSite = new NewDAOSiteImpl();

    public void testGetNettoConsumptionCafeBetweenDates() {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.DATE, -90);
        System.out.println(daoSite.getNettoConsumptionCafeBetweenDates(1001, from, to));
    }
}
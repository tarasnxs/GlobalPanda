package ua.com.pandasushi.database.site.dao;

import com.sun.org.apache.xpath.internal.operations.Bool;
import command.Command;
import ua.com.pandasushi.database.common.*;
import ua.com.pandasushi.database.common.inventory.CalculatedNetto;
import ua.com.pandasushi.database.common.inventory.InvCafeSelect;
import ua.com.pandasushi.database.common.inventory.InvSelect;
import ua.com.pandasushi.database.common.menu.*;
import ua.com.pandasushi.database.common.menu.cafe.TEHCARDS_CAFE;
import ua.com.pandasushi.main.GlobalPandaApp;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Taras on 03-Apr-17.
 */
public class NewDAOSiteImpl implements DAOSite {
    private static final long serialVersionUID = 1L;

    private static final String POST_URL = "http://185.25.117.145:8080/pandaserver/GetData";


    public NewDAOSiteImpl () {

    }


    private Object sendCommand (Command command) throws IOException, ClassNotFoundException {
        Object result = null;
        command.setConfig(GlobalPandaApp.config);

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            URL url = new URL(POST_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            oos = new ObjectOutputStream(conn.getOutputStream());
            oos.writeObject(command);

            conn.connect();

            ois = new ObjectInputStream(conn.getInputStream());

            result = ois.readObject();


        } catch (EOFException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null)
                oos.close();
            if (ois != null)
                ois.close();
        }

        return result;
    }

    @Override
    public void save(Object o) {
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Object.class});
        command.setArguments(new Object[]{o});
        try {
            sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Object o) {
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Object.class});
        command.setArguments(new Object[]{o});
        try {
            sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateList(Collection list) {
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Collection.class});
        command.setArguments(new Object[]{list});
        try {
            sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer updateImportance(Integer ingId, Integer kitchId, Integer value) {
        Integer newImportance = 0;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class, Integer.class});
        command.setArguments(new Object[]{ingId, kitchId, value});
        try {
            newImportance = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newImportance;
    }

    @Override
    public ArrayList<CustomersSite> getCustomers(Date from, Date to) {
        ArrayList<CustomersSite> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Date.class, Date.class});
        command.setArguments(new Object[]{from, to});
        try {
            result = (ArrayList<CustomersSite>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<CustomersSite> getCustomers(Date from, Date to, Integer kitchen) {
        ArrayList<CustomersSite> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Date.class, Date.class, Integer.class});
        command.setArguments(new Object[]{from, to, kitchen});
        try {
            result = (ArrayList<CustomersSite>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Float> getSumForType(Integer type) {
        ArrayList<Float> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{type.getClass()});
        command.setArguments(new Object[]{type});
        try {
            ArrayList<Float> receive = (ArrayList<Float>) sendCommand(command);
            result = receive;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getReportProductPurchase() {
        ArrayList<Operations> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Operations>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getReportProductPurchase(Date date) {
        ArrayList<Operations> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{date.getClass()});
        command.setArguments(new Object[]{date});
        try {
            result = (ArrayList<Operations>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getDebtPurchase(Integer checkId) {
        ArrayList<Operations> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{checkId.getClass()});
        command.setArguments(new Object[]{checkId});
        try {
            result = (ArrayList<Operations>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getNextCheckID(Integer type) {
        Integer result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{type.getClass()});
        command.setArguments(new Object[]{type});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getNextInventoryCheckId() {
        Integer result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Boolean alreadyInventory() {
        boolean result = true;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            Boolean bool = (Boolean) sendCommand(command);
            result = bool.booleanValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public LinkedHashMap<Integer, ArrayList<Inventory>> getTodayInventory() {
        LinkedHashMap<Integer, ArrayList<Inventory>> result = new LinkedHashMap<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (LinkedHashMap<Integer, ArrayList<Inventory>>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Long getLastInventoryId() {
        Long result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (Long) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Float getAvgCoefProduct(Integer productId) {
        Float result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{productId.getClass()});
        command.setArguments(new Object[]{productId});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Float getAvgCoefProduct(String productName) {
        Float result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{productName.getClass()});
        command.setArguments(new Object[]{productName});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Float getCoefNF(Integer ingredientId, Integer nfId) {
        Float result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{ingredientId.getClass(), nfId.getClass()});
        command.setArguments(new Object[]{ingredientId, nfId});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Boolean saveInventory(LinkedHashMap<Integer, ArrayList<Inventory>> inventoryMap) {
        boolean result = false;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{inventoryMap.getClass()});
        command.setArguments(new Object[]{inventoryMap});
        try {
            Boolean res = (Boolean) sendCommand(command);
            result = res.booleanValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void saveOperation(Operations op) {
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{op.getClass()});
        command.setArguments(new Object[]{op});
        try {
            sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Rozrobka> getTodayRozrobka() {
        ArrayList<Rozrobka> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Rozrobka>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Boolean saveRozrobka(Rozrobka rozrobka) {
        boolean result = false;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{rozrobka.getClass()});
        command.setArguments(new Object[]{rozrobka});
        try {
            Boolean bool = (Boolean) sendCommand(command);
            result = bool.booleanValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Boolean saveOperations(ArrayList<Operations> opList) {
        boolean result = false;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{opList.getClass()});
        command.setArguments(new Object[]{opList});
        try {
            Boolean bool = (Boolean) sendCommand(command);
            result = bool.booleanValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public HashMap<Integer, ArrayList<Operations>> getOpenShifts() {
        HashMap<Integer, ArrayList<Operations>> result = new HashMap<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (HashMap<Integer, ArrayList<Operations>>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Kitchens> getKitchens() {
        ArrayList<Kitchens> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Kitchens>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Float getDebt(Integer checkID) {
        float result = 0.0f;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{checkID.getClass()});
        command.setArguments(new Object[]{checkID});
        try {
            Float fl = (Float) sendCommand(command);
            result = fl.floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void updateAverageProducts(Integer prodId) {
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{prodId.getClass()});
        command.setArguments(new Object[]{prodId});
        try {
            sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Employee> getEmployeesForLogin() {
        ArrayList<Employee> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Employee>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Employee> getCooks() {
        ArrayList<Employee> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Employee>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Employee> getCouriers() {
        ArrayList<Employee> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Employee>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<String> getContrAgentsForType(Integer type) {
        ArrayList<String> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{type.getClass()});
        command.setArguments(new Object[]{type});
        try {
            result = (ArrayList<String>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getOperationsByType(Integer type) {
        ArrayList<Operations> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{type.getClass()});
        command.setArguments(new Object[]{type});
        try {
            result = (ArrayList<Operations>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Operations getLastProductPurchase(Integer productID) {
        Operations op = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{productID.getClass()});
        command.setArguments(new Object[]{productID});
        try {
            op = (Operations) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return op;
    }

    @Override
    public ArrayList<Rozrobka> getLastRozrobka(Integer ingredientID) {
        ArrayList<Rozrobka> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{ingredientID.getClass()});
        command.setArguments(new Object[]{ingredientID});
        try {
            result = (ArrayList<Rozrobka>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getOperations() {
        ArrayList<Operations> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Operations>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Float getNewAvgCoef(Integer prodId) {
        float result = 0.0f;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{prodId.getClass()});
        command.setArguments(new Object[]{prodId});
        try {
            Float res = (Float) sendCommand(command);
            result = res.floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<TEHCARDS> getTehcards() {
        ArrayList<TEHCARDS> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<TEHCARDS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<TEHCARDS> getTehcardsForDish(Integer dishID) {
        ArrayList<TEHCARDS> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{dishID.getClass()});
        command.setArguments(new Object[]{dishID});
        try {
            result = (ArrayList<TEHCARDS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PRODUCTS_INGREDIENTS> getProdIng() {
        ArrayList<PRODUCTS_INGREDIENTS> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<PRODUCTS_INGREDIENTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PRODUCTS_INGREDIENTS getProdIngForProd(String prod) {
        PRODUCTS_INGREDIENTS result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{prod.getClass()});
        command.setArguments(new Object[]{prod});
        try {
            result = (PRODUCTS_INGREDIENTS) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PRODUCTS_INGREDIENTS> getProdIngRozrobka() {
        ArrayList<PRODUCTS_INGREDIENTS> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<PRODUCTS_INGREDIENTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PRODUCTS_INGREDIENTS> getProdForIng(Integer ingredientId) {
        ArrayList<PRODUCTS_INGREDIENTS> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{ingredientId.getClass()});
        command.setArguments(new Object[]{ingredientId});
        try {
            result = (ArrayList<PRODUCTS_INGREDIENTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getSumProdPurchase(Date from, Date to, Integer productId, Integer kitchId) {
        Integer result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), productId.getClass(), kitchId.getClass()});
        command.setArguments(new Object[]{from, to, productId, kitchId});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getSumU1ProdPurchase(Date from, Date to, Integer productId, Integer kitchId) {
        Integer result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), productId.getClass(), kitchId.getClass()});
        command.setArguments(new Object[]{from, to, productId, kitchId});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getReportProdPurchase(Date from, Date to, Integer productId, Integer kitchen) {
        ArrayList<Operations> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), productId.getClass(), kitchen.getClass()});
        command.setArguments(new Object[]{from, to, productId, kitchen});
        try {
            result = (ArrayList<Operations>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getSumProdShift(Date from, Date to, Integer prodIngId, Integer kitchId) {
        int result = 0;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), prodIngId.getClass(), kitchId.getClass()});
        command.setArguments(new Object[]{from, to, prodIngId, kitchId});
        try {
            result = (int) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getSumU1ProdShift(Date from, Date to, Integer prodIngId, Integer kitchId) {
        Integer result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), prodIngId.getClass(), kitchId.getClass()});
        command.setArguments(new Object[]{from, to, prodIngId, kitchId});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getReportProdShift(Date from, Date to, Integer productId, Integer kitchen) {
        ArrayList<Operations> result = null;
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), productId.getClass(), kitchen.getClass()});
        command.setArguments(new Object[]{from, to, productId, kitchen});
        try {
            result = (ArrayList<Operations>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Consumption> deleteCancelled(Collection<Consumption> cl) {
        return new ArrayList<>();
    }

    @Override
    public Integer getIngredientConsumption(Date from, Date to, Integer ingredientId, Integer kitchId) {
        Integer result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), ingredientId.getClass(), kitchId.getClass()});
        command.setArguments(new Object[]{from, to, ingredientId, kitchId});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public ArrayList<Consumption> getIngredientConsumptionList(Date from, Date to, Integer ingredientId, Integer kitch) {
        ArrayList<Consumption> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), ingredientId.getClass(), kitch.getClass()});
        command.setArguments(new Object[]{from, to, ingredientId, kitch});
        try {
            result = (ArrayList<Consumption>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<ConsumptionCafe> getIngredientConsumptionCafeList(Date from, Date to, Integer ingredientId, Integer kitch) {
        ArrayList<ConsumptionCafe> result = new ArrayList<>();
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), ingredientId.getClass(), kitch.getClass()});
        command.setArguments(new Object[]{from, to, ingredientId, kitch});
        try {
            result = (ArrayList<ConsumptionCafe>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Integer> getRozrobka(Date from, Date to, Integer productId, Integer kitchId) {
        ArrayList<Integer> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), productId.getClass(), kitchId.getClass()});
        command.setArguments(new Object[]{from, to, productId, kitchId});
        try {
            result = (ArrayList<Integer>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Rozrobka> getRozrobkas(Date from, Date to, Integer productId, Integer kitchId) {
        ArrayList<Rozrobka> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{from.getClass(), to.getClass(), productId.getClass(), kitchId.getClass()});
        command.setArguments(new Object[]{from, to, productId, kitchId});
        try {
            result = (ArrayList<Rozrobka>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Inventory> getLastInventory(Integer basicIngredientId, Integer kitchId) {
        ArrayList<Inventory> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{basicIngredientId.getClass(), kitchId.getClass()});
        command.setArguments(new Object[]{basicIngredientId, kitchId});
        try {
            result = (ArrayList<Inventory>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Inventory> getLastInventory(Integer basicIngredientId, Integer checkId, Integer kitchid) {
        ArrayList<Inventory> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{basicIngredientId.getClass(), checkId.getClass(), kitchid.getClass()});
        command.setArguments(new Object[]{basicIngredientId, checkId, kitchid});
        try {
            result = (ArrayList<Inventory>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<INGREDIENTS> getIngredientsInventory() {
        ArrayList<INGREDIENTS> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<INGREDIENTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Inventory> getAllInventory() {
        ArrayList<Inventory> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Inventory>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Inventory> getInventoryList(Integer checkId) {
        ArrayList<Inventory> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{checkId.getClass()});
        command.setArguments(new Object[]{checkId});
        try {
            result = (ArrayList<Inventory>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<TEHCARDS> getNF(Integer ingredientId) {
        ArrayList<TEHCARDS> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{ ingredientId.getClass() });
        command.setArguments(new Object[]{ ingredientId });
        try {
            result = (ArrayList<TEHCARDS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;

    }

    @Override
    public ArrayList<PRODUCTS_INGREDIENTS> getProductsInventory(Integer ingredientId) {
        ArrayList<PRODUCTS_INGREDIENTS> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{ingredientId.getClass()});
        command.setArguments(new Object[]{ingredientId});
        try {
            result = (ArrayList<PRODUCTS_INGREDIENTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;

    }

    @Override
    public ArrayList<PRODUCTS> getProducts() {
        ArrayList<PRODUCTS> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<PRODUCTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public PRODUCTS getProduct(Integer id) {
        PRODUCTS result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{id.getClass()});
        command.setArguments(new Object[]{id});
        try {
            result = (PRODUCTS) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getNextProductId() {
        Integer result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getNextProdIngId() {
        Integer result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<String> getProdFirstUnits() {
        ArrayList<String> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<String>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Menu> getMenu() {
        ArrayList<Menu> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Menu>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<INGREDIENTS> getIngredients() {
        ArrayList<INGREDIENTS> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<INGREDIENTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<GROUPS_LIB> getGroups() {
        ArrayList<GROUPS_LIB> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<GROUPS_LIB>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Employee>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Employee> getEmployeesForSchedule() {
        ArrayList<Employee> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<Employee>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Employee> getEmployeesForSchedule(Integer kitchId) {
        ArrayList<Employee> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class});
        command.setArguments(new Object[]{kitchId});
        try {
            result = (ArrayList<Employee>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Employee> getEmployeesForSchedule(String position) {
        ArrayList<Employee> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{String.class});
        command.setArguments(new Object[]{position});
        try {
            result = (ArrayList<Employee>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public KitchProperties getKitchProperty(LocalDate date, Integer kitch) {
        KitchProperties result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{LocalDate.class, Integer.class});
        command.setArguments(new Object[]{date, kitch});
        try {
            result = (KitchProperties) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Schedule> getSchedule(Integer kitchId, LocalDate date, Boolean plan) {
        ArrayList<Schedule> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, LocalDate.class, Boolean.class});
        command.setArguments(new Object[]{kitchId, date, plan});
        try {
            result = (ArrayList<Schedule>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Schedule> getScheduleBetween(Integer kitchId, LocalDate start, LocalDate end, Boolean plan) {
        return null;
    }

    @Override
    public ArrayList<Schedule> getEmployeeWorkDays(Integer employeeId, LocalDate start, LocalDate end, Boolean plan) {
        ArrayList<Schedule> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, LocalDate.class, LocalDate.class, Boolean.class});
        command.setArguments(new Object[]{employeeId, start, end, plan});
        try {
            result = (ArrayList<Schedule>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public HashMap<LocalDate, HashMap<Integer, Schedule>> getSceduleMapPlan(LocalDate start, LocalDate end, String position) {
        HashMap<LocalDate, HashMap<Integer, Schedule>> result = new HashMap<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{LocalDate.class, LocalDate.class, String.class});
        command.setArguments(new Object[]{start, end, position});
        try {
            result = (HashMap<LocalDate, HashMap<Integer, Schedule>>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<Schedule> getSceduleListFact(LocalDate date, Integer kitchId) {
        ArrayList<Schedule> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{LocalDate.class, Integer.class});
        command.setArguments(new Object[]{date, kitchId});
        try {
            result = (ArrayList<Schedule>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void saveSchedule(Schedule schedule) {
        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Schedule.class});
        command.setArguments(new Object[]{schedule});
        try {
            sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Float getIngCost() {
        Float result = 0.0f;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Float getIngCost(ArrayList<Inventory> list) {
        Float result = 0.0f;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{ArrayList.class});
        command.setArguments(new Object[]{list});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Float getProperty(String property) {
        Float result = 0.0f;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{String.class});
        command.setArguments(new Object[]{property});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<InvSelect> fillTodayInventory() {
        ArrayList<InvSelect> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<InvSelect>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Calendar[] getDayLimits(Calendar day) {
        Calendar[] result = null;

        Command command = new Command();
            command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
            command.setArgumentTypes(new Class[]{Calendar.class});
            command.setArguments(new Object[]{day});
            try {
            result = (Calendar[]) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public CalculatedNetto getCalculatedNettoOnDate(Integer kitchen, Integer ingId, Calendar date) {
        CalculatedNetto result = new CalculatedNetto();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class, Calendar.class});
        command.setArguments(new Object[]{kitchen, ingId, date});
        try {
            result = (CalculatedNetto) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Calendar getLastInventoryDate(Integer kitchen, Integer ingId, Calendar date) {
        return null;
    }

    @Override
    public Integer getInventoryNettoOnDate(Integer kitchen, Integer ingId, Calendar date) {
        return null;
    }

    @Override
    public Integer getProductPurchaseNettoBetweenDates(Integer kitchen, Integer ingId, Calendar start, Calendar end) {
        return null;
    }

    @Override
    public Integer getNettoShiftBetweenDates(Integer kitchen, Integer ingId, Calendar start, Calendar end) {
        return null;
    }

    @Override
    public Integer getNettoWriteOffBetweenDates(Integer kitchen, Integer ingId, Calendar start, Calendar end) {
        return null;
    }

    @Override
    public Integer getDiffProcessingBetweenDates(Integer kitchen, Integer ingId, Calendar start, Calendar end) {
        return null;
    }

    @Override
    public Integer getNettoConsumptionBetweenDates(Integer kitchen, Integer ingId, Calendar start, Calendar end) {
        return null;
    }

    @Override
    public Float getCoefToNettoOnDate(Integer prodIngId, Calendar date) {
        Float result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Calendar.class});
        command.setArguments(new Object[]{prodIngId,date});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Float getCoefNf(Integer ingId, Integer nfId) {
        Float result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class});
        command.setArguments(new Object[]{ingId,nfId});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Float getIngredientCostOnDate(Integer ingId, Calendar date, String city) {
        return getIngredientCostOnDate(null, ingId, date, city);
    }

    @Override
    public Float getIngredientCostOnDate(Integer nettoLeft, Integer ingId, Calendar date, String city) {
        Float result = 0.0f;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class, Calendar.class, String.class});
        command.setArguments(new Object[]{nettoLeft, ingId, date, city});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public HashMap<Integer, HashMap<Integer, Integer>> getStatsForMobile(Date from, Date to) {
        return null;
    }

    @Override
    public Float getPrice(Integer id, Calendar date) {
        Float result = 0.0f;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Calendar.class});
        command.setArguments(new Object[]{id, date});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<INGREDIENTS> getIngsForReport() {
        ArrayList<INGREDIENTS> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<INGREDIENTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public InventoryBalance getLastInvBalance(Integer kitch, Integer ingredientId) {
        InventoryBalance result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class});
        command.setArguments(new Object[]{kitch, ingredientId});
        try {
            result = (InventoryBalance) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public HashMap<Integer, HashMap<Integer, InventoryBalance>> getInventoryBalanceOnDate(Calendar date) {
        HashMap<Integer, HashMap<Integer, InventoryBalance>> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Calendar.class});
        command.setArguments(new Object[]{date});
        try {
            result = (HashMap<Integer, HashMap<Integer, InventoryBalance>>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public LinkedHashMap<Integer, ArrayList<InventoryCafe>> getTodayCafeInventory(Calendar cal) {
        LinkedHashMap<Integer, ArrayList<InventoryCafe>> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Calendar.class});
        command.setArguments(new Object[]{cal});
        try {
            result = (LinkedHashMap<Integer, ArrayList<InventoryCafe>>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public CalculatedNetto getCalculatedNettoCafeOnDate(Integer kitch_id, Integer i, Calendar instance) {
        CalculatedNetto result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class, Calendar.class});
        command.setArguments(new Object[]{kitch_id, i, instance});
        try {
            result = (CalculatedNetto) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Boolean saveCafeInventory(LinkedHashMap<Integer, ArrayList<InventoryCafe>> inventoryMap) {
        Boolean result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{inventoryMap.getClass()});
        command.setArguments(new Object[]{inventoryMap});
        try {
            result = (Boolean) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getNextCafeInventoryCheckId() {
        Integer result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<InventoryCafe> getLastCafeInventory(Integer ingredientId, Integer kitch_id) {
        ArrayList<InventoryCafe> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class});
        command.setArguments(new Object[]{ingredientId, kitch_id});
        try {
            result = (ArrayList<InventoryCafe>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<InventoryCafe> getLastCafeInventory(Integer basicIng, Integer checkId, Integer kitchen) {
        ArrayList<InventoryCafe> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class, Integer.class});
        command.setArguments(new Object[]{basicIng, checkId, kitchen});
        try {
            result = (ArrayList<InventoryCafe>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<InvCafeSelect> fillTodayCafeInventory() {
        ArrayList<InvCafeSelect> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<InvCafeSelect>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<InventoryCafe> getAllCafeInventory() {
        ArrayList<InventoryCafe> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<InventoryCafe>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<InventoryCafe> getInventoryCafeList(Integer checkId) {
        ArrayList<InventoryCafe> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class});
        command.setArguments(new Object[]{checkId});
        try {
            result = (ArrayList<InventoryCafe>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Float getIngCafeCost(ArrayList<InventoryCafe> cur) {
        Float result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{cur.getClass()});
        command.setArguments(new Object[]{cur});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<INGREDIENTS> getCafeIngredients() {
        ArrayList<INGREDIENTS> result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{});
        command.setArguments(new Object[]{});
        try {
            result = (ArrayList<INGREDIENTS>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getNettoConsumptionCafeBetweenDates(Integer ingId, Calendar lastInventoryDate, Calendar date) {
        Integer result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Calendar.class, Calendar.class});
        command.setArguments(new Object[]{ingId, lastInventoryDate, date});
        try {
            result = (Integer) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Boolean alreadyCafeInventory(Calendar cal) {
        Boolean result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Calendar.class});
        command.setArguments(new Object[]{cal});
        try {
            result = (Boolean) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public ArrayList<TEHCARDS_CAFE> getCafeNF(Integer ingredientId) {
        ArrayList<TEHCARDS_CAFE> result = new ArrayList<>();

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{ ingredientId.getClass() });
        command.setArguments(new Object[]{ ingredientId });
        try {
            result = (ArrayList<TEHCARDS_CAFE>) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Float getCoefCafeNf(Integer ingId, Integer nfId) {
        Float result = null;

        Command command = new Command();
        command.setMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
        command.setArgumentTypes(new Class[]{Integer.class, Integer.class});
        command.setArguments(new Object[]{ingId,nfId});
        try {
            result = (Float) sendCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
}

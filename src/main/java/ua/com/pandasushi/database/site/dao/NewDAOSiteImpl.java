package ua.com.pandasushi.database.site.dao;

import command.Command;
import ua.com.pandasushi.database.common.*;
import ua.com.pandasushi.database.common.menu.*;
import ua.com.pandasushi.main.GlobalPandaApp;

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
    public boolean alreadyInventory() {
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
    public boolean saveInventory(LinkedHashMap<Integer, ArrayList<Inventory>> inventoryMap) {
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
    public boolean saveRozrobka(Rozrobka rozrobka) {
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
    public boolean saveOperations(ArrayList<Operations> opList) {
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
    public float getDebt(Integer checkID) {
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
    public float getNewAvgCoef(Integer prodId) {
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
    public int getSumProdPurchase(Date from, Date to, Integer productId, Integer kitchId) {
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
    public int getSumU1ProdPurchase(Date from, Date to, Integer productId, Integer kitchId) {
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
    public int getSumProdShift(Date from, Date to, Integer prodIngId, Integer kitchId) {
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
    public int getSumU1ProdShift(Date from, Date to, Integer prodIngId, Integer kitchId) {
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
    public int getIngredientConsumption(Date from, Date to, Integer ingredientId, Integer kitchId) {
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
}

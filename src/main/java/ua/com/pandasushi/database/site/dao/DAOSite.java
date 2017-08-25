package ua.com.pandasushi.database.site.dao;

import ua.com.pandasushi.database.common.*;
import ua.com.pandasushi.database.common.menu.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Taras on 03-Apr-17.
 */
public interface DAOSite extends Serializable {
    void save(Object o);

    void update(Object o);

    void updateList(Collection list);
    
    //Get all orders between from and to
    ArrayList<CustomersSite> getCustomers(Date from, Date to);
    
    //Get orders from kitchen between from and to
    ArrayList<CustomersSite> getCustomers(Date from, Date to, Integer kitchen);
    
    Integer updateImportance(Integer ingId, Integer kitchId, Integer value);

    ArrayList<Float> getSumForType(Integer type);

    ArrayList<Operations> getReportProductPurchase();
    
    ArrayList<Operations> getReportProductPurchase(Date date);

    ArrayList<Operations> getDebtPurchase(Integer checkId);

    Integer getNextCheckID(Integer type);

    Integer getNextInventoryCheckId();

    Boolean alreadyInventory();

    LinkedHashMap<Integer, ArrayList<Inventory>> getTodayInventory();

    Long getLastInventoryId();

    Float getAvgCoefProduct(Integer productId);

    Float getAvgCoefProduct(String productName);

    Float getCoefNF(Integer ingredientId, Integer nfId);

    Boolean saveInventory(LinkedHashMap<Integer, ArrayList<Inventory>> inventoryMap);

    void saveOperation(Operations op);

    ArrayList<Rozrobka> getTodayRozrobka();

    Boolean saveRozrobka(Rozrobka rozrobka);

    Boolean saveOperations(ArrayList<Operations> opList);

    HashMap<Integer, ArrayList<Operations>> getOpenShifts();

    ArrayList<Kitchens> getKitchens();

    Float getDebt(Integer checkID);

    void updateAverageProducts(Integer prodId);

    ArrayList<Employee> getEmployeesForLogin();

    ArrayList<Employee> getCooks();

    ArrayList<Employee> getCouriers();

    ArrayList<String> getContrAgentsForType(Integer type);

    ArrayList<Operations> getOperationsByType(Integer type);

    Operations getLastProductPurchase(Integer productID);

    ArrayList<Rozrobka> getLastRozrobka(Integer ingredientID);

    ArrayList<Operations> getOperations();

    Float getNewAvgCoef(Integer prodId);

    ArrayList<TEHCARDS> getTehcards();

    ArrayList<TEHCARDS> getTehcardsForDish(Integer dishID);

    ArrayList<PRODUCTS_INGREDIENTS> getProdIng();

    PRODUCTS_INGREDIENTS getProdIngForProd(String prod);

    ArrayList<PRODUCTS_INGREDIENTS> getProdIngRozrobka();

    ArrayList<PRODUCTS_INGREDIENTS> getProdForIng(Integer ingredientId);

    Integer getSumProdPurchase(Date from, Date to, Integer productId, Integer kitchId);

    Integer getSumU1ProdPurchase(Date from, Date to, Integer productId, Integer kitchId);

    ArrayList<Operations> getReportProdPurchase(Date from, Date to, Integer productId, Integer kitchen);

    Integer getSumProdShift(Date from, Date to, Integer prodIngId, Integer kitchId);

    Integer getSumU1ProdShift(Date from, Date to, Integer prodIngId, Integer kitchId);

    ArrayList<Operations> getReportProdShift(Date from, Date to, Integer productId, Integer kitchen);

    ArrayList<Consumption> deleteCancelled(Collection<Consumption> cl);

    Integer getIngredientConsumption(Date from, Date to, Integer ingredientId, Integer kitchId);

    ArrayList<Consumption> getIngredientConsumptionList(Date from, Date to, Integer ingredientId, Integer kitch);

    ArrayList<Integer> getRozrobka(Date from, Date to, Integer productId, Integer kitchId);

    ArrayList<Rozrobka> getRozrobkas(Date from, Date to, Integer productId, Integer kitchId);

    ArrayList<Inventory> getLastInventory(Integer basicIngredientId, Integer kitchId);

    ArrayList<Inventory> getLastInventory(Integer basicIngredientId, Integer checkId, Integer kitchid);

    ArrayList<INGREDIENTS> getIngredientsInventory();

    ArrayList<Inventory> getAllInventory();

    ArrayList<Inventory> getInventoryList(Integer checkId);

    ArrayList<TEHCARDS> getNF(Integer ingredientId);

    ArrayList<PRODUCTS_INGREDIENTS> getProductsInventory(Integer ingredientId);

    ArrayList<PRODUCTS> getProducts();

    PRODUCTS getProduct(Integer id);

    Integer getNextProductId();

    Integer getNextProdIngId();

    ArrayList<String> getProdFirstUnits();

    ArrayList<Menu> getMenu();

    ArrayList<INGREDIENTS> getIngredients();

    ArrayList<GROUPS_LIB> getGroups();
    
    ArrayList<Employee> getAllEmployees();
    
    ArrayList<Employee> getEmployeesForSchedule();
    
    ArrayList<Employee> getEmployeesForSchedule(Integer kitchId);
    
    ArrayList<Employee> getEmployeesForSchedule(String position);
    
    KitchProperties getKitchProperty(LocalDate date, Integer kitch);
    
    ArrayList<Schedule> getSchedule(Integer kitchId, LocalDate date, Boolean plan);
    
    ArrayList<Schedule> getEmployeeWorkDays(Integer employeeId, LocalDate start, LocalDate end, Boolean plan);

    HashMap<LocalDate, HashMap<Integer, Schedule>> getSceduleMapPlan(LocalDate start, LocalDate end, String position);

    ArrayList<Schedule> getSceduleListFact(LocalDate date, Integer kitchId);

    void saveSchedule(Schedule schedule);
    
}

package ua.com.pandasushi.database.site.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import ua.com.pandasushi.database.common.*;
import ua.com.pandasushi.database.common.menu.*;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.util.*;

public final class DAOSiteImpl {
    /*private static final SessionFactory sessionFactory = GlobalPandaApp.cfg.buildSessionFactory();
    private static volatile DAOSiteImpl instance;

    private DAOSiteImpl() {
    }

    public static DAOSiteImpl getInstance() {
        DAOSiteImpl localInstance = instance;
        if (localInstance == null)
            synchronized (DAOSiteImpl.class) {
                localInstance = instance;
                if (localInstance == null)
                    instance = localInstance = new DAOSiteImpl();
            }
        return localInstance;
    }

    public Session getSession() {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            e.printStackTrace();
            session = sessionFactory.openSession();
        }
        return session;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void close() {
        getSession().close();
    }

    public void begin() {
        getSession().beginTransaction();
    }

    public void commit() {
        getSession().getTransaction().commit();
    }

    public void rollback() {
        try {
            getSession().getTransaction().rollback();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        try {
            close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

    }

    public void closeSession() {
        try {
            sessionFactory.getCurrentSession().close();
        } catch (Exception e) {
        }
        sessionFactory.close();
    }

    @Override
    public void save(Object o) {
        try {
            begin();
            getSession().save(o);
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Object o) {
        try {
            begin();
            getSession().saveOrUpdate(o);
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void updateList(Collection list) {
        try {
            begin();
            for (Object o : list) {
                o.toString();
                getSession().saveOrUpdate(o);
            }
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
    }

    @Override
    public float[] getSumForType(Integer type) {
        float[] result = new float[3];
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 8);
            Date start = cal.getTime();
            cal.add(Calendar.DATE, 1);
            Date end = cal.getTime();
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(getSession().createCriteria(Operations.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).add(Restrictions.eq("type", type)).add(Restrictions.between("date", start, end)).list());
            commit();
            if (cl != null) {
                result[0] = 0.0f;
                result[1] = 0.0f;
                result[2] = 0.0f;
                for (Operations op : cl) {
                    switch (op.getCurrency()) {
                        case "UAH":
                            result[0] += op.getSum();
                            break;

                        case "USD":
                            result[1] += op.getSum();
                            break;

                        case "EUR":
                            result[2] += op.getSum();
                            break;

                        default:
                            break;
                    }
                }
            }
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getReportProductPurchase() {
        ArrayList<Operations> result = new ArrayList<>();
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 8);
            Date start = cal.getTime();
            cal.add(Calendar.DATE, 1);
            Date end = cal.getTime();
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(getSession().createCriteria(Operations.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).add(Restrictions.between("date", start, end)).add(Restrictions.eq("type", Operations.PRODUCT_PURCHASE)).addOrder(Order.asc("checkId")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        System.out.println(result.size());
        return result;
    }

    @Override
    public ArrayList<Operations> getDebtPurchase(Integer checkId) {
        ArrayList<Operations> result = new ArrayList<>();
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(getSession().createCriteria(Operations.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).add(Restrictions.eq("type", Operations.DEBT_PURCHASE)).add(Restrictions.eq("checkId", checkId)).addOrder(Order.asc("operation_id")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getNextCheckID(Integer type) {
        Integer result = type * 100000 + 1;
        try {
            begin();
            Operations last = (Operations) getSession().createCriteria(Operations.class).add(Restrictions.eq("type", type)).addOrder(Order.desc("operation_id")).setMaxResults(1).uniqueResult();
            commit();
            if (last != null)
                result = last.getCheckId() + 1;
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getNextInventoryCheckId() {
        Integer nextId = (Config.getKitchen().getKitch_id() + 1) * 100000;
        try {
            begin();
            Inventory inv = (Inventory) getSession().createCriteria(Inventory.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).addOrder(Order.desc("begin")).setMaxResults(1).uniqueResult();
            commit();
            if (inv != null)
                nextId = inv.getCheckId() + 1;
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return nextId;
    }

    @Override
    public boolean alreadyInventory() {
        try {
            begin();
            Inventory inv = (Inventory) getSession().createCriteria(Inventory.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).addOrder(Order.desc("begin")).setMaxResults(1).uniqueResult();
            commit();
            if (inv != null) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 8);
                return inv.getBegin().after(cal.getTime());
            }
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public LinkedHashMap<Integer, ArrayList<Inventory>> getTodayInventory() {
        LinkedHashMap<Integer, ArrayList<Inventory>> result = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        try {
            begin();
            Collection<Inventory> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Inventory.class).
                            add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).
                            add(Restrictions.ge("begin", cal.getTime())).addOrder(Order.asc("inventoryId")).list()
            );
            commit();
            for (Inventory inv : cl) {
                if (!result.containsKey(inv.getBasicIng()))
                    result.put(inv.getBasicIng(), new ArrayList<>());
                result.get(inv.getBasicIng()).add(inv);
            }
            for (ArrayList<Inventory> list : result.values())
                list.sort((o1, o2) -> {
                    return o1.getProdIngId().compareTo(o2.getProdIngId());
                });

        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }

        return result;
    }

    @Override
    public Long getLastInventoryId() {
        Long result = (Config.getKitchen().getKitch_id() + 1) * 100000000l;
        try {
            begin();
            Inventory inv = (Inventory) getSession().createCriteria(Inventory.class).
                    add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).
                    addOrder(Order.desc("inventoryId")).setMaxResults(1).uniqueResult();
            if (inv != null)
                result = inv.getInventoryId() + 1;
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Float getAvgCoefProduct(Integer productId) {
        Float result = 0.0f;
        try {
            begin();
            PRODUCTS_INGREDIENTS prodIng = (PRODUCTS_INGREDIENTS) getSession().createCriteria(PRODUCTS_INGREDIENTS.class).add(Restrictions.eq("productId", productId)).setMaxResults(1).uniqueResult();
            commit();

            if (prodIng != null)
                result = prodIng.getAvgCoef();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public Float getAvgCoefProduct(String productName) {
        Float result = 0.0f;
        try {
            begin();
            PRODUCTS_INGREDIENTS prodIng = (PRODUCTS_INGREDIENTS) getSession().createCriteria(PRODUCTS_INGREDIENTS.class).add(Restrictions.eq("productName", productName)).setMaxResults(1).uniqueResult();
            commit();

            if (prodIng != null)
                result = prodIng.getAvgCoef();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public Float getCoefNF(Integer ingredientId, Integer nfId) {
        Float result = 0.0f;
        try {
             begin();
             ArrayList<TEHCARDS> tehcard = new ArrayList<>( getSession().createCriteria(TEHCARDS.class).add(Restrictions.eq("dishId", nfId)).list() );
             commit();
             float totalWeight = 0.0f;
             float ingWeight = 0.0f;
             for (TEHCARDS th : tehcard) {
                 totalWeight += th.getFinalWeight();
                 if (th.getIngredientId().equals(ingredientId))
                     ingWeight = th.getCount();
             }
             result = ingWeight / totalWeight;
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        if (result > 0)
            return result;
        else
            return 1.0f;
    }

    @Override
    public boolean saveInventory(LinkedHashMap<Integer, ArrayList<Inventory>> inventoryMap) {
        Long inventoryId = getLastInventoryId();
        try {
            begin();
            for (ArrayList<Inventory> list : inventoryMap.values()) {
                for (Inventory inv : list) {
                    if (inv.getInventoryId() == null)
                        inv.setInventoryId(inventoryId++);
                    getSession().saveOrUpdate(inv);
                }
            }
            commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
            return false;
        }
        return true;
    }

    @Override
    public void saveOperation(Operations op) {
        try {
            begin();
            Operations last = (Operations) getSession().createCriteria(Operations.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).addOrder(Order.desc("operation_id")).setMaxResults(1).uniqueResult();
            Integer nextId;
            if (last != null)
                nextId = last.getOperation_id() + 1;
            else
                nextId = (Config.getKitchen().getKitch_id() + 1) * 10000000;
            if (op.getOperation_id() == null)
                op.setOperation_id(nextId);
            getSession().saveOrUpdate(op);
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Rozrobka> getTodayRozrobka() {
        ArrayList<Rozrobka> result = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, 8);
        end.add(Calendar.DATE, 1);
        end.set(Calendar.HOUR_OF_DAY, 2);
        try {
            begin();
            Collection cl = new LinkedHashSet(getSession().createCriteria(Rozrobka.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).add(Restrictions.between("date", start.getTime(), end.getTime())).list());
            commit();
            result.addAll(cl);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean saveRozrobka(Rozrobka rozrobka) {
        try {
            begin();
            Rozrobka last = (Rozrobka) getSession().createCriteria(Rozrobka.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).addOrder(Order.desc("rozrobkaId")).setMaxResults(1).uniqueResult();
            long nextId = 0;
            if (last != null)
                nextId = last.getRozrobkaId() + 1;
            else
                nextId = (Config.getKitchen().getKitch_id() + 1) * 10000000;
            rozrobka.setRozrobkaId(nextId);
            getSession().save(rozrobka);
            commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOperations(ArrayList<Operations> opList) {
        try {
            begin();
            Operations last = (Operations) getSession().createCriteria(Operations.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).addOrder(Order.desc("operation_id")).setMaxResults(1).uniqueResult();
            Integer nextId;
            if (last != null)
                nextId = last.getOperation_id() + 1;
            else
                nextId = (Config.getKitchen().getKitch_id() + 1) * 10000000;
            for (Operations op : opList) {
                if (op.getOperation_id() == null)
                    op.setOperation_id(nextId++);
                getSession().saveOrUpdate(op);
            }
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public HashMap<Integer, ArrayList<Operations>> getOpenShifts() {
        ArrayList<Operations> ops = new ArrayList<>();
        HashMap<Integer, ArrayList<Operations>> result = new HashMap<>();
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, 8);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, 1);
        end.set(Calendar.HOUR_OF_DAY, 8);
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Operations.class).add(Restrictions.eq("type", Operations.PRODUCT_SHIFT))
                            .add(Restrictions.or(Restrictions.eq("boolparameter2", false), Restrictions.between("date", start.getTime(), end.getTime()))).list()
            );
            if (cl != null)
                ops.addAll(cl);
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        for (Operations op : ops) {
            if (result.containsKey(op.getCheckId())) {
                result.get(op.getCheckId()).add(op);
            } else {
                result.put(op.getCheckId(), new ArrayList<>());
                result.get(op.getCheckId()).add(op);
            }
        }
        return result;
    }

    @Override
    public ArrayList<Kitchens> getKitchens() {
        ArrayList<Kitchens> result = new ArrayList<>();
        try {
            begin();
            Collection<Kitchens> cl = new LinkedHashSet<>(getSession().createCriteria(Kitchens.class).addOrder(Order.asc("kitch_id")).list());
            commit();
            if (cl != null && !cl.isEmpty())
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public float getDebt(Integer checkID) {
        ArrayList<Operations> ops = new ArrayList<>();
        float debt = 0.0f;
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Operations.class).add(Restrictions.eq("checkId", checkID)).list()
            );
            if (cl != null)
                ops.addAll(cl);
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        if (ops.isEmpty())
            return -1;
        for (Operations op : ops)
            debt += op.getSum();
        return debt;
    }

    @Override
    public void updateAverageProducts(Integer prodId) {
        try {
            begin();
            ArrayList<Operations> cl = new ArrayList(getSession().createCriteria(Operations.class).
                    add(Restrictions.eq("type", Operations.PRODUCT_PURCHASE)).add(Restrictions.eq("intparameter1", prodId.floatValue())).addOrder(Order.desc("date")).list());
            commit();
            if (!cl.isEmpty()) {
                float totalPrice = 0.0f;
                float totalCount = 0.0f;
                PRODUCTS_INGREDIENTS prodIng = getProdIngForProd(cl.get(0).getStrparameter1());
                PRODUCTS prod = getProduct(prodId);
                prod.setLastPriceCur(cl.get(0).getIntparameter4());
                for (int i = 0; i < cl.size(); i++) {
                    totalPrice += cl.get(i).getSum().floatValue();
                    totalCount += cl.get(i).getFloatparameter4().floatValue();
                }
                prod.setAvgPriceCur(1000 * totalPrice / totalCount);
                if (cl.get(0).getCurrency().equals("UAH")) {
                    prod.setAvgPriceUah(prod.getAvgPriceCur());
                    prod.setLastPriceUah(prod.getLastPriceUah());
                } else {
                    prod.setAvgPriceUah(prod.getAvgPriceCur() * prod.getCurToUah());
                    prod.setLastPriceUah(prod.getLastPriceCur() * prod.getCurToUah());
                }
                if (prodIng != null) {
                    prodIng.setAvgPrice(prod.getAvgPriceUah());
                    update(prodIng);
                }
                update(prod);
            }
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Employee> getEmployeesForLogin() {
        ArrayList<Employee> result = new ArrayList<>();
        try {
            begin();
            Collection<Employee> cl = new LinkedHashSet<>(getSession().createCriteria(Employee.class).add(Restrictions.eq("gpAccess", true)).add(Restrictions.ne("password", "1111")).list());
            commit();
            if (cl != null && !cl.isEmpty())
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        if (!result.isEmpty())
            Collections.sort(result, (o1, o2) -> {
                return o2.getPosition().compareTo(o1.getPosition());
            });
        return result;
    }

    @Override
    public ArrayList<Employee> getCooks() {
        ArrayList<Employee> result = new ArrayList<>();
        try {
            begin();
            Collection<Employee> cl = new LinkedHashSet<>(getSession().createCriteria(Employee.class).add(Restrictions.eq("active", true)).add(Restrictions.eq("position", "Кухар")).addOrder(Order.asc("name")).list());
            commit();
            if (cl != null && !cl.isEmpty())
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Employee> getCouriers() {
        ArrayList<Employee> result = new ArrayList<>();
        try {
            begin();
            Collection<Employee> cl = new LinkedHashSet<>(getSession().createCriteria(Employee.class).add(Restrictions.eq("active", true)).add(Restrictions.eq("position", "Кур'єр")).addOrder(Order.asc("name")).list());
            commit();
            if (cl != null && !cl.isEmpty())
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<String> getContrAgentsForType(Integer type) {
        ArrayList<String> result = new ArrayList<>();
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(getSession().createCriteria(Operations.class).add(Restrictions.eq("type", type)).list());
            commit();
            if (cl != null) {
                HashSet<String> set = new HashSet<>();
                for (Operations op : cl) {
                    set.add(op.getContrAgent());
                }
                result.addAll(set);
            }
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getOperationsByType(Integer type) {
        ArrayList<Operations> result = new ArrayList<>();
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(getSession().createCriteria(Operations.class).add(Restrictions.eq("type", type)).addOrder(Order.desc("date")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Operations getLastProductPurchase(Integer productID) {
        Operations op = null;
        try {
            begin();
            op = (Operations) getSession().createCriteria(Operations.class).add(Restrictions.eq("type", Operations.PRODUCT_PURCHASE))
                    .add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).add(Restrictions.eq("intparameter1", productID.floatValue()))
                    .addOrder(Order.desc("date")).setMaxResults(1).uniqueResult();
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return op;
    }

    @Override
    public ArrayList<Rozrobka> getLastRozrobka(Integer ingredientID) {
        ArrayList<Rozrobka> result = new ArrayList<>();
        try {
            begin();
            Collection<Rozrobka> cl = new LinkedHashSet<>(getSession().createCriteria(Rozrobka.class).
                    add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).add(Restrictions.eq("ingredientId", ingredientID)).
                    addOrder(Order.desc("date")).setMaxResults(10).list());
            commit();
            result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getOperations() {
        ArrayList<Operations> result = new ArrayList<>();
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(getSession().createCriteria(Operations.class).addOrder(Order.asc("operation_id")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public float getNewAvgCoef(Integer prodId) {
        float newAvgCoef = 0.0f;
        try {
            begin();
            //TODO 3 month
            Collection<Rozrobka> cl = new LinkedHashSet<>(getSession().createCriteria(Rozrobka.class).add(Restrictions.eq("productId", prodId)).list());
            commit();
            for (Rozrobka r : cl)
                newAvgCoef += r.getCoef();
            newAvgCoef /= (cl.size());
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return newAvgCoef;
    }

    @Override
    public ArrayList<TEHCARDS> getTehcards() {
        ArrayList<TEHCARDS> result = new ArrayList<>();
        try {
            begin();
            Collection<TEHCARDS> cl = new LinkedHashSet<>(getSession().createCriteria(TEHCARDS.class).addOrder(Order.asc("tehcard_id")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<TEHCARDS> getTehcardsForDish(Integer dishID) {
        ArrayList<TEHCARDS> result = new ArrayList<>();
        try {
            begin();
            Collection<TEHCARDS> cl = new LinkedHashSet<>(getSession().createCriteria(TEHCARDS.class).add(Restrictions.eq("dishId", dishID)).addOrder(Order.asc("tehcard_id")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PRODUCTS_INGREDIENTS> getProdIng() {
        ArrayList<PRODUCTS_INGREDIENTS> result = new ArrayList<>();
        try {
            begin();
            Collection<PRODUCTS_INGREDIENTS> cl = new LinkedHashSet<>(getSession().createCriteria(PRODUCTS_INGREDIENTS.class).addOrder(Order.asc("prodIngId")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PRODUCTS_INGREDIENTS getProdIngForProd(String prod) {
        PRODUCTS_INGREDIENTS result = null;
        try {
            begin();
            PRODUCTS_INGREDIENTS cl = (PRODUCTS_INGREDIENTS) getSession().createCriteria(PRODUCTS_INGREDIENTS.class).add(Restrictions.eq("productName", prod)).setMaxResults(1).uniqueResult();
            commit();
            if (cl != null)
                result = cl;
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PRODUCTS_INGREDIENTS> getProdIngRozrobka() {
        ArrayList<PRODUCTS_INGREDIENTS> result = new ArrayList<>();
        try {
            begin();
            Collection<PRODUCTS_INGREDIENTS> cl = new LinkedHashSet<>(getSession().createCriteria(PRODUCTS_INGREDIENTS.class).add(Restrictions.eq("auto", false)).addOrder(Order.asc("prodIngId")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PRODUCTS_INGREDIENTS> getProdForIng(Integer ingredientId) {
        ArrayList<PRODUCTS_INGREDIENTS> result = new ArrayList<>();
        try {
            begin();
            Collection<PRODUCTS_INGREDIENTS> cl = new LinkedHashSet<>(getSession().createCriteria(PRODUCTS_INGREDIENTS.class).add(Restrictions.eq("ingredientId", ingredientId)).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getSumProdPurchase(Date from, Date to, Integer productId, Integer kitchId) {
        int result = 0;
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Operations.class).add(Restrictions.eq("kitchen", kitchId)).
                            add(Restrictions.between("date", from, to)).add(Restrictions.eq("type", Operations.PRODUCT_PURCHASE)).add(Restrictions.eq("intparameter1", productId.floatValue())).list()
            );
            commit();
            if (cl != null)
                for (Operations op : cl)
                    result += op.getFloatparameter4();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public int getSumU1ProdPurchase(Date from, Date to, Integer productId, Integer kitchId) {
        int result = 0;
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Operations.class).add(Restrictions.eq("kitchen", kitchId)).
                            add(Restrictions.between("date", from, to)).add(Restrictions.eq("type", Operations.PRODUCT_PURCHASE)).add(Restrictions.eq("intparameter1", productId.floatValue())).list()
            );
            commit();
            if (cl != null)
                for (Operations op : cl)
                    result += op.getFloatparameter3();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public ArrayList<Operations> getReportProdPurchase(Date from, Date to, Integer productId, Integer kitchen) {
        ArrayList<Operations> result = new ArrayList<>();
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Operations.class).add(Restrictions.eq("kitchen", kitchen)).
                            add(Restrictions.between("date", from, to)).add(Restrictions.eq("type", Operations.PRODUCT_PURCHASE)).add(Restrictions.eq("intparameter1", productId.floatValue())).list()
            );
            commit();
            result.addAll(cl);
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public int getSumProdShift(Date from, Date to, Integer prodIngId, Integer kitchId) {
        int result = 0;
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Operations.class).add(Restrictions.eq("type", Operations.PRODUCT_SHIFT)).
                            add(Restrictions.or(Restrictions.eq("kitchen", kitchId), Restrictions.eq("intparameter2", kitchId.floatValue()))).add(Restrictions.between("date", from, to)).
                            add(Restrictions.eq("intparameter1", prodIngId.floatValue())).list()
            );
            commit();
                for (Operations op : cl)
                    if (op.getKitchen() == kitchId)
                        result -= op.getFloatparameter2();
                    else if (op.getIntparameter2().intValue() == kitchId)
                        result += op.getFloatparameter4();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }

        return result;
    }

    @Override
    public int getSumU1ProdShift(Date from, Date to, Integer prodIngId, Integer kitchId) {
        int result = 0;
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Operations.class).add(Restrictions.eq("type", Operations.PRODUCT_SHIFT)).
                            add(Restrictions.or(Restrictions.eq("kitchen", kitchId), Restrictions.eq("intparameter2", kitchId.floatValue()))).add(Restrictions.between("date", from, to)).
                            add(Restrictions.eq("intparameter1", prodIngId.floatValue())).list()
            );
            commit();
            for (Operations op : cl)
                if (op.getKitchen() == kitchId)
                    result -= op.getFloatparameter1();
                else if (op.getIntparameter2().intValue() == kitchId)
                    result += op.getFloatparameter3();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }

        return result;
    }

    @Override
    public ArrayList<Operations> getReportProdShift(Date from, Date to, Integer productId, Integer kitchen) {
        ArrayList<Operations> result = new ArrayList<>();
        try {
            begin();
            Collection<Operations> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Operations.class).add(Restrictions.or(Restrictions.eq("kitchen", kitchen), Restrictions.eq("intparameter2", kitchen.floatValue()))).
                            add(Restrictions.between("date", from, to)).add(Restrictions.eq("type", Operations.PRODUCT_SHIFT)).add(Restrictions.eq("intparameter1", productId.floatValue())).list()
            );
            commit();
            result.addAll(cl);
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public ArrayList<Consumption> deleteCancelled(Collection<Consumption> cl) {
        ArrayList<Consumption> result = new ArrayList<>(cl);

        try {
            begin();
            HashSet<Long> ids = new HashSet<>();
            for (Consumption cons : cl)
                ids.add(cons.getCustomerID());
            ArrayList<CustomersSite> list = new ArrayList<>( getSession().createCriteria(CustomersSite.class).add(Restrictions.in("site_id", ids)).list() );
            for (Iterator<Consumption> it = cl.iterator(); it.hasNext(); ) {
                Consumption cons = it.next();
                for (CustomersSite cust : list )
                    if (cons.getCustomerID() == cust.getSite_id().longValue()) {
                        if (cust.isCanceled() || cust.getSendTime() == null)
                            it.remove();
                        break;
                    }
            }
            commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public int getIngredientConsumption(Date from, Date to, Integer ingredientId, Integer kitchId) {
        int result = 0;
        try {
            begin();
            Collection<Consumption> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Consumption.class).
                            add(Restrictions.eq("ingredientId", ingredientId)).
                            add(Restrictions.eq("kitchen", kitchId)).
                            add(Restrictions.between("date", from, to)).
                            list()
            );
            commit();
            if (cl != null && !cl.isEmpty())
                for (Consumption con : deleteCancelled(cl))
                    result += con.getWeight();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public ArrayList<Consumption> getIngredientConsumptionList(Date from, Date to, Integer ingredientId, Integer kitch) {
        ArrayList<Consumption> result = new ArrayList<>();
        try {
            begin();
            Collection<Consumption> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Consumption.class).
                            add(Restrictions.eq("ingredientId", ingredientId)).
                            add(Restrictions.eq("kitchen", kitch)).
                            add(Restrictions.between("date", from, to)).
                            list()
            );
            commit();
            if (!cl.isEmpty())
                result.addAll(deleteCancelled(cl));
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public int[] getRozrobka(Date from, Date to, Integer productId, Integer kitchId) {
        int[] result = new int[3];
        result[0] = 0;
        result[1] = 0;
        result[2] = 0;
        try {
            begin();
            Collection<Rozrobka> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Rozrobka.class).
                            add(Restrictions.eq("kitchen", kitchId)).
                            add(Restrictions.between("date", from, to)).
                            add(Restrictions.eq("productId", productId)).list()
            );
            for (Rozrobka rozrobka : cl) {
                result[0] -= rozrobka.getCountU2();
                result[1] += rozrobka.getCountIng();
                result[2] -= rozrobka.getCountU1();
            }
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Rozrobka> getRozrobkas(Date from, Date to, Integer productId, Integer kitchId) {
        ArrayList<Rozrobka> result = new ArrayList<>();
        try {
            begin();
            Collection<Rozrobka> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Rozrobka.class).
                            add(Restrictions.eq("kitchen", kitchId)).
                            add(Restrictions.between("date", from, to)).
                            add(Restrictions.eq("productId", productId)).list()
            );
            commit();
            result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Inventory> getLastInventory(Integer basicIngredientId) {
        ArrayList<Inventory> result = new ArrayList<>();
        try {
            begin();
            Collection<Inventory> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Inventory.class).add(Restrictions.eq("kitchen", Config.getKitchen().getKitch_id())).
                            add(Restrictions.eq("basicIng", basicIngredientId)).addOrder(Order.desc("inventoryId")).list()
            );
            commit();
            if (cl != null && !cl.isEmpty()) {
                result.addAll(cl);
                Date last = result.get(0).getBegin();
                for (Inventory inv : result)
                    if (inv.getBegin().after(last))
                        last = inv.getBegin();

                for (Iterator<Inventory> it = result.iterator(); it.hasNext(); )
                    if (!it.next().getBegin().equals(last))
                        it.remove();
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public ArrayList<Inventory> getLastInventory(Integer basicIngredientId, Integer checkId, Integer kitchid) {
        ArrayList<Inventory> result = new ArrayList<>();
        try {
            begin();
            Collection<Inventory> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Inventory.class).add(Restrictions.eq("kitchen", kitchid)).
                            add(Restrictions.eq("basicIng", basicIngredientId)).add(Restrictions.lt("checkId", checkId)).addOrder(Order.desc("inventoryId")).list()
            );
            commit();
            if (cl != null && !cl.isEmpty()) {
                result.addAll(cl);
                for (Iterator<Inventory> it = result.iterator(); it.hasNext(); ) {
                    if (!it.next().getBegin().equals(result.get(0).getBegin()))
                        it.remove();
                }
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return result;
    }

    @Override
    public ArrayList<INGREDIENTS> getIngredientsInventory() {
        ArrayList<INGREDIENTS> result = new ArrayList<>();
        try {
            begin();
            Collection<INGREDIENTS> cl = new LinkedHashSet<>(
                    getSession().createCriteria(INGREDIENTS.class).add(Restrictions.le("ingredientId", 1699)).add(Restrictions.eq("onInventary", true)).list()
            );
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Inventory> getAllInventory() {
        ArrayList<Inventory> list = new ArrayList<>();
        try {
            begin();
            Collection<Inventory> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Inventory.class).addOrder(Order.desc("begin")).list()
            );
            if (!cl.isEmpty())
                list.addAll(cl);
            commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return list;
    }

    @Override
    public ArrayList<Inventory> getInventoryList(Integer checkId) {
        ArrayList<Inventory> list = new ArrayList<>();
        try {
            begin();
            Collection<Inventory> cl = new LinkedHashSet<>(
                    getSession().createCriteria(Inventory.class).add(Restrictions.eq("checkId", checkId)).addOrder(Order.asc("prodIngId")).list()
            );
            if (!cl.isEmpty())
                list.addAll(cl);
            commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            rollback();
        }
        return list;
    }

    @Override
    public ArrayList<TEHCARDS> getNF(Integer ingredientId) {
        ArrayList<TEHCARDS> result = new ArrayList<>();
        try {
            begin();
            Collection<TEHCARDS> cl = new LinkedHashSet<>(
                    getSession().createCriteria(TEHCARDS.class).add(Restrictions.eq("ingredientId", ingredientId)).
                            add(Restrictions.between("dishId", 1700, 1900)).list()
            );
            commit();
            result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PRODUCTS_INGREDIENTS> getProductsInventory(Integer ingredientId) {
        ArrayList<PRODUCTS_INGREDIENTS> result = new ArrayList<>();
        try {
            begin();
            Collection<PRODUCTS_INGREDIENTS> cl = new LinkedHashSet<>(
                    getSession().createCriteria(PRODUCTS_INGREDIENTS.class).add(Restrictions.eq("ingredientId", ingredientId)).list()
            );
            commit();
            result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PRODUCTS> getProducts() {
        ArrayList<PRODUCTS> result = new ArrayList<>();
        try {
            begin();
            Collection<PRODUCTS> cl = new LinkedHashSet<>(getSession().createCriteria(PRODUCTS.class).addOrder(Order.asc("productId")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PRODUCTS getProduct(Integer id) {
        PRODUCTS result = null;
        try {
            begin();
            PRODUCTS cl = (PRODUCTS) getSession().createCriteria(PRODUCTS.class).add(Restrictions.eq("productId", id)).setMaxResults(1).uniqueResult();
            commit();
            if (cl != null)
                result = cl;
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getNextProductId() {
        Integer integer = null;
        try {
            begin();
            PRODUCTS prod = (PRODUCTS) getSession().createCriteria(PRODUCTS.class).addOrder(Order.desc("productId")).setMaxResults(1).uniqueResult();
            integer = prod.getProductId();
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return ++integer;
    }

    @Override
    public Integer getNextProdIngId() {
        Integer integer = null;
        try {
            begin();
            PRODUCTS_INGREDIENTS prodIng = (PRODUCTS_INGREDIENTS) getSession().createCriteria(PRODUCTS_INGREDIENTS.class).addOrder(Order.desc("prodIngId")).setMaxResults(1).uniqueResult();
            integer = prodIng.getProdIngId();
            commit();
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return ++integer;
    }

    @Override
    public ArrayList<String> getProdFirstUnits() {
        ArrayList<String> result = new ArrayList<>();
        try {
            begin();
            Collection<PRODUCTS> cl = new LinkedHashSet<>(getSession().createCriteria(PRODUCTS.class).list());
            commit();
            if (cl != null) {
                HashSet<String> set = new HashSet<>();
                for (PRODUCTS prod : cl)
                    set.add(prod.getFirstUnits());
                result.addAll(set);
            }
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Menu> getMenu() {
        ArrayList<Menu> result = new ArrayList<>();
        try {
            begin();
            Collection<Menu> cl = new LinkedHashSet<>(getSession().createCriteria(Menu.class).addOrder(Order.asc("code")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<INGREDIENTS> getIngredients() {
        ArrayList<INGREDIENTS> result = new ArrayList<>();
        try {
            begin();
            Collection<INGREDIENTS> cl = new LinkedHashSet<>(getSession().createCriteria(INGREDIENTS.class).addOrder(Order.asc("ingredientName")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<GROUPS_LIB> getGroups() {
        ArrayList<GROUPS_LIB> result = new ArrayList<>();
        try {
            begin();
            Collection<GROUPS_LIB> cl = new LinkedHashSet<>(getSession().createCriteria(GROUPS_LIB.class).addOrder(Order.asc("group_id")).list());
            commit();
            if (cl != null)
                result.addAll(cl);
        } catch (HibernateException e) {
            rollback();
            e.printStackTrace();
        }
        return result;
    }*/

}

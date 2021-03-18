package ua.com.pandasushi.controller.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import ua.com.pandasushi.database.common.Operations;
import ua.com.pandasushi.database.common.menu.*;
import ua.com.pandasushi.database.common.menu.Menu;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class ExcelService {
    private static final String MENU_SHEET = "Страви";
    public static final String TEHCARD_SHEET = "Техкарти";
    public static final String PRODUCTS_INGREDIENTS_SHEET = "ПродуктиІнгредієнти";
    public static final String INGREDIENTS_SHEET = "Інгредієнти";
    public static final String PRODUCTS_SHEET = "Продукти";
    private static final String GROUPS_SHEET = "Групи";
    private static final String STREET_SHEET = "Вулиці";

    private static XSSFCellStyle dateStyle;
    private static XSSFCellStyle intNumStyle;
    private static XSSFCellStyle floNumStyle;

    private static void menu (XSSFWorkbook wb) {
        Sheet menuSheet = wb.getSheet(MENU_SHEET);
        Iterator<Row> it = menuSheet.rowIterator();
        it.next();
        it.next();
        it.next();

        ArrayList<Menu> menuList = new ArrayList<>();
        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            Menu dish = new Menu();
            try {
                dish.setCode((int) cells.next().getNumericCellValue());
                dish.setDish(cells.next().getStringCellValue());
                dish.setPrice((int) cells.next().getNumericCellValue());
                dish.setSCode(cells.next().getStringCellValue());
                dish.setUnitsTehcard(cells.next().getStringCellValue());
                dish.setWeight((int) cells.next().getNumericCellValue());
                dish.setUnits(cells.next().getStringCellValue());
                dish.setGinger((int) cells.next().getNumericCellValue());
                dish.setColdHotAdds(cells.next().getStringCellValue());
                dish.setUnagi((int) cells.next().getNumericCellValue());
                dish.setPizzaSushiDrinks(cells.next().getStringCellValue());
                dish.setDirection(cells.next().getStringCellValue());
                dish.setImageUrl(cells.next().getStringCellValue());
                dish.setGroup_id((int) cells.next().getNumericCellValue());
                dish.setGroupName(cells.next().getStringCellValue());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(row.getRowNum());
            }
            menuList.add(dish);
        }

        GlobalPandaApp.site.updateList(menuList);
    }

    private static void tehcards (XSSFWorkbook wb) {
        Sheet tehcardSheet = wb.getSheet(TEHCARD_SHEET);
        Iterator<Row> itT = tehcardSheet.iterator();
        itT.next();
        itT.next();
        itT.next();

        ArrayList<TEHCARDS> tehcardList = new ArrayList<>();
        while (itT.hasNext()) {
            Row row = itT.next();
            Iterator<Cell> cells = row.iterator();
            TEHCARDS tehcard = new TEHCARDS();

            try {
                tehcard.setTehcard_id((int) cells.next().getNumericCellValue());
                tehcard.setDishName(cells.next().getStringCellValue());
                tehcard.setDishId((int) cells.next().getNumericCellValue());
                tehcard.setIngredientName(cells.next().getStringCellValue());
                tehcard.setIngredientId((int) cells.next().getNumericCellValue());
                tehcard.setUnits(cells.next().getStringCellValue());
                tehcard.setCount((float) cells.next().getNumericCellValue());
                tehcard.setUsushka((int) cells.next().getNumericCellValue());
                tehcard.setFinalWeight((int) cells.next().getNumericCellValue());
            } catch (Exception e ) {
                e.printStackTrace();
                System.out.println(row.getRowNum());
            }
            tehcardList.add(tehcard);
        }

        GlobalPandaApp.site.updateList(tehcardList);
    }

    private static void productsIngredients (XSSFWorkbook wb) {
        Sheet productIngredientSheet = wb.getSheet(PRODUCTS_INGREDIENTS_SHEET);
        Iterator<Row> itPI = productIngredientSheet.iterator();
        itPI.next();
        itPI.next();

        ArrayList<PRODUCTS_INGREDIENTS> prodIngList = new ArrayList<>();
        while(itPI.hasNext()) {
            Row row = itPI.next();
            Iterator<Cell> cells = row.iterator();
            PRODUCTS_INGREDIENTS prIng = new PRODUCTS_INGREDIENTS();
            try {
                prIng.setProdIngId( (int) cells.next().getNumericCellValue() );
                prIng.setProductName( cells.next().getStringCellValue() );
                prIng.setProductId( (int) cells.next().getNumericCellValue() );
                prIng.setIngredientName( cells.next().getStringCellValue() );
                prIng.setIngredientId( (int) cells.next().getNumericCellValue() );
                prIng.setUnits( cells.next().getStringCellValue() );
                prIng.setLastCheck( cells.next().getDateCellValue() );
                prIng.setLastCoef( (float) cells.next().getNumericCellValue() );
                prIng.setAvgCoef( (float) cells.next().getNumericCellValue() );
                prIng.setAvgPrice( (float) cells.next().getNumericCellValue() );
                prIng.setAuto( cells.next().getStringCellValue().equals("Auto") );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(row.getRowNum());
            }
            prodIngList.add(prIng);
        }

        GlobalPandaApp.site.updateList(prodIngList);

    }

    private static void ingredients (XSSFWorkbook wb) {
        Sheet ingredientsSheet = wb.getSheet(INGREDIENTS_SHEET);
        Iterator<Row> itI = ingredientsSheet.iterator();
        itI.next();
        itI.next();
        itI.next();

        ArrayList<INGREDIENTS> ingredientsList = new ArrayList<>();
        while (itI.hasNext()) {
            Row row = itI.next();
            Iterator<Cell> cells = row.iterator();
            INGREDIENTS ing = new INGREDIENTS();
            try {
                ing.setIngredientName( cells.next().getStringCellValue() );
                ing.setIngredientId( (int) cells.next().getNumericCellValue() );
                ing.setUnits( cells.next().getStringCellValue() );
                ing.setExpiredPeriod( (int) cells.next().getNumericCellValue() );
                ing.setImageUrl( cells.next().getStringCellValue() );
                ing.setSyhivImportance( (int) cells.next().getNumericCellValue() );
                ing.setVarshavImportance( (int) cells.next().getNumericCellValue() );
                ing.setOnInventary( (int) cells.next().getNumericCellValue() == 1 ? true : false );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(row.getRowNum());
            }
            ingredientsList.add(ing);
        }
        GlobalPandaApp.site.updateList(ingredientsList);
    }

    private static void products(XSSFWorkbook wb) {
        Sheet productsSheet = wb.getSheet(PRODUCTS_SHEET);
        Iterator<Row> itP = productsSheet.iterator();
        itP.next();
        itP.next();
        itP.next();
        ArrayList<PRODUCTS> productsList = new ArrayList<>();

        while (itP.hasNext()) {
            Row row = itP.next();
            Iterator<Cell> cells = row.iterator();
            PRODUCTS prod = new PRODUCTS();
            try {
                prod.setProductName( cells.next().getStringCellValue() );
                prod.setProductId( (int) cells.next().getNumericCellValue() );
                prod.setFirstUnits( cells.next().getStringCellValue() );
                prod.setSecondUnits( cells.next().getStringCellValue() );
                prod.setUnitsRelation( (int) cells.next().getNumericCellValue() );
                prod.setExpiredPeriod( (int) cells.next().getNumericCellValue() );
                prod.setCurrency( cells.next().getStringCellValue() );
                prod.setLastPriceCur( (float) cells.next().getNumericCellValue() );
                prod.setAvgPriceCur( (float) cells.next().getNumericCellValue() );
                prod.setCurToUah( (float) cells.next().getNumericCellValue() );
                prod.setLastPriceUah( (float) cells.next().getNumericCellValue() );
                prod.setAvgPriceUah( (float) cells.next().getNumericCellValue() );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(row.getRowNum());
            }
            productsList.add(prod);
        }
        GlobalPandaApp.site.updateList(productsList);
    }

    private static void streets (XSSFWorkbook wb) {
        Sheet streetSheet = wb.getSheet(STREET_SHEET);
        if( streetSheet != null ) {
            Iterator<Row> itS = streetSheet.iterator();
            itS.next();
            ArrayList<Streets> streets = new ArrayList<>();
            while ( itS.hasNext() ) {
                Row row = itS.next();
                Iterator<Cell> cells = row.iterator();
                Streets street = new Streets();
                try {
                    street.setId( (int) cells.next().getNumericCellValue());
                    street.setStreet( cells.next().getStringCellValue() );
                    street.setRegion( cells.next().getStringCellValue() );
                    street.setKitchen( (int) cells.next().getNumericCellValue() );
                    street.setMinOrder( (int) cells.next().getNumericCellValue() );
                    streets.add(street);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(row.getRowNum());
                }
            }
            GlobalPandaApp.site.updateList(streets);
        }
    }

    private static void groups (XSSFWorkbook wb) {
        Sheet groupsSheet = wb.getSheet(GROUPS_SHEET);
        Iterator<Row> itG = groupsSheet.iterator();
        itG.next();
        itG.next();
        itG.next();

        ArrayList<GROUPS_LIB> groupList = new ArrayList<>();
        while (itG.hasNext()) {
            Row row = itG.next();
            Iterator<Cell> cells = row.iterator();
            GROUPS_LIB group = new GROUPS_LIB();
            try {
                group.setGroup_id( (int) cells.next().getNumericCellValue() );
                group.setName( cells.next().getStringCellValue() );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(row.getRowNum());
            }
            groupList.add(group);
        }

        GlobalPandaApp.site.updateList(groupList);
    }

    public static void parse (File file) {
        InputStream is;
        XSSFWorkbook wb = null;

        try {
            is = new FileInputStream(file);
            wb = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(wb != null) {
            menu(wb);
            tehcards(wb);
            productsIngredients(wb);
            ingredients(wb);
            products(wb);
            groups(wb);
            streets(wb);
        }
    }

    public static void writeToFile (File file) {
        XSSFWorkbook book = new XSSFWorkbook();

        dateStyle = book.createCellStyle();
        intNumStyle = book.createCellStyle();
        floNumStyle = book.createCellStyle();

        XSSFDataFormat df = book.createDataFormat();

        dateStyle.setDataFormat(df.getFormat("dd.mm.yyyy hh:mm"));
        intNumStyle.setDataFormat(df.getFormat("0"));
        floNumStyle.setDataFormat(df.getFormat("0.00"));


        System.out.println("Fill menu sheet");
        fillMenuSheet( book.createSheet(MENU_SHEET) );

        System.out.println("Fill tehcard sheet");
        fillTehcardSheet( book.createSheet(TEHCARD_SHEET), null );

        System.out.println("Fill proding sheet");
        fillProdIngSheet( book.createSheet(PRODUCTS_INGREDIENTS_SHEET), null );

        System.out.println("Fill ing sheet");
        fillIngredientSheet( book.createSheet(INGREDIENTS_SHEET), null );

        System.out.println("Fill prod sheet");
        fillProdSheet( book.createSheet(PRODUCTS_SHEET), null );

        System.out.println("Fill groups sheet");
        fillGroupSheet( book.createSheet(GROUPS_SHEET) );

        try {
            book.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Desktop desktop = null;

        if(Desktop.isDesktopSupported())
            desktop = Desktop.getDesktop();

        if( desktop != null )
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }



    public static void createReportProductPurchase (File file, Date date) {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFDataFormat df = book.createDataFormat();

        dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(df.getFormat("dd.mm.yyyy hh:mm"));
        intNumStyle = book.createCellStyle();
        intNumStyle.setDataFormat(df.getFormat("0"));
        floNumStyle = book.createCellStyle();
        floNumStyle.setDataFormat(df.getFormat("0.00"));

        fillProductsResult( book.createSheet("Підсумок"), date );
        fillProductsPurchaseDetails( book.createSheet("Деталі"), date );

        try {
            book.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Desktop desktop = null;

        if(Desktop.isDesktopSupported())
            desktop = Desktop.getDesktop();

        if( desktop != null )
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static void fillProductsPurchaseDetails (Sheet detailsSheet, Date date) {
        Row header = detailsSheet.createRow(0);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("CHECK_ID");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Дата");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Контрагент");
        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Продукт");
        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Кількість");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Вага");
        header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Сума");
        header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Валюта");


        ArrayList<Operations> list = GlobalPandaApp.site.getReportProductPurchase(date);
        if ( list != null && !list.isEmpty() ) {
            int rowNum = 1;
            int checkId = list.get(0).getCheckId();
            for (Operations op : list) {
                if (op.getCheckId() != checkId) {
                    rowNum++;
                    checkId = op.getCheckId();
                }
                Row row = detailsSheet.createRow( rowNum++ );
                row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(op.getCheckId());
                row.getCell(0).setCellStyle(intNumStyle);
                row.createCell(1).setCellValue(op.getDate());
                row.getCell(1).setCellStyle(dateStyle);
                row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(op.getContrAgent());
                row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(op.getStrparameter1());
                row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(op.getFloatparameter3() + " " + op.getStrparameter2());
                row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(op.getFloatparameter4() + " " + op.getStrparameter3());
                row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(op.getSum());
                row.getCell(6).setCellStyle(floNumStyle);
                row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue(op.getCurrency());
            }
        }
        for(int j = 0; j < 8; j++)
            detailsSheet.autoSizeColumn(j);
    }

    private static void fillProductsResult (Sheet resultSheet, Date date) {
        resultSheet.protectSheet("poiProtect");

        HashMap<String, Integer> totalSum = new HashMap<>();
        HashMap<String, Integer> totalOplacheno = new HashMap<>();

        //Float totalSum = 0.0f;
        //Float totalOplacheno = 0.0f;
        HashMap<Integer, Operations> result = new HashMap<>();
        for ( Operations op : GlobalPandaApp.site.getReportProductPurchase(date) ) {
            if ( result.containsKey(op.getCheckId()) ) {
                result.get(op.getCheckId()).addSum(op.getSum());
            } else {
                Operations res = new Operations();
                res.setDate( op.getDate() );
                res.setContrAgent( op.getContrAgent() );
                res.setCurrency( op.getCurrency() );
                res.setCheckId( op.getCheckId() );
                res.setFloatparameter1(0.0f);
                res.setSum( op.getSum() );
                result.put( op.getCheckId(), res);
            }
            if (!totalSum.containsKey(op.getCurrency()))
                totalSum.put(op.getCurrency(), 0);
            totalSum.put(op.getCurrency(), totalSum.get(op.getCurrency()) + op.getSum().intValue());
        }

        for ( Operations op : result.values() ) {
            ArrayList<Operations> borg = GlobalPandaApp.site.getDebtPurchase(op.getCheckId());
            float opl = 0.0f;
            if( borg != null && borg.size() > 0)
                for (Operations bor : borg)
                    opl += bor.getSum();
            op.setFloatparameter1( opl );
            if(!totalOplacheno.containsKey(op.getCurrency()))
                totalOplacheno.put(op.getCurrency(), 0);
            totalOplacheno.put(op.getCurrency(), totalOplacheno.get(op.getCurrency()) + (int) opl);
        }

        Row header = resultSheet.createRow(0);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("CHECK_ID");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Дата");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Контрагент");
        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Сума");
        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Оплачено");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Валюта");

        int i = 1;
        for (Iterator<Operations> iterator = result.values().iterator(); iterator.hasNext(); ) {
            Operations op = iterator.next();
            Row row = resultSheet.createRow( i++ );
            row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(op.getCheckId());
            row.getCell(0).setCellStyle(intNumStyle);
            row.createCell(1).setCellValue(op.getDate());
            row.getCell(1).setCellStyle(dateStyle);
            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(op.getContrAgent());
            row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(op.getSum());
            row.getCell(3).setCellStyle(floNumStyle);
            row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(op.getFloatparameter1());
            row.getCell(4).setCellStyle(floNumStyle);
            row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(op.getCurrency());
        }

        Row rowTotal = resultSheet.createRow(++i);
        rowTotal.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Сума");
        rowTotal.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Оплачено");
        rowTotal.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Борг");

        for (String s : totalSum.keySet()) {
            System.out.println(s);
            Row row = resultSheet.createRow(++i);
            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(s);
            row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(totalSum.get(s));
            row.getCell(3).setCellStyle(floNumStyle);
            row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(totalOplacheno.get(s));
            row.getCell(4).setCellStyle(floNumStyle);
            row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(totalSum.get(s) + totalOplacheno.get(s));
            row.getCell(5).setCellStyle(floNumStyle);
        }




        for(int j = 0; j < 6; j++)
            resultSheet.autoSizeColumn(j);
    }

    private static void fillDetails (Sheet detailsSheet) {
        detailsSheet.protectSheet("poiProtect");
        ArrayList<Operations> list = GlobalPandaApp.site.getReportProductPurchase();
        Collections.sort(list, (o1, o2) -> o1.getCheckId().compareTo(o2.getCheckId()));

        Row header = detailsSheet.createRow(0);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("CHECK_ID");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Дата");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Контрагент");
        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Продукт");
        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("К-ть 1");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("К-ть 2");
        header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Сума");
        header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Валюта");
        header.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Прийняв");
        header.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Коментар");



    }

    private static void fillGroupSheet (Sheet groupSheet) {
        Row header = groupSheet.createRow(2);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("GROUP_ID");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("GROUP_NAME");

        ArrayList<GROUPS_LIB> groupList = GlobalPandaApp.site.getGroups();
        int rowNum = 3;
        for ( GROUPS_LIB group : groupList ) {
            Row row = groupSheet.createRow(rowNum++);
            row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(group.getGroup_id());
            row.getCell(0).setCellStyle(intNumStyle);
            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(group.getName());
        }
    }

    public static void fillProdSheet (Sheet prodSheet, Collection<Integer> cl) {

        try {
            Row header = prodSheet.createRow(2);
            header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("PRODUCT_NAME");
            header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("PRODUCT_ID");
            header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("UNITS1");
            header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("UNITS2");
            header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("U2/U1");
            header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("EXPIRED_PERIOD");
            header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("CURRENCY");
            header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("LAST_PRICE_CUR");
            header.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("AVERAGE_PRICE_CUR");
            header.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("CUR_TO_UAH_TODAY");
            header.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("LAST_PRICE_UAH");
            header.createCell(11, Cell.CELL_TYPE_STRING).setCellValue("AVERAGE_PRICE_UAH");

            ArrayList<PRODUCTS> prodList = GlobalPandaApp.site.getProducts();
            int rowNum = 3;
            for (PRODUCTS prod : prodList) {
                if (cl != null && !cl.contains(prod.getProductId()))
                    continue;
                Row row = prodSheet.createRow(rowNum++);
                row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(prod.getProductName());
                row.createCell(1, Cell.CELL_TYPE_NUMERIC).setCellValue(prod.getProductId());
                row.getCell(1).setCellStyle(intNumStyle);
                row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(prod.getFirstUnits());
                row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(prod.getSecondUnits());
                row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(prod.getUnitsRelation());
                row.getCell(4).setCellStyle(intNumStyle);
                row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(prod.getExpiredPeriod() == null ? 0 : prod.getExpiredPeriod());
                row.getCell(5).setCellStyle(intNumStyle);
                row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue(prod.getCurrency());
                row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(prod.getLastPriceCur() == null ? 0 : prod.getLastPriceCur());
                row.getCell(7).setCellStyle(floNumStyle);
                row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(prod.getAvgPriceCur() == null ? 0 : prod.getAvgPriceCur());
                row.getCell(8).setCellStyle(floNumStyle);
                row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(prod.getCurToUah() == null ? 0 : prod.getCurToUah());
                row.getCell(9).setCellStyle(floNumStyle);
                row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(prod.getLastPriceUah() == null ? 0 : prod.getLastPriceUah());
                row.getCell(10).setCellStyle(floNumStyle);
                row.createCell(11, Cell.CELL_TYPE_NUMERIC).setCellValue(prod.getAvgPriceUah() == null ? 0 : prod.getAvgPriceUah());
                row.getCell(11).setCellStyle(floNumStyle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillIngredientSheet (Sheet ingSheet, Collection<Integer> cl) {
        Row header = ingSheet.createRow(2);
        header.createCell(0 ,Cell.CELL_TYPE_STRING).setCellValue("INGREDIENT_NAME");
        header.createCell(1 ,Cell.CELL_TYPE_STRING).setCellValue("INGREDIENT_ID");
        header.createCell(2 ,Cell.CELL_TYPE_STRING).setCellValue("UNITS");
        header.createCell(3 ,Cell.CELL_TYPE_STRING).setCellValue("EXPIRED PERIOD");
        header.createCell(4 ,Cell.CELL_TYPE_STRING).setCellValue("IMAGE URL");
        header.createCell(5 ,Cell.CELL_TYPE_STRING).setCellValue("Сихів");
        header.createCell(6 ,Cell.CELL_TYPE_STRING).setCellValue("Варшавська");
        header.createCell(7 ,Cell.CELL_TYPE_STRING).setCellValue("Інвентаризація");

        ArrayList<INGREDIENTS> ingList = GlobalPandaApp.site.getIngredients();
        int rowNum = 3;
        for(INGREDIENTS ing : ingList) {
            if (cl != null && !cl.contains(ing.getIngredientId()))
                continue;

            Row row = ingSheet.createRow(rowNum++);
            row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(ing.getIngredientName());
            row.createCell(1, Cell.CELL_TYPE_NUMERIC).setCellValue(ing.getIngredientId());
            row.getCell(1).setCellStyle(intNumStyle);
            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(ing.getUnits());
            row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(ing.getExpiredPeriod());
            row.getCell(3).setCellStyle(intNumStyle);
            row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(ing.getImageUrl());
            row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(ing.getSyhivImportance());
            row.getCell(5).setCellStyle(intNumStyle);
            row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(ing.getVarshavImportance());
            row.getCell(6).setCellStyle(intNumStyle);
            row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(ing.getOnInventary() ? 1 : 0);
            row.getCell(7).setCellStyle(intNumStyle);
        }
     }

    public static void fillProdIngSheet (Sheet prodIngSheet, Collection<Integer> cl) {
        Row header = prodIngSheet.createRow(1);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("PROD_ING_ID");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("PRODUCT_NAME");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("PRODUCT_ID");
        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("INGREDIENT_NAME");
        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("INGREDIENT_ID");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("UNIT2");
        header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("LAST_CHECK");
        header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("COEF");
        header.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("AVG_COEF");
        header.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("AVG_PRICE");
        header.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("AUTO|MANUAL");

        ArrayList<PRODUCTS_INGREDIENTS> prodIngList = GlobalPandaApp.site.getProdIng();
        int rowNum = 2;
        for(PRODUCTS_INGREDIENTS prIng : prodIngList) {

            if ( cl != null && ( !cl.contains(prIng.getProductId()) || !cl.contains(prIng.getIngredientId()) ) )
                continue;
            Row row = prodIngSheet.createRow(rowNum++);
            row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(prIng.getProdIngId());
            row.getCell(0).setCellStyle(intNumStyle);
            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(prIng.getProductName());
            row.createCell(2, Cell.CELL_TYPE_NUMERIC).setCellValue(prIng.getProductId());
            row.getCell(2).setCellStyle(intNumStyle);
            row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(prIng.getIngredientName());
            row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(prIng.getIngredientId());
            row.getCell(4).setCellStyle(intNumStyle);
            row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(prIng.getUnits());
            row.createCell(6).setCellValue(prIng.getLastCheck());
            row.getCell(6).setCellStyle(dateStyle);
            row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(prIng.getLastCoef());
            row.getCell(7).setCellStyle(floNumStyle);
            row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(prIng.getAvgCoef());
            row.getCell(8).setCellStyle(floNumStyle);
            row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(prIng.getAvgPrice());
            row.getCell(9).setCellStyle(floNumStyle);
            row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue(prIng.getAuto() ? "А" : "Man");
        }
    }

    public static void fillTehcardSheet (Sheet tehcardSheet, Collection<Integer> cl) {
        Row header = tehcardSheet.createRow(2);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("TEHCARD_ID");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("DISH_NAME");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("DISH_ID");
        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("INGREDIENT_NAME");
        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("INGREDIENT_ID");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("UNITS");
        header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("COUNT");
        header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("USUSHKA");
        header.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("FINAL_WEIGHT");

        ArrayList<TEHCARDS> tehcardList = GlobalPandaApp.site.getTehcards();
        int rowNum = 3;

        if (cl == null) {
            for (TEHCARDS tehcard : tehcardList) {
                Row row = tehcardSheet.createRow(rowNum++);
                row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(tehcard.getTehcard_id());
                row.getCell(0).setCellStyle(intNumStyle);
                row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(tehcard.getDishName());
                row.createCell(2, Cell.CELL_TYPE_NUMERIC).setCellValue(tehcard.getDishId());
                row.getCell(2).setCellStyle(intNumStyle);
                row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(tehcard.getIngredientName());
                row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(tehcard.getIngredientId());
                row.getCell(4).setCellStyle(intNumStyle);
                row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(tehcard.getUnits());
                row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(tehcard.getCount());
                row.getCell(6).setCellStyle(intNumStyle);
                row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(tehcard.getUsushka());
                row.getCell(7).setCellStyle(intNumStyle);
                row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(tehcard.getFinalWeight());
                row.getCell(8).setCellStyle(intNumStyle);
            }
        } else {

            try {
                CellStyle group = tehcardSheet.getWorkbook().createCellStyle();
                Font hFont = tehcardSheet.getWorkbook().createFont();
                hFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                hFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                hFont.setFontName("Calibri");
                group.setFont(hFont);

                for (TEHCARDS tehcard : tehcardList) {
                    if (cl.contains(tehcard.getIngredientId())) {
                        Integer id = tehcard.getIngredientId();
                        List<TEHCARDS> list = tehcardList.stream().filter(tehcards -> tehcards.getDishId().equals(tehcard.getDishId())).collect(Collectors.toList());
                        int begin = rowNum;
                        for (TEHCARDS th : list) {
                            if (th.getIngredientId().equals(id))
                                continue;
                            Row row = tehcardSheet.createRow(rowNum++);

                            row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getTehcard_id());
                            row.getCell(0).setCellStyle(intNumStyle);
                            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(th.getDishName());
                            row.createCell(2, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getDishId());
                            row.getCell(2).setCellStyle(intNumStyle);
                            row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(th.getIngredientName());
                            row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getIngredientId());
                            row.getCell(4).setCellStyle(intNumStyle);
                            row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(th.getUnits());
                            row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getCount());
                            row.getCell(6).setCellStyle(intNumStyle);
                            row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getUsushka());
                            row.getCell(7).setCellStyle(intNumStyle);
                            row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getFinalWeight());
                            row.getCell(8).setCellStyle(intNumStyle);
                        }
                        int end = rowNum - 1;

                        TEHCARDS th = list.stream().filter(tehcards -> tehcards.getIngredientId().equals(id)).findFirst().get();

                        Row row = tehcardSheet.createRow(rowNum++);

                        row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getTehcard_id());
                        row.getCell(0).setCellStyle(intNumStyle);
                        row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(th.getDishName());
                        row.createCell(2, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getDishId());
                        row.getCell(2).setCellStyle(intNumStyle);
                        row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(th.getIngredientName());
                        row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getIngredientId());
                        row.getCell(4).setCellStyle(intNumStyle);
                        row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(th.getUnits());
                        row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getCount());
                        row.getCell(6).setCellStyle(intNumStyle);
                        row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getUsushka());
                        row.getCell(7).setCellStyle(intNumStyle);
                        row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(th.getFinalWeight());
                        row.getCell(8).setCellStyle(intNumStyle);

                        for (int i = 0; i < 9; i++) {
                            XSSFCellStyle style = (XSSFCellStyle) tehcardSheet.getWorkbook().createCellStyle();
                            style.cloneStyleFrom(row.getCell(i).getCellStyle());
                            style.setFont(hFont);
                            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                            row.getCell(i).setCellStyle(style);
                        }

                        if (end - begin >= 0) {
                            try {
                                tehcardSheet.groupRow(begin, end);
                                tehcardSheet.setRowGroupCollapsed(begin, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    private static void fillMenuSheet (Sheet menuSheet) {
        Row header = menuSheet.createRow(2);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("DISH_ID");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("DISH_NAME");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("PRICE");
        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("CHARCODE");
        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("UNITS_TEHCARD");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("WEIGHT");
        header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("UNITS");
        header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("GINGER");
        header.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("COLDHOTADDS");
        header.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("UNAGI");
        header.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("PIZZASUSHIDRINKS");
        header.createCell(11, Cell.CELL_TYPE_STRING).setCellValue("DIRECTION_NAME");
        header.createCell(12, Cell.CELL_TYPE_STRING).setCellValue("IMAGE_URL");
        header.createCell(13, Cell.CELL_TYPE_STRING).setCellValue("GROUP_ID");
        header.createCell(14, Cell.CELL_TYPE_STRING).setCellValue("GROUP_NAME");

        ArrayList<Menu> menuList = GlobalPandaApp.site.getMenu();
        int rowNum = 3;
        for (Menu menu : menuList) {
            Row row = menuSheet.createRow(rowNum++);
            row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(menu.getCode());
            row.getCell(0).setCellStyle(intNumStyle);
            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(menu.getDish());
            row.createCell(2, Cell.CELL_TYPE_NUMERIC).setCellValue(menu.getPrice());
            row.getCell(2).setCellStyle(intNumStyle);
            row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(menu.getSCode());
            row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(menu.getUnitsTehcard());
            row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(menu.getWeight());
            row.getCell(5).setCellStyle(intNumStyle);
            row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue(menu.getUnits());
            row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(menu.getGinger());
            row.getCell(7).setCellStyle(intNumStyle);
            row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue(menu.getColdHotAdds());
            row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(menu.getUnagi());
            row.getCell(9).setCellStyle(intNumStyle);
            row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue(menu.getPizzaSushiDrinks());
            row.createCell(11, Cell.CELL_TYPE_STRING).setCellValue(menu.getDirection());
            row.createCell(12, Cell.CELL_TYPE_STRING).setCellValue(menu.getImageUrl());
            row.createCell(13, Cell.CELL_TYPE_NUMERIC).setCellValue(menu.getGroup_id());
            row.getCell(13).setCellStyle(intNumStyle);
            row.createCell(14, Cell.CELL_TYPE_STRING).setCellValue(menu.getGroupName());
        }
    }
}

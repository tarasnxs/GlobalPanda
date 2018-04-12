package ua.com.pandasushi.controller.kitchen.inventory;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import ua.com.pandasushi.controller.services.ExcelService;
import ua.com.pandasushi.database.common.*;
import ua.com.pandasushi.database.common.menu.INGREDIENTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS_INGREDIENTS;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Тарас on 23.03.2017.
 */
public class InventoryReportTotal extends Task<Workbook> {

    private static CellStyle prevStyle;
    private static CellStyle dateStyle;
    private static CellStyle headerStyle;
    private static CellStyle timeStyle;
    private static CellStyle fullDateStyle;
    private static CellStyle intNumStyle;
    private static CellStyle floNumStyle;
    private static CellStyle statsStyle;
    private static CellStyle percentStyle;
    private static CellStyle fillGroupStyle;

    public Workbook book;

    private ArrayList<HashMap<Integer, ArrayList<Inventory>>> current;
    private ArrayList<HashMap<Integer, ArrayList<Inventory>>> previous;
    private ArrayList<INGREDIENTS> ingredientsList;

    private Collection<Integer> ids;

    SimpleDoubleProperty progressBar;

    InventoryReportTotal() {
        progressBar = new SimpleDoubleProperty(0.0);
    }

    @Override
    protected Workbook call() throws Exception {
        try {
            return makeExcel();
        } catch (HibernateException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getStackTrace());
            e.printStackTrace();
        }
        return new XSSFWorkbook();
    }


    public void setInv(ArrayList<Inventory> invList) {


        ids = new HashSet<>();
        current = new ArrayList<>();
        previous = new ArrayList<>();
        ingredientsList = GlobalPandaApp.site.getIngredients();
        for (int i = 0; i < invList.size(); i++) {
            current.add(new HashMap<>());
            previous.add(new HashMap<>());
            for (Inventory cur : GlobalPandaApp.site.getInventoryList(invList.get(i).getCheckId())) {
                if (!current.get(i).containsKey(cur.getBasicIng()))
                    current.get(i).put(cur.getBasicIng(), new ArrayList<>());
                current.get(i).get(cur.getBasicIng()).add(cur);
                if (!previous.get(i).containsKey(cur.getBasicIng()))
                    previous.get(i).put(cur.getBasicIng(), GlobalPandaApp.site.getLastInventory(cur.getBasicIng(), cur.getCheckId(), cur.getKitchen()));
            }
        }

        if (current.size() > 21)
            book = new SXSSFWorkbook();
        else
            book = new XSSFWorkbook();

    }

    public Workbook makeExcel() {
        progressBar.set(0.10);
        headerStyle = book.createCellStyle();
        dateStyle = book.createCellStyle();
        timeStyle = book.createCellStyle();
        fullDateStyle = book.createCellStyle();
        intNumStyle = book.createCellStyle();
        floNumStyle = book.createCellStyle();
        prevStyle = book.createCellStyle();
        statsStyle = book.createCellStyle();
        percentStyle = book.createCellStyle();
        fillGroupStyle = book.createCellStyle();


        DataFormat df = book.createDataFormat();
        Font hFont = book.createFont();
        hFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        hFont.setFontName("Calibri");
        Font prevFont = book.createFont();
        prevFont.setItalic(true);

        prevStyle.setFont(prevFont);
        prevStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFont(hFont);
        fullDateStyle.setDataFormat(df.getFormat("dd.MM.yy HH:mm"));
        timeStyle.setDataFormat(df.getFormat("HH:mm"));
        dateStyle.setDataFormat(df.getFormat("dd.MM.yy"));
        intNumStyle.setDataFormat(df.getFormat("#,##0_);(#,##0);\"-\""));
        statsStyle.setDataFormat(df.getFormat("#,##0_);(#,##0);\"-\""));
        statsStyle.setFont(hFont);
        statsStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        statsStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        statsStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        floNumStyle.setDataFormat(df.getFormat("#,##0.00_);(#,##0.00);\"-\""));
        percentStyle.setDataFormat(df.getFormat("#,##0.0\"%\"_);(#,##0.0\"%\");\"-\""));
        fillGroupStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        fillGroupStyle.setFillPattern(CellStyle.BIG_SPOTS);


        progressBar.set(0.0);
        System.out.println("fillTotalSheet");
        for (int i = 0; i < current.size(); i++) {
            fillTotalSheet(current.get(i), previous.get(i));
            progressBar.set((1.0 / (double) current.size()) * ((double) i + 1.0));
        }

        progressBar.set(1.0);
        return book;
    }

    private void fillTotalSheet(HashMap<Integer, ArrayList<Inventory>> inventory, HashMap<Integer, ArrayList<Inventory>> previous) {

        Inventory iTemp = inventory.values().iterator().next().get(0);

        String kitchName = "";
        switch (iTemp.getKitchen()) {
            case 0:
                kitchName = "Сихів";
                break;

            case 1:
                kitchName = "Варшавська";
                break;

            case 5:
                kitchName = "Садова";
                break;

            default:
                break;
        }

        String name = new SimpleDateFormat("dd.MM").format(iTemp.getTimeStart()) + kitchName;

        try {
            Sheet totalSheet = book.createSheet(name);


            Row preHeader = totalSheet.createRow(0);
            preHeader.setRowStyle(headerStyle);

            preHeader.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Попередня інвентаризація");
            preHeader.getCell(3).setCellStyle(headerStyle);

            preHeader.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Прихід");
            preHeader.getCell(7).setCellStyle(headerStyle);

            preHeader.createCell(11, Cell.CELL_TYPE_STRING).setCellValue("Переміщення/Списання");
            preHeader.getCell(11).setCellStyle(headerStyle);

            preHeader.createCell(15, Cell.CELL_TYPE_STRING).setCellValue("Розробка");
            preHeader.getCell(15).setCellStyle(headerStyle);

            preHeader.createCell(19, Cell.CELL_TYPE_STRING).setCellValue("Розхід");
            preHeader.getCell(19).setCellStyle(headerStyle);

            preHeader.createCell(22, Cell.CELL_TYPE_STRING).setCellValue("Факт");
            preHeader.getCell(22).setCellStyle(headerStyle);

            preHeader.createCell(26, Cell.CELL_TYPE_STRING).setCellValue("Різниця");
            preHeader.getCell(26).setCellStyle(headerStyle);

            Row header = totalSheet.createRow(1);
            header.setRowStyle(headerStyle);

            //Попередня інвентаризація
            header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Дата");
            header.getCell(0).setCellStyle(headerStyle);

            //header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Період");
            //header.getCell(1).setCellStyle(headerStyle);

            header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Відповідальний");
            header.getCell(1).setCellStyle(headerStyle);

            header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Продукт");
            header.getCell(2).setCellStyle(headerStyle);

            header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("U1");
            header.getCell(3).setCellStyle(headerStyle);

            header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("U2");
            header.getCell(4).setCellStyle(headerStyle);

            header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Коеф.");
            header.getCell(5).setCellStyle(headerStyle);

            header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Нетто");
            header.getCell(6).setCellStyle(headerStyle);

            //Прихід
            header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("U1");
            header.getCell(7).setCellStyle(headerStyle);

            header.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("U2");
            header.getCell(8).setCellStyle(headerStyle);

            header.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Коеф.");
            header.getCell(9).setCellStyle(headerStyle);

            header.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Нетто");
            header.getCell(10).setCellStyle(headerStyle);

            //Переміщення/Списання
            header.createCell(11, Cell.CELL_TYPE_STRING).setCellValue("U1");
            header.getCell(11).setCellStyle(headerStyle);

            header.createCell(12, Cell.CELL_TYPE_STRING).setCellValue("U2");
            header.getCell(12).setCellStyle(headerStyle);

            header.createCell(13, Cell.CELL_TYPE_STRING).setCellValue("Коеф");
            header.getCell(13).setCellStyle(headerStyle);

            header.createCell(14, Cell.CELL_TYPE_STRING).setCellValue("Нетто");
            header.getCell(14).setCellStyle(headerStyle);

            //Розробка
            header.createCell(15, Cell.CELL_TYPE_STRING).setCellValue("U1");
            header.getCell(15).setCellStyle(headerStyle);

            header.createCell(16, Cell.CELL_TYPE_STRING).setCellValue("Розроблено");
            header.getCell(16).setCellStyle(headerStyle);

            header.createCell(17, Cell.CELL_TYPE_STRING).setCellValue("Отримано");
            header.getCell(17).setCellStyle(headerStyle);

            header.createCell(18, Cell.CELL_TYPE_STRING).setCellValue("Коеф");
            header.getCell(18).setCellStyle(headerStyle);

            //Розхід
            header.createCell(19, Cell.CELL_TYPE_STRING).setCellValue("U2");
            header.getCell(19).setCellStyle(headerStyle);

            header.createCell(20, Cell.CELL_TYPE_STRING).setCellValue("Коеф");
            header.getCell(20).setCellStyle(headerStyle);

            header.createCell(21, Cell.CELL_TYPE_STRING).setCellValue("Нетто");
            header.getCell(21).setCellStyle(headerStyle);

            //Факт
            header.createCell(22, Cell.CELL_TYPE_STRING).setCellValue("U1");
            header.getCell(22).setCellStyle(headerStyle);

            header.createCell(23, Cell.CELL_TYPE_STRING).setCellValue("U2");
            header.getCell(23).setCellStyle(headerStyle);

            header.createCell(24, Cell.CELL_TYPE_STRING).setCellValue("Коеф");
            header.getCell(24).setCellStyle(headerStyle);

            header.createCell(25, Cell.CELL_TYPE_STRING).setCellValue("Нетто");
            header.getCell(25).setCellStyle(headerStyle);

            //Різниця
            header.createCell(26, Cell.CELL_TYPE_STRING).setCellValue("U1");
            header.getCell(26).setCellStyle(headerStyle);

            header.createCell(27, Cell.CELL_TYPE_STRING).setCellValue("Нетто");
            header.getCell(27).setCellStyle(headerStyle);

            header.createCell(28, Cell.CELL_TYPE_STRING).setCellValue("%");
            header.getCell(28).setCellStyle(headerStyle);

            header.createCell(29, Cell.CELL_TYPE_STRING).setCellValue("Сума >5%");
            header.getCell(29).setCellStyle(headerStyle);

            header.createCell(30, Cell.CELL_TYPE_STRING).setCellValue("Сума загальна");
            header.getCell(30).setCellStyle(headerStyle);

            header.createCell(31, Cell.CELL_TYPE_STRING).setCellValue("З першої спроби");
            header.getCell(31).setCellStyle(headerStyle);


            int index = 2;
            for (Integer i : inventory.keySet()) {
                ArrayList<Inventory> cur = inventory.get(i);
                ArrayList<Inventory> prev = previous.get(i);

                String ingName = "";
                float totalPrevious = 0;
                float totalPurchase = 0;
                float totalShift = 0;
                float totalDiffRozrobka = 0;
                float totalConsumption = 0;
                float totalFact = 0;
                float totalDiffs = 0;

                for (Inventory c : cur) {
                    int totalTushka = 0;

                    ids.add(c.getProdIngId());

                    Optional<Inventory> p = prev.stream().filter(ing -> ing.getProdIngId().equals(c.getProdIngId())).findFirst();
                    Row row = totalSheet.createRow(index++);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

                    float diff = c.getBegin().getTime() - c.getPrevious().getTime();

                    diff /= 1000 * 60 * 60 * 24;

                    int days = Math.round(diff);


                    row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(sdf.format(c.getPrevious()) + " - ( " + days + " днів )");
                    row.getCell(0).setCellStyle(dateStyle);

                    row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(p.isPresent() ? p.get().getCook() : "");

                    row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(c.getProdIngName());

                    row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(p.isPresent() ? p.get().getFactU1() : 0);
                    row.getCell(3).setCellStyle(intNumStyle);
                    totalTushka += p.isPresent() ? p.get().getFactU1() : 0;

                    row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(p.isPresent() ? p.get().getFactU2() : 0);
                    row.getCell(4).setCellStyle(intNumStyle);

                    int sumU1purchase = GlobalPandaApp.site.getSumU1ProdPurchase(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());
                    row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(sumU1purchase);
                    totalTushka += sumU1purchase;
                    row.getCell(7).setCellStyle(intNumStyle);

                    row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(GlobalPandaApp.site.getSumProdPurchase(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen()));
                    row.getCell(8).setCellStyle(intNumStyle);

                    int sumU1shift = GlobalPandaApp.site.getSumU1ProdShift(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());
                    row.createCell(11, Cell.CELL_TYPE_NUMERIC).setCellValue(sumU1shift);
                    totalTushka += sumU1shift;
                    row.getCell(11).setCellStyle(intNumStyle);

                    row.createCell(12, Cell.CELL_TYPE_NUMERIC).setCellValue(GlobalPandaApp.site.getSumProdShift(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen()));
                    row.getCell(12).setCellStyle(intNumStyle);

                    ArrayList<Integer> rozrobka = GlobalPandaApp.site.getRozrobka(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());

                    row.createCell(15, Cell.CELL_TYPE_NUMERIC).setCellValue(rozrobka.get(2));
                    totalTushka -= rozrobka.get(2);
                    row.getCell(15).setCellStyle(intNumStyle);

                    row.createCell(16, Cell.CELL_TYPE_NUMERIC).setCellValue(rozrobka.get(0));
                    row.getCell(16).setCellStyle(intNumStyle);

                    row.createCell(17, Cell.CELL_TYPE_NUMERIC).setCellValue(rozrobka.get(1));
                    row.getCell(17).setCellStyle(intNumStyle);


                    if (rozrobka.get(1) > 0) {
                        float coef = (float) rozrobka.get(0) / (float) rozrobka.get(1);
                        row.createCell(18, Cell.CELL_TYPE_NUMERIC).setCellValue(coef);
                        row.getCell(18).setCellStyle(floNumStyle);
                        PRODUCTS_INGREDIENTS prodIng = GlobalPandaApp.site.getProdIngForProd(c.getProdIngName());
                        if (prodIng != null) {
                            float diffRozrobka = (float) rozrobka.get(1) - ((float) rozrobka.get(0) / prodIng.getAvgCoef());
                            totalDiffRozrobka += diffRozrobka;
                        }
                    }

                    row.createCell(19, Cell.CELL_TYPE_NUMERIC).setCellValue(GlobalPandaApp.site.getIngredientConsumption(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen()));
                    row.getCell(19).setCellStyle(intNumStyle);

                    row.createCell(22, Cell.CELL_TYPE_NUMERIC).setCellValue(c.getFactU1());
                    row.getCell(22).setCellStyle(intNumStyle);

                    row.createCell(23, Cell.CELL_TYPE_NUMERIC).setCellValue(c.getFactU2());
                    row.getCell(23).setCellStyle(intNumStyle);


                    if (c.getProdIngId() < 1700) {
                        ingName = c.getProdIngName();
                        row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(p.isPresent() ? 1 : 0);
                        row.getCell(5).setCellStyle(intNumStyle);

                        row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(p.isPresent() ? p.get().getFactU2() : 0);
                        totalPrevious += p.isPresent() ? p.get().getFactU2() : 0;
                        row.getCell(6).setCellStyle(intNumStyle);

                        row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
                        row.getCell(9).setCellStyle(intNumStyle);

                        row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
                        row.getCell(10).setCellStyle(intNumStyle);

                        int sumProdShift = GlobalPandaApp.site.getSumProdShift(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());
                        row.createCell(13, Cell.CELL_TYPE_NUMERIC).setCellValue(sumProdShift != 0 ? 1 : 0);
                        row.getCell(13).setCellStyle(intNumStyle);

                        row.createCell(14, Cell.CELL_TYPE_NUMERIC).setCellValue(sumProdShift);
                        totalShift += sumProdShift;
                        row.getCell(14).setCellStyle(intNumStyle);

                        int sumCons = GlobalPandaApp.site.getIngredientConsumption(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());

                        row.createCell(20, Cell.CELL_TYPE_NUMERIC).setCellValue(sumCons > 0 ? 1 : 0);
                        row.getCell(20).setCellStyle(intNumStyle);

                        row.createCell(21, Cell.CELL_TYPE_NUMERIC).setCellValue(sumCons);
                        totalConsumption += sumCons;
                        row.getCell(21).setCellStyle(intNumStyle);

                        row.createCell(24, Cell.CELL_TYPE_NUMERIC).setCellValue(c.getFactU2() > 0 ? 1 : 0);
                        row.getCell(24).setCellStyle(intNumStyle);

                        row.createCell(25, Cell.CELL_TYPE_NUMERIC).setCellValue(c.getFactU2());
                        totalFact += c.getFactU2();
                        row.getCell(25).setCellStyle(intNumStyle);

                    } else if (c.getProdIngId() < 1900) {
                        float nfCoef = GlobalPandaApp.site.getCoefNF(c.getBasicIng(), c.getProdIngId());

                        row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(p.isPresent() ? nfCoef : 0.0f);
                        row.getCell(5).setCellStyle(floNumStyle);

                        row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(p.isPresent() ? p.get().getFactU2().floatValue() / nfCoef : 0.0f); // Coef of NF in range between 0 and 1
                        totalPrevious += p.isPresent() ? p.get().getFactU2().floatValue() / nfCoef : 0.0f;
                        row.getCell(6).setCellStyle(intNumStyle);

                        row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
                        row.getCell(9).setCellStyle(floNumStyle);

                        row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
                        row.getCell(10).setCellStyle(intNumStyle);

                        int sumProdShift = GlobalPandaApp.site.getSumProdShift(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());

                        row.createCell(13, Cell.CELL_TYPE_NUMERIC).setCellValue(sumProdShift > 0 ? nfCoef : 0);
                        row.getCell(13).setCellStyle(floNumStyle);

                        row.createCell(14, Cell.CELL_TYPE_NUMERIC).setCellValue((float) sumProdShift / nfCoef);
                        totalShift += (float) sumProdShift / nfCoef;
                        row.getCell(14).setCellStyle(intNumStyle);


                        int sumCons = GlobalPandaApp.site.getIngredientConsumption(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());

                        row.createCell(20, Cell.CELL_TYPE_NUMERIC).setCellValue(sumCons > 0 ? nfCoef : 0);
                        row.getCell(20).setCellStyle(floNumStyle);

                        row.createCell(21, Cell.CELL_TYPE_NUMERIC).setCellValue((float) sumCons / nfCoef);
                        totalConsumption += (float) sumCons / nfCoef;
                        row.getCell(21).setCellStyle(intNumStyle);

                        row.createCell(24, Cell.CELL_TYPE_NUMERIC).setCellValue(c.getFactU2() > 0 ? nfCoef : 0);
                        row.getCell(24).setCellStyle(floNumStyle);

                        row.createCell(25, Cell.CELL_TYPE_NUMERIC).setCellValue((float) c.getFactU2() / nfCoef);
                        totalFact += (float) c.getFactU2() / nfCoef;
                        row.getCell(25).setCellStyle(intNumStyle);

                    } else if (c.getProdIngId() > 3000) {
                        float avgCoef = GlobalPandaApp.site.getAvgCoefProduct(c.getProdIngId());

                        row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(p.isPresent() ? avgCoef : 0.0f);
                        row.getCell(5).setCellStyle(floNumStyle);

                        row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(p.isPresent() ? p.get().getFactU2().floatValue() / avgCoef : 0.0f); // Coef of product is greater or equals 1
                        totalPrevious += p.isPresent() ? p.get().getFactU2().floatValue() / avgCoef : 0.0f;
                        row.getCell(6).setCellStyle(intNumStyle);

                        int sumProdPurch = GlobalPandaApp.site.getSumProdPurchase(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());

                        row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(sumProdPurch > 0 ? avgCoef : 0.0f);
                        row.getCell(9).setCellStyle(floNumStyle);

                        row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue((float) sumProdPurch / avgCoef);
                        totalPurchase += (float) sumProdPurch / avgCoef;
                        row.getCell(10).setCellStyle(intNumStyle);

                        int sumProdShift = GlobalPandaApp.site.getSumProdShift(c.getPrevious(), c.getBegin(), c.getProdIngId(), c.getKitchen());

                        row.createCell(13, Cell.CELL_TYPE_NUMERIC).setCellValue(sumProdShift > 0 ? avgCoef : 0);
                        row.getCell(13).setCellStyle(floNumStyle);

                        row.createCell(14, Cell.CELL_TYPE_NUMERIC).setCellValue((float) sumProdShift / avgCoef);
                        totalShift += (float) sumProdShift / avgCoef;
                        row.getCell(14).setCellStyle(intNumStyle);

                        row.createCell(20, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
                        row.getCell(20).setCellStyle(intNumStyle);

                        row.createCell(21, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
                        row.getCell(21).setCellStyle(intNumStyle);

                        row.createCell(24, Cell.CELL_TYPE_NUMERIC).setCellValue(c.getFactU2() > 0 ? avgCoef : 0);
                        row.getCell(24).setCellStyle(floNumStyle);

                        row.createCell(25, Cell.CELL_TYPE_NUMERIC).setCellValue((float) c.getFactU2() / avgCoef);
                        totalFact += (float) c.getFactU2() / avgCoef;
                        row.getCell(25).setCellStyle(intNumStyle);

                        PRODUCTS_INGREDIENTS prIng = GlobalPandaApp.site.getProdIngForProd(c.getProdIngName());
                        if (prIng != null && !prIng.getAuto()) {
                            row.createCell(26, Cell.CELL_TYPE_NUMERIC).setCellValue(c.getFactU1() - totalTushka);
                            row.getCell(26).setCellStyle(intNumStyle);
                        }
                    }

                }

                Row totals = totalSheet.createRow(index++);

                int importance = 0;

                String finalIngName = ingName;
                INGREDIENTS ing = ingredientsList.stream().filter(ingredients -> ingredients.getIngredientName().equals(finalIngName)).findFirst().get();

                switch (cur.get(0).getKitchen().intValue()) {
                    case 0:
                        importance = ing.getSyhivImportance();
                        break;

                    case 1:
                        importance = ing.getVarshavImportance();
                        break;

                    default:

                        break;
                }


                totals.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Нетто : " + ingName + " (" + importance + ")");

                totals.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.round(totalPrevious));
                totals.getCell(6).setCellStyle(intNumStyle);

                totals.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.round(totalPurchase));
                totals.getCell(10).setCellStyle(intNumStyle);

                totals.createCell(14, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.round(totalShift));
                totals.getCell(14).setCellStyle(intNumStyle);

                totals.createCell(17, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.round(totalDiffRozrobka));
                totals.getCell(17).setCellStyle(intNumStyle);

                totals.createCell(21, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.round(totalConsumption));
                totals.getCell(21).setCellStyle(intNumStyle);

                totals.createCell(25, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.round(totalFact));
                totals.getCell(25).setCellStyle(intNumStyle);

                float difference = totalPrevious + totalPurchase + totalShift + totalDiffRozrobka - totalConsumption - totalFact;

                totals.createCell(27, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.round(difference) * -1);
                totals.getCell(27).setCellStyle(intNumStyle);

                if (totalConsumption > 0) {
                    totals.createCell(28, Cell.CELL_TYPE_NUMERIC).setCellValue(-100 * difference / totalConsumption);
                    totals.getCell(28).setCellStyle(percentStyle);
                }


                totals.createCell(29, Cell.CELL_TYPE_NUMERIC).setCellValue(cur.get(0).getDiffCompensation());
                totals.getCell(29).setCellStyle(intNumStyle);


                totals.createCell(30, Cell.CELL_TYPE_NUMERIC).setCellValue(cur.get(0).getDiffUah());
                totals.getCell(30).setCellStyle(intNumStyle);


                totals.createCell(31, Cell.CELL_TYPE_STRING).setCellValue(cur.get(0).getFirstAttempt() ? "Так" : "Ні");

                for (int j = 0; j < 32; j++) {
                    Cell cell = totals.getCell(j);
                    if (cell == null) {
                        totals.createCell(j);
                        cell = totals.getCell(j);
                    }
                    CellStyle totalsCell = book.createCellStyle();
                    totalsCell.cloneStyleFrom(cell.getCellStyle());
                    totalsCell.setBorderTop(CellStyle.BORDER_THIN);
                    Font tFont = book.createFont();
                    tFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    totalsCell.setFont(tFont);
                    cell.setCellStyle(totalsCell);
                }

                for (Inventory toUpdate : cur) {
                    int totalCalc = Math.round(totalPrevious + totalPurchase + totalShift + totalDiffRozrobka - totalConsumption);
                    toUpdate.setCalculatedNetto(totalCalc);
                    toUpdate.setDiffNetto(Math.round(difference) * -1);
                    toUpdate.setDiffPercent(-100 * difference / totalConsumption);
                    try {
                        float cost = GlobalPandaApp.site.getIngCost(cur).floatValue();
                        toUpdate.setDiffUah(Math.round(cost * toUpdate.getDiffNetto()));
                        if (toUpdate.getDiffPercent() < -5.0f) {
                            float diffComp = (toUpdate.getDiffUah().floatValue() / toUpdate.getDiffPercent().floatValue()) * (toUpdate.getDiffPercent().floatValue() + 5.0f);
                            toUpdate.setDiffCompensation(Math.round(diffComp * cost));
                        } else
                            toUpdate.setDiffCompensation(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //GlobalPandaApp.site.update(toUpdate);
                    //System.out.println("Updated : " + toUpdate.getKitchen() + " - " + toUpdate.getTimeStart().toString() + " - " + toUpdate.getProdIngName());
                }

                totalSheet.createRow(index++);
            }

            int i = 0;
            while (i < 32) {
                totalSheet.autoSizeColumn(i++);
                if (i < 3) {
                    for (int j = 0; j < totalSheet.getLastRowNum(); j++) {
                        Cell cell = totalSheet.getRow(j).getCell(i);
                        if (cell != null) {
                            cell.setCellStyle(prevStyle);
                        }
                    }
                }
                if (i == 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 22 || i == 26) {
                    for (int j = 0; j < totalSheet.getLastRowNum(); j++) {
                        try {
                            Cell cell = totalSheet.getRow(j).getCell(i);
                            if (cell != null) {
                                CellStyle lBord = book.createCellStyle();
                                lBord.cloneStyleFrom(cell.getCellStyle());
                                lBord.setBorderLeft(CellStyle.BORDER_MEDIUM);
                                cell.setCellStyle(lBord);
                            } else {
                                totalSheet.getRow(j).createCell(i);
                                Cell cellN = totalSheet.getRow(j).getCell(i);
                                CellStyle lBord = book.createCellStyle();
                                lBord.cloneStyleFrom(cellN.getCellStyle());
                                lBord.setBorderLeft(CellStyle.BORDER_MEDIUM);
                                cellN.setCellStyle(lBord);
                            }
                        } catch (HibernateException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }
            }

            for (int j = 2; j < totalSheet.getLastRowNum(); j++) {
                Cell cell = totalSheet.getRow(j).getCell(0);
                Cell cellPrev = totalSheet.getRow(j - 1).getCell(0);
                if (cell != null && (cell.getCellStyle().equals(dateStyle) || (cellPrev != null && cellPrev.getCellStyle().equals(dateStyle)))) {
                    int k = 0;
                    while (k < 32) {
                        Cell cll = totalSheet.getRow(j).getCell(k++);
                        CellStyle cllStyle = book.createCellStyle();

                        if (cll == null) {
                            totalSheet.getRow(j).createCell(k - 1);
                            cll = totalSheet.getRow(j).getCell(k - 1);
                        }

                        cllStyle.cloneStyleFrom(cll.getCellStyle());
                        cllStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                        cllStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                        cll.setCellStyle(cllStyle);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
    }


}

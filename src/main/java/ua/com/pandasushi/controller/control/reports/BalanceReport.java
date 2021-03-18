package ua.com.pandasushi.controller.control.reports;

import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ua.com.pandasushi.database.common.InventoryBalance;
import ua.com.pandasushi.database.common.Operations;
import ua.com.pandasushi.database.common.menu.INGREDIENTS;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Taras on 07.06.2017.
 */
public class BalanceReport {
    private static XSSFCellStyle dateStyle;
    private static XSSFCellStyle intNumStyle;
    private static XSSFCellStyle floNumStyle;
    private static HashMap<Calendar, HashMap<Integer, HashMap<Integer, InventoryBalance>>> workList;
    private static ArrayList<INGREDIENTS> ingList;
    private static ArrayList<ForReport> weightList;
    private static ArrayList<ForReport> moneyList;
    private static int[] kitchs = new int[]{0,1,5,-1};
    private static HashMap<Integer, String> kitchNames;


    public static synchronized void createReport(Calendar from, Calendar to, boolean full) {
        if (full)
            kitchs = new int[]{0,1,5,-1};
        else
            kitchs = new int[]{-1};
        kitchNames = new HashMap<>();
        kitchNames.put(0, "Сихів");
        kitchNames.put(1, "Варшавська");
        kitchNames.put(5, "Садова");
        kitchNames.put(-1, "Тотал");

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
        String fileName = "balance_" + sdf.format(from.getTime()) + "-" + sdf.format(to.getTime()) + ".xlsx";
        from.set(Calendar.HOUR_OF_DAY, 10);
        to.set(Calendar.HOUR_OF_DAY, 11);

        ArrayList<Calendar> keysList = new ArrayList<>();
        ingList = GlobalPandaApp.site.getIngsForReport();
        workList = new HashMap<>();
        weightList = new ArrayList<>();
        moneyList = new ArrayList<>();
        do {
            Calendar key = (Calendar) from.clone();
            keysList.add(key);
            workList.put(key, GlobalPandaApp.site.getInventoryBalanceOnDate(key));
            from.add(Calendar.DATE, 1);
        } while (from.before(to));

        for (INGREDIENTS ing : ingList) {
            for (int kitch : kitchs) {
                ForReport weight = new ForReport();
                weight.setIngredient(ing.getIngredientId());
                weight.setIngredientName(ing.getIngredientName());
                weight.setKitchen(kitch);
                ForReport money = new ForReport();
                money.setIngredient(ing.getIngredientId());
                money.setIngredientName(ing.getIngredientName());
                money.setKitchen(kitch);
                InventoryBalance start = null;
                InventoryBalance end = null;
                for (Calendar cal : keysList) {
                    InventoryBalance ib = workList.get(cal).get(ing.getIngredientId()).get(kitch);
                    if (ib == null) continue;
                    if (start == null)
                        start = ib;
                    end = ib;

                    weight.setPurchase(weight.getPurchase() + ib.getPurchase());
                    weight.setShift(weight.getShift() + ib.getShift());
                    weight.setWriteOff(weight.getWriteOff() + ib.getWriteOff());
                    weight.setConsumption(weight.getConsumption() + ib.getConsumption());
                    weight.setInvDiff(weight.getInvDiff() + ib.getInvDiff());

                    money.setPurchase(money.getPurchase() + ib.getPurchaseUah());
                    money.setShift(money.getShift() + ib.getShiftUah());
                    money.setWriteOff(money.getWriteOff() + ib.getWriteOffUah());
                    money.setConsumption(money.getConsumption() + ib.getConsumptionUah());
                    money.setInvDiff(money.getInvDiff() + ib.getInvDiffUah());
                }
                if (start == null)
                    continue;
                weight.setStart(start.getDate());
                money.setStart(start.getDate());
                weight.setEnd(end.getDate());
                money.setEnd(end.getDate());
                weight.setNettoOnStart(start.getNetto());
                money.setNettoOnStart(Math.round(start.getNetto().floatValue() * start.getIngCost()));
                weight.setNettoOnEnd(end.getNetto());
                money.setNettoOnEnd(Math.round(end.getNetto().floatValue() * end.getIngCost()));
                weight.setIngCost(end.getIngCost());
                money.setIngCost(end.getIngCost());

                weight.setPurchase(weight.getPurchase() - end.getPurchase());
                weight.setShift(weight.getShift() - end.getShift());
                weight.setWriteOff(weight.getWriteOff() - end.getWriteOff());
                weight.setConsumption(weight.getConsumption() - end.getConsumption());
                weight.setInvDiff(weight.getInvDiff() - end.getInvDiff());

                money.setPurchase(money.getPurchase() - end.getPurchaseUah());
                money.setShift(money.getShift() - end.getShiftUah());
                money.setWriteOff(money.getWriteOff() - end.getWriteOffUah());
                money.setConsumption(money.getConsumption() - end.getConsumptionUah());
                money.setInvDiff(money.getInvDiff() - end.getInvDiffUah());

                weightList.add(weight);
                moneyList.add(money);
            }
        }


        createReportProductPurchase(fileName);
    }


    public static void createReportProductPurchase (String fileName) {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFDataFormat df = book.createDataFormat();

        dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(df.getFormat("dd.mm.yyyy"));
        intNumStyle = book.createCellStyle();
        intNumStyle.setDataFormat(df.getFormat("0"));
        floNumStyle = book.createCellStyle();
        floNumStyle.setDataFormat(df.getFormat("0.00"));

        fillWeightSheet( book.createSheet("Вага"));
        fillMoneySheet( book.createSheet("Гроші"));

        File file = saveFile(fileName);

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

    private static File saveFile(String initFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(initFileName);
        String userDir = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(userDir));
        fileChooser.setTitle("Зберегти звіт...");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(GlobalPandaApp.mainStage);
        if(file != null) {
            return file;
        } else {
            return new File("temp.xlsx");
        }
    }

    private static void fillWeightSheet (Sheet weightSheet) {
        XSSFCellStyle headerStyle = (XSSFCellStyle) weightSheet.getWorkbook().createCellStyle();
        XSSFCellStyle totalStyle = (XSSFCellStyle) weightSheet.getWorkbook().createCellStyle();

        XSSFFont defaultFont = (XSSFFont) weightSheet.getWorkbook().createFont();
        defaultFont.setFontHeightInPoints((short)10);
        defaultFont.setFontName("Arial");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        defaultFont.setBold(false);
        defaultFont.setItalic(false);

        XSSFFont font = (XSSFFont) weightSheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short)14);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        font.setItalic(false);

        XSSFFont fontTotal = (XSSFFont) weightSheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short)9);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        font.setItalic(false);

        headerStyle.setFont(font);
        totalStyle.setFont(fontTotal);


        Row header = weightSheet.createRow(0);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Інгредієнт");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Кухня");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Дата початку");

        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Нето на початок, г");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Прихід, г");
        header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Переміщення, г");
        header.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Списання, г");
        header.createCell(11, Cell.CELL_TYPE_STRING).setCellValue("Розхід, г");
        header.createCell(13, Cell.CELL_TYPE_STRING).setCellValue("Різниця на інв., г");
        header.createCell(15, Cell.CELL_TYPE_STRING).setCellValue("Нетто на кінець, г");

        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Нето на початок, UAH");
        header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Прихід, UAH");
        header.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Переміщення, UAH");
        header.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Списання, UAH");
        header.createCell(12, Cell.CELL_TYPE_STRING).setCellValue("Розхід, UAH");
        header.createCell(14, Cell.CELL_TYPE_STRING).setCellValue("Різниця на інв., UAH");
        header.createCell(16, Cell.CELL_TYPE_STRING).setCellValue("Нетто на кінець, UAH");

        header.createCell(17, Cell.CELL_TYPE_STRING).setCellValue("Вартість/кг");
        header.createCell(18, Cell.CELL_TYPE_STRING).setCellValue("Дата кінця");

        for(int j = 0; j < 19; j++)
            header.getCell(j).setCellStyle(headerStyle);

        int rowNum = 1;
        for (int i = 0; i < weightList.size(); i++) {
            ForReport fr = weightList.get(i);
            ForReport frp = moneyList.get(i);
            Row row = weightSheet.createRow( rowNum++ );
            row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(fr.getIngredientName());
            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(kitchNames.get(fr.getKitchen()));
            row.createCell(2, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getStart());
            row.getCell(2).setCellStyle(dateStyle);

            row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getNettoOnStart());
            row.getCell(3).setCellStyle(intNumStyle);
            row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getPurchase());
            row.getCell(5).setCellStyle(intNumStyle);
            row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getShift());
            row.getCell(7).setCellStyle(intNumStyle);
            row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getWriteOff());
            row.getCell(9).setCellStyle(intNumStyle);
            row.createCell(11, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getConsumption());
            row.getCell(11).setCellStyle(intNumStyle);
            row.createCell(13, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getInvDiff());
            row.getCell(13).setCellStyle(intNumStyle);
            row.createCell(15, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getNettoOnEnd());
            row.getCell(15).setCellStyle(intNumStyle);

            row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(frp.getNettoOnStart());
            row.getCell(4).setCellStyle(intNumStyle);
            row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(frp.getPurchase());
            row.getCell(6).setCellStyle(intNumStyle);
            row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(frp.getShift());
            row.getCell(8).setCellStyle(intNumStyle);
            row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(frp.getWriteOff());
            row.getCell(10).setCellStyle(intNumStyle);
            row.createCell(12, Cell.CELL_TYPE_NUMERIC).setCellValue(frp.getConsumption());
            row.getCell(12).setCellStyle(intNumStyle);
            row.createCell(14, Cell.CELL_TYPE_NUMERIC).setCellValue(frp.getInvDiff());
            row.getCell(14).setCellStyle(intNumStyle);
            row.createCell(16, Cell.CELL_TYPE_NUMERIC).setCellValue(frp.getNettoOnEnd());
            row.getCell(16).setCellStyle(intNumStyle);


            row.createCell(17, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.floor(fr.getIngCost()*1000));
            row.getCell(17).setCellStyle(intNumStyle);
            row.createCell(18, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getEnd());
            row.getCell(18).setCellStyle(dateStyle);

            if (fr.getKitchen().equals(-1)) {
                for(int j = 0; j < 19; j++) {
                    Cell cell = row.getCell(j);
                    CellStyle totalsCell = weightSheet.getWorkbook().createCellStyle();
                    totalsCell.cloneStyleFrom(cell.getCellStyle());
                    Font tFont = weightSheet.getWorkbook().createFont();
                    tFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    totalsCell.setFont(tFont);
                    cell.setCellStyle(totalsCell);
                }
            }
        }
        for(int j = 0; j < 19; j++)
            weightSheet.autoSizeColumn(j);

        weightSheet.createFreezePane(3, 1);
    }

    private static void fillMoneySheet (Sheet moneySheet) {
        Row header = moneySheet.createRow(0);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Інгредієнт");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Кухня");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Дата початку");
        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Сума на початок");
        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Прихід");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Переміщення");
        header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Списання");
        header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Розхід");
        header.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Різниця на інв.");
        header.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Сума на кінець");
        header.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Вартість/кг");
        header.createCell(11, Cell.CELL_TYPE_STRING).setCellValue("Дата кінця");

        int rowNum = 1;
        for (ForReport fr : moneyList) {
            Row row = moneySheet.createRow( rowNum++ );
            row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(fr.getIngredientName());
            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(kitchNames.get(fr.getKitchen()));
            row.createCell(2, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getStart());
            row.getCell(2).setCellStyle(dateStyle);
            row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getNettoOnStart());
            row.getCell(3).setCellStyle(intNumStyle);
            row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getPurchase());
            row.getCell(4).setCellStyle(intNumStyle);
            row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getShift());
            row.getCell(5).setCellStyle(intNumStyle);
            row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getWriteOff());
            row.getCell(6).setCellStyle(intNumStyle);
            row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getConsumption());
            row.getCell(7).setCellStyle(intNumStyle);
            row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getInvDiff());
            row.getCell(8).setCellStyle(intNumStyle);
            row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getNettoOnEnd());
            row.getCell(9).setCellStyle(intNumStyle);
            row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(Math.floor(fr.getIngCost()*1000));
            row.getCell(10).setCellStyle(intNumStyle);
            row.createCell(11, Cell.CELL_TYPE_NUMERIC).setCellValue(fr.getEnd());
            row.getCell(11).setCellStyle(dateStyle);
        }
        for(int j = 0; j < 12; j++)
            moneySheet.autoSizeColumn(j);
    }

    private static class ForReport {
        private Date start;
        private Date end;
        private Integer ingredient;
        private String ingredientName;
        private Integer kitchen;
        private Float ingCost = 0.0f;
        private Integer nettoOnStart = 0;
        private Integer nettoOnEnd = 0;
        
        private Integer purchase = 0;
        private Integer shift = 0;
        private Integer writeOff = 0;
        private Integer consumption = 0;
        private Integer invDiff = 0;

        public ForReport() {
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        public Integer getIngredient() {
            return ingredient;
        }

        public void setIngredient(Integer ingredient) {
            this.ingredient = ingredient;
        }

        public String getIngredientName() {
            return ingredientName;
        }

        public void setIngredientName(String ingredientName) {
            this.ingredientName = ingredientName;
        }

        public Integer getKitchen() {
            return kitchen;
        }

        public void setKitchen(Integer kitchen) {
            this.kitchen = kitchen;
        }

        public Float getIngCost() {
            return ingCost;
        }

        public void setIngCost(Float ingCost) {
            this.ingCost = ingCost;
        }

        public Integer getNettoOnStart() {
            return nettoOnStart;
        }

        public void setNettoOnStart(Integer nettoOnStart) {
            this.nettoOnStart = nettoOnStart;
        }

        public Integer getPurchase() {
            return purchase;
        }

        public void setPurchase(Integer purchase) {
            this.purchase = purchase;
        }

        public Integer getShift() {
            return shift;
        }

        public void setShift(Integer shift) {
            this.shift = shift;
        }

        public Integer getWriteOff() {
            return writeOff;
        }

        public void setWriteOff(Integer writeOff) {
            this.writeOff = writeOff;
        }

        public Integer getConsumption() {
            return consumption;
        }

        public void setConsumption(Integer consumption) {
            this.consumption = consumption;
        }

        public Integer getInvDiff() {
            return invDiff;
        }

        public void setInvDiff(Integer invDiff) {
            this.invDiff = invDiff;
        }

        public Integer getNettoOnEnd() {
            return nettoOnEnd;
        }

        public void setNettoOnEnd(Integer nettoOnEnd) {
            this.nettoOnEnd = nettoOnEnd;
        }
    }
}

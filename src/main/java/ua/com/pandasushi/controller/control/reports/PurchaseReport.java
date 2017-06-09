package ua.com.pandasushi.controller.control.reports;

import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ua.com.pandasushi.database.common.Operations;
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
public class PurchaseReport {
    private static XSSFCellStyle dateStyle;
    private static XSSFCellStyle intNumStyle;
    private static XSSFCellStyle floNumStyle;
    private static ArrayList<Operations> list;


    public static void createReport(Calendar from, Calendar to) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMM");
        String fileName = "product_purchase_" + sdf.format(from.getTime()) + "-" + sdf.format(to.getTime()) + ".xlsx";
        list = GlobalPandaApp.site.getOperationsByType(Operations.PRODUCT_PURCHASE);
        //list.addAll(GlobalPandaApp.site.getOperationsByType(Operations.DEBT_PURCHASE));
        ArrayList<Operations> workList;
        workList = list.stream().filter(operations -> operations.getKitchen().equals(GlobalPandaApp.config.getKitchen().getKitch_id()) && operations.getDate().after(from.getTime()) && operations.getDate().before(to.getTime())).collect(Collectors.toCollection(ArrayList::new));
        workList.sort((o1, o2) -> {
            if ( o1.getCheckId().compareTo(o2.getCheckId()) == 0)
                return o1.getSum().compareTo(o2.getSum());
            else
                return o1.getCheckId().compareTo(o2.getCheckId());
        });
        createReportProductPurchase(fileName, workList);
    }


    public static void createReportProductPurchase (String fileName, ArrayList<Operations> workList) {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFDataFormat df = book.createDataFormat();

        dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(df.getFormat("dd.mm.yyyy hh:mm"));
        intNumStyle = book.createCellStyle();
        intNumStyle.setDataFormat(df.getFormat("0"));
        floNumStyle = book.createCellStyle();
        floNumStyle.setDataFormat(df.getFormat("0.00"));

        fillProductsResult( book.createSheet("Підсумок"), workList );
        fillProductsPurchaseDetails( book.createSheet("Деталі"), workList );

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
        fileChooser.setInitialDirectory(new File(userDir +"/Desktop"));
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

    private static void fillProductsPurchaseDetails (Sheet detailsSheet, ArrayList<Operations> list) {
        Row header = detailsSheet.createRow(0);
        header.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("CHECK_ID");
        header.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Дата");
        header.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Контрагент");
        header.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Продукт");
        header.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Кількість");
        header.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Вага");
        header.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Сума");
        header.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Валюта");

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

    private static void fillProductsResult (Sheet resultSheet, ArrayList<Operations> list) {
        resultSheet.protectSheet("poiProtect");

        HashMap<String, Float> totalSum = new HashMap<>();
        HashMap<String, Float> totalOplacheno = new HashMap<>();

        //Float totalSum = 0.0f;
        //Float totalOplacheno = 0.0f;
        HashMap<Integer, Operations> result = new HashMap<>();
        for ( Operations op : list ) {
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
                totalSum.put(op.getCurrency(), 0.0f);
            totalSum.put(op.getCurrency(), totalSum.get(op.getCurrency()) + op.getSum());
        }

        for ( Operations op : result.values() ) {
            ArrayList<Operations> borg = GlobalPandaApp.site.getDebtPurchase(op.getCheckId());
            float opl = 0.0f;
            if( borg != null && borg.size() > 0)
                for (Operations bor : borg)
                    opl += bor.getSum();
            op.setFloatparameter1( opl );
            if(!totalOplacheno.containsKey(op.getCurrency()))
                totalOplacheno.put(op.getCurrency(), 0.0f);
            totalOplacheno.put(op.getCurrency(), totalOplacheno.get(op.getCurrency()) + opl);
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
}

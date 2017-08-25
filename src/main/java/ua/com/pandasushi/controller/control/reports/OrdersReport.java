package ua.com.pandasushi.controller.control.reports;

import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ua.com.pandasushi.database.common.CustomersSite;
import ua.com.pandasushi.database.common.OrdersSite;
import ua.com.pandasushi.database.common.menu.Menu;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Taras on 07.06.2017.
 */
public class OrdersReport {

    public static void createReport(Calendar from, Calendar to, int kitch) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMM");
        String fileName = "orders_" + sdf.format(from.getTime()) + "-" + sdf.format(to.getTime()) + ".xlsx";
        createOrdersReport(fileName, from, to, kitch);
    }


    private static void createOrdersReport (String fileName, Calendar from, Calendar to, int kitch) {
        SXSSFWorkbook book = (SXSSFWorkbook) createBook(from, to, kitch);


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


    private static Workbook createBook(Calendar from, Calendar to, int kitch) {
        SXSSFWorkbook book = new SXSSFWorkbook();
        Sheet customersSheet = book.createSheet("Звіт");
        Sheet ordersSheet = book.createSheet("Замовлення");
        Sheet analytics = book.createSheet("Аналітика");
        HashMap<Integer, Menu> menu = new HashMap<>();
        for (Menu menuItem : GlobalPandaApp.site.getMenu()) {
            menu.put(menuItem.getCode(), menuItem);
        }
        int oldCompCount = 0;
        int oldCompSum = 0;
        int oldCancelsCount = 0;
        int oldCancelsSum = 0;
        int totalSum = 0;
        int cancelSum = 0;
        int cancelCount = 0;
        int compSum = 0;
        int compCount = 0;
        int openSum = 0;
        int openCount = 0;
        int newBonusSum = 0;
        int oldBonusSum = 0;

        int pizzaCount = 0;
        int pizzaSum = 0;
        int sushiCount = 0;
        int sushiSum = 0;
        int drinksCount = 0;
        int drinksSum = 0;
        int soupCount = 0;
        int soupSum = 0;
        int saladCount = 0;
        int saladSum = 0;
        int gounkanCount = 0;
        int gounkanSum = 0;

        CellStyle style = book.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);

        Row header = customersSheet.createRow(0);
        Cell h1 = header.createCell(0);
        h1.setCellValue("Номер");
        Cell h2 = header.createCell(1);
        h2.setCellValue("Дата");
        Cell h3 = header.createCell(2);
        h3.setCellValue("Час");
        Cell h7 = header.createCell(3);
        h7.setCellValue("Партнер");
        Cell h8 = header.createCell(4);
        h8.setCellValue("Телефон");
        Cell h9 = header.createCell(5);
        h9.setCellValue("Ім'я");
        Cell h10 = header.createCell(6);
        h10.setCellValue("Адреса");
        Cell h12 = header.createCell(7);
        h12.setCellValue("Коментар");
        Cell h13 = header.createCell(8);
        h13.setCellValue("К-ть осіб");
        Cell h14 = header.createCell(9);
        h14.setCellValue("Знижка");
        Cell h15 = header.createCell(10);
        h15.setCellValue("Звідки дізнались");
        Cell h16 = header.createCell(11);
        h16.setCellValue("Сума замовлення");
        Cell h17 = header.createCell(12);
        h17.setCellValue("Бонус");
        Cell h18 = header.createCell(13);
        h18.setCellValue("До оплати");
        Cell h19 = header.createCell(14);
        h19.setCellValue("Нарахований бонус");
        Cell h20 = header.createCell(15);
        h20.setCellValue("Кур'єр");
        Cell h21 = header.createCell(16);
        h21.setCellValue("Об. час");
        Cell h22 = header.createCell(17);
        h22.setCellValue("Вихід кур'єра");
        Cell h23 = header.createCell(18);
        h23.setCellValue("Факт. час доставки");
        Cell h24 = header.createCell(19);
        h24.setCellValue("Компенсація");
        Cell h25 = header.createCell(20);
        h25.setCellValue("Статус");
        Cell h26 = header.createCell(21);
        h26.setCellValue("Кухня");
        Cell h27 = header.createCell(22);
        h27.setCellValue("Оператор");
        Cell h28 = header.createCell(23);
        h28.setCellValue("Повар");
        Cell h29 = header.createCell(24);
        h29.setCellValue("На час");
        Row headero = ordersSheet.createRow(0);
        Cell ho1 = headero.createCell(0);
        ho1.setCellValue("Номер");
        Cell ho2 = headero.createCell(1);
        ho2.setCellValue("Страва");
        Cell ho3 = headero.createCell(2);
        ho3.setCellValue("К-ть");
        Cell ho4 = headero.createCell(3);
        ho4.setCellValue("Знижка");
        Cell ho5 = headero.createCell(4);
        ho5.setCellValue("Сума");

        ArrayList<CustomersSite> customers;

        if (kitch >= 0)
            customers = GlobalPandaApp.site.getCustomers(from.getTime(), to.getTime(), GlobalPandaApp.config.getKitchen().getKitch_id());
        else
            customers = GlobalPandaApp.site.getCustomers(from.getTime(), to.getTime());

        System.out.println(customers.size());

        int c = 1;
        for (int i = 0; i < customers.size(); i++) {
            CustomersSite customer = customers.get(i);
            totalSum += customer.getFianlCost();
            if (customer.isCanceled()) {
                cancelCount++;
                cancelSum += customer.getFianlCost();
            } else {
                oldBonusSum += customer.getBonus();
                newBonusSum += customer.getNewBonus();
            }

            if (!customer.isDone()) {
                openCount++;
                openSum += customer.getFianlCost();
            }

            if (customer.getDeliverCost() > 0) {
                compCount++;
                compSum += customer.getDeliverCost();
            }
            Row row = customersSheet.createRow(i + 1);

            DataFormat format = book.createDataFormat();
            CellStyle dateStyle = book.createCellStyle();
            CellStyle timeStyle = book.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            timeStyle.setDataFormat(format.getFormat("HH:MM"));

            Cell chCode = row.createCell(0);
            chCode.setCellValue(customer.getCharCode());
            Cell date = row.createCell(1);
            date.setCellStyle(dateStyle);
            date.setCellValue(customer.getDate());
            Cell time = row.createCell(2);
            time.setCellStyle(timeStyle);
            time.setCellValue(customer.getDate());
            Cell kitchen = row.createCell(21);
            kitchen.setCellValue(customer.getKitchen() == 1 ? "Варшавська" : "Сихів");
            Cell operator = row.createCell(22);
            operator.setCellValue(customer.getOperator());
            Cell cook = row.createCell(23);
            cook.setCellValue(customer.getCook());
            Cell partner = row.createCell(3);
            partner.setCellValue(customer.getPartner());
            Cell phone = row.createCell(4);
            phone.setCellValue(customer.getPhone());
            Cell name = row.createCell(5);
            name.setCellValue(customer.getName());
            Cell address = row.createCell(6);
            String sAddress = customer.getStreet() + " " + customer.getHouse();
            if (!customer.getApartament().isEmpty())
                sAddress += " кв." + customer.getApartament();
            if (!customer.getPorch().isEmpty())
                sAddress += " під'їзд " + customer.getPorch();
            if (!customer.getFloor().isEmpty())
                sAddress += " пов." + customer.getFloor();
            if (!customer.getDoorcode().isEmpty())
                sAddress += " код " + customer.getDoorcode();
            address.setCellValue(sAddress);
            // Cell email = row.createCell(10);
            // email.setCellValue(customer.getEmail());
            Cell comment = row.createCell(7);
            comment.setCellValue(customer.getComment());
            Cell people = row.createCell(8);
            people.setCellValue(customer.getPeople());
            Cell discount = row.createCell(9);
            discount.setCellValue(customer.getDiscount() + "%");
            Cell info = row.createCell(10);
            info.setCellValue(customer.getInfo());
            Cell orderSum = row.createCell(11);
            orderSum.setCellValue(customer.getCost());
            Cell bonus = row.createCell(12);
            bonus.setCellValue(customer.getBonus());
            Cell finalSum = row.createCell(13);
            if (!customer.isCanceled())
                finalSum.setCellValue(customer.getFianlCost());
            else
                finalSum.setCellValue(0);
            Cell newBonus = row.createCell(14);
            newBonus.setCellValue(customer.getNewBonus());
            Cell courier = row.createCell(15);
            courier.setCellValue(customer.getCourier());
            Cell delTime = row.createCell(16);
            delTime.setCellStyle(timeStyle);
            delTime.setCellValue(customer.getDeliveryTime());
            Cell sendTime = row.createCell(17);
            sendTime.setCellStyle(timeStyle);
            if (customer.getSendTime() != null)
                sendTime.setCellValue(customer.getSendTime());
            Cell factTime = row.createCell(18);
            factTime.setCellStyle(timeStyle);
            if (customer.getDeliverTime() != null)
                factTime.setCellValue(customer.getDeliverTime());
            Cell compensation = row.createCell(19);
            compensation.setCellValue(customer.getDeliverCost());
            Cell status = row.createCell(20);

            if (customer.isCanceled())
                status.setCellValue("Відміна | " + customer.getCancelReason());
            if (customer.isDone() && !customer.isCanceled())
                status.setCellValue("Готово");

            Cell onTime = row.createCell(24);
            onTime.setCellValue(customer.isOnTime() ? "Так" : "Ні");

            for (int j = 0; j < customers.get(i).getOrdersSite().size(); j++) {
                OrdersSite order = customers.get(i).getOrdersSite().get(j);
                if (menu.get(order.getCode()).getColdHotAdds().contains("Г")
                        && menu.get(order.getCode()).getPizzaSushiDrinks().contains("П")) {
                    pizzaCount += order.getCount();
                    pizzaSum += order.getSum();
                }
                if (menu.get(order.getCode()).getColdHotAdds().contains("Ц"))
                    pizzaSum += order.getSum();
                if (menu.get(order.getCode()).getPizzaSushiDrinks().contains("Н")) {
                    drinksCount += order.getCount();
                    drinksSum += order.getSum();
                }

                if (menu.get(order.getCode()).getDish().contains("суп")) {
                    soupCount += order.getCount();
                    soupSum += order.getSum();
                }

                if (menu.get(order.getCode()).getPizzaSushiDrinks().contains("СГ")) {
                    gounkanCount += order.getCount();
                    gounkanSum += order.getSum();
                }

                if (menu.get(order.getCode()).getPizzaSushiDrinks().contains("S")) {
                    saladCount += order.getCount();
                    saladSum += order.getSum();
                }

                if (menu.get(order.getCode()).getPizzaSushiDrinks().contains("С") && !menu.get(order.getCode()).getColdHotAdds().contains("Д")) {
                    sushiCount += order.getCount();
                    sushiSum += order.getSum();
                }

                Row row1 = ordersSheet.createRow(c++);
                Cell orderNumber = row1.createCell(0);
                orderNumber.setCellValue(customer.getCharCode());
                Cell dish = row1.createCell(1);
                dish.setCellValue(order.getDish());
                Cell count = row1.createCell(2);
                count.setCellValue(order.getCount());
                Cell dscnt = row1.createCell(3);
                dscnt.setCellValue(order.getDiscount());
                Cell sum = row1.createCell(4);
                sum.setCellValue(order.getSum());
            }
        }

        Row aKitchen = analytics.createRow(2);
        Cell lk = aKitchen.createCell(0);
        lk.setCellValue("Кухня: ");
        Cell vk = aKitchen.createCell(1);
        vk.setCellValue(GlobalPandaApp.config.getKitchen().getKitch_id().intValue() == 0 ? "Сихів" : "Варшавська");

        Row aCols = analytics.createRow(4);
        Cell aCo = aCols.createCell(1);
        aCo.setCellValue("К-ть");
        Cell aSu = aCols.createCell(2);
        aSu.setCellValue("Сума");

        Row aLO = analytics.createRow(5);
        Cell lOl = aLO.createCell(0);
        lOl.setCellValue("Вчорашні замовлення: ");

        Row cancOlOr = analytics.createRow(6);
        Cell lCoo = cancOlOr.createCell(0);
        lCoo.setCellValue("Відмінені");
        Cell countOldCancel = cancOlOr.createCell(1);
        countOldCancel.setCellValue(oldCancelsCount);
        Cell sumOldCancel = cancOlOr.createCell(2);
        sumOldCancel.setCellValue(oldCancelsSum);

        Row compOlOr = analytics.createRow(7);
        Cell lOCom = compOlOr.createCell(0);
        lOCom.setCellValue("Компенсація");
        Cell countOldComp = compOlOr.createCell(1);
        countOldComp.setCellValue(oldCompCount);
        Cell sumOldComp = compOlOr.createCell(2);
        sumOldComp.setCellValue(oldCompSum);

        Row aNO = analytics.createRow(9);
        Cell lOn = aNO.createCell(0);
        lOn.setCellValue("Сьогоднішні замовлення: ");

        Row ofZam = analytics.createRow(10);
        Cell lOz = ofZam.createCell(0);
        lOz.setCellValue("Оформлені");
        Cell cOz = ofZam.createCell(1);
        cOz.setCellValue(customers.size());
        Cell sOz = ofZam.createCell(2);
        sOz.setCellValue(totalSum);

        Row caZam = analytics.createRow(11);
        Cell lCz = caZam.createCell(0);
        lCz.setCellValue("Відмінені");
        Cell cCz = caZam.createCell(1);
        cCz.setCellValue(cancelCount);
        Cell sCz = caZam.createCell(2);
        sCz.setCellValue(cancelSum);

        Row wcZam = analytics.createRow(12);
        Cell lWz = wcZam.createCell(0);
        lWz.setCellValue("Без відмінених");
        Cell cWz = wcZam.createCell(1);
        cWz.setCellValue(customers.size() - cancelCount);
        Cell sWz = wcZam.createCell(2);
        sWz.setCellValue(totalSum - cancelSum);

        Row kZam = analytics.createRow(13);
        Cell lKz = kZam.createCell(0);
        lKz.setCellValue("Компенсації");
        Cell cKz = kZam.createCell(1);
        cKz.setCellValue(compCount);
        Cell sKz = kZam.createCell(2);
        sKz.setCellValue(compSum);

        Row fZam = analytics.createRow(14);
        Cell lFz = fZam.createCell(0);
        lFz.setCellValue("Остаточна сума");
        Cell sFz = fZam.createCell(2);
        sFz.setCellValue(totalSum - cancelSum - compSum);

        Row oZam = analytics.createRow(15);
        Cell lOpz = oZam.createCell(0);
        lOpz.setCellValue("Відкриті");
        Cell cOpz = oZam.createCell(1);
        cOpz.setCellValue(openCount);
        Cell sOpz = oZam.createCell(2);
        sOpz.setCellValue(openSum);

        Row pizza = analytics.createRow(17);
        Cell lPi = pizza.createCell(0);
        lPi.setCellValue("Піца");
        Cell cPi = pizza.createCell(1);
        cPi.setCellValue(pizzaCount);
        Cell sPi = pizza.createCell(2);
        sPi.setCellValue(pizzaSum);

        Row rolly = analytics.createRow(18);
        Cell lRo = rolly.createCell(0);
        lRo.setCellValue("Роли");
        Cell cRo = rolly.createCell(1);
        cRo.setCellValue(sushiCount);
        Cell sRo = rolly.createCell(2);
        sRo.setCellValue(sushiSum);

        Row sushi = analytics.createRow(19);
        Cell lSu = sushi.createCell(0);
        lSu.setCellValue("Суші/Гункани");
        Cell cSu = sushi.createCell(1);
        cSu.setCellValue(gounkanCount);
        Cell sSu = sushi.createCell(2);
        sSu.setCellValue(gounkanSum);

        Row salad = analytics.createRow(20);
        Cell lSa = salad.createCell(0);
        lSa.setCellValue("Салати/Супи");
        Cell cSa = salad.createCell(1);
        cSa.setCellValue(saladCount);
        Cell sSa = salad.createCell(2);
        sSa.setCellValue(saladSum);

        Row soup = analytics.createRow(21);
        Cell lSo = soup.createCell(0);
        lSo.setCellValue("");
        Cell cSo = soup.createCell(1);
        //cSo.setCellValue(soupCount);
        Cell sSo = soup.createCell(2);
        //sSo.setCellValue(soupSum);

        Row drinks = analytics.createRow(22);
        Cell lDr = drinks.createCell(0);
        lDr.setCellValue("Напої");
        Cell cDr = drinks.createCell(1);
        cDr.setCellValue(drinksCount);
        Cell sDr = drinks.createCell(2);
        sDr.setCellValue(drinksSum);

        Row lBonus = analytics.createRow(25);
        Cell lLb = lBonus.createCell(0);
        lLb.setCellValue("Бонуси:");

        Row stLe = analytics.createRow(26);
        Cell lsl = stLe.createCell(0);
        lsl.setCellValue("Залишок на початок дня");
        Cell ssl = stLe.createCell(2);
        ssl.setCellValue("??");

        Row naDa = analytics.createRow(27);
        Cell lnD = naDa.createCell(0);
        lnD.setCellValue("Нараховано за день");
        Cell snD = naDa.createCell(2);
        snD.setCellValue(newBonusSum);

        Row vyDa = analytics.createRow(28);
        Cell lvD = vyDa.createCell(0);
        lvD.setCellValue("Витрачено за день");
        Cell svD = vyDa.createCell(2);
        svD.setCellValue(oldBonusSum);

        Row fiLe = analytics.createRow(29);
        Cell lfl = fiLe.createCell(0);
        lfl.setCellValue("Залишок на кінець дня");
        Cell sfl = fiLe.createCell(2);
        sfl.setCellValue("??");


		/*
         * Row row = customersSheet.createRow(customers.size() + 1); Cell
		 * cellSum = row.createCell(13);
		 * cellSum.setCellType(Cell.CELL_TYPE_FORMULA);
		 * cellSum.setCellFormula("SUM(N2:N" + (customers.size() + 1) + ")");
		 */

        int colC = 0;
        int colO = 0;
        int colA = 0;
        try {
            while (colC <= 24)
                customersSheet.autoSizeColumn(colC++);
            while (colO <= 4)
                ordersSheet.autoSizeColumn(colO++);
            while (colA <= 2)
                analytics.autoSizeColumn(colA++);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }
}

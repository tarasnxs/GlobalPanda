package ua.com.pandasushi.controller.costs.product_shift;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ua.com.pandasushi.database.common.Operations;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


class PdfMakerShift {
    private Document check;
    private ArrayList<Operations> productList;
    private PdfWriter writer;
    private float finalSum;

    PdfMakerShift(ArrayList<Operations> productList, float finalSum) throws DocumentException, IOException {
        this.productList = productList;
        this.finalSum = finalSum;
        check = new Document(PageSize.A5, 25, 25, 25, 25);
        writer = PdfWriter.getInstance(check, new FileOutputStream("check#" + productList.get(0).getCheckId() + ".pdf"));
    }

    void createPdf() throws DocumentException, IOException {
        check.open();

        if(productList.size() < 18) {

            Paragraph first = new Paragraph();
            first.add(getTable( productList, 0, 0));
            check.add(first);

        } else if (productList.size() < 36) {

            Paragraph first = new Paragraph();
            first.add(getTable(productList.subList(0, 18), 1, 2));
            check.add(first);

            check.newPage();

            Paragraph second = new Paragraph();
            second.add(getTable(productList.subList(18, productList.size()), 2, 2));
            check.add(second);

        } else {

            Paragraph first = new Paragraph();
            first.add(getTable(productList.subList(0, 18), 1, 3));
            check.add(first);

            check.newPage();

            Paragraph second = new Paragraph();
            second.add(getTable(productList.subList(18, 36), 2, 3));
            check.add(second);

            check.newPage();

            Paragraph third = new Paragraph();
            third.add(getTable(productList.subList(36, productList.size()), 3, 3));
            check.add(third);

        }

        check.close();

        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }

        try {
            if(desktop != null)
                desktop.open(new File("check#" + productList.get(0).getCheckId() + ".pdf"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private PdfPTable getTable (List<Operations> list, int pageNumber, int pageCount) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\Calibri.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font hFont = new Font(bf, 12, Font.BOLD);
        Font huFont = new Font(bf, 10, Font.BOLD);
        Font cFont = new Font(bf, 8, Font.NORMAL);

        PdfPTable mainTab = new PdfPTable(3);


        PdfPTable products = new PdfPTable(8);
        products.setWidthPercentage(100);
        products.setTotalWidth(100);
        int[] widths = {31,10,10,9,10,10,9,11};
        products.setWidths(widths);

        PdfPCell productH = new PdfPCell(new Phrase("Продукт", hFont));
        productH.setRowspan(2);
        productH.setVerticalAlignment(Element.ALIGN_CENTER);
        productH.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell poCheku = new PdfPCell(new Phrase("Відправлено", hFont));
        poCheku.setColspan(2);
        poCheku.setVerticalAlignment(Element.ALIGN_CENTER);
        poCheku.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cost = new PdfPCell(new Phrase("Вартість", hFont));
        cost.setRowspan(2);
        cost.setVerticalAlignment(Element.ALIGN_CENTER);
        cost.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell poFactu = new PdfPCell(new Phrase("Отримано", hFont));
        poFactu.setColspan(2);
        poFactu.setVerticalAlignment(Element.ALIGN_CENTER);
        poFactu.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell costPerUnit = new PdfPCell(new Phrase("Вартість за кг", hFont));
        costPerUnit.setRowspan(2);
        costPerUnit.setVerticalAlignment(Element.ALIGN_CENTER);
        costPerUnit.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell commentar = new PdfPCell(new Phrase("Прийняв", hFont));
        commentar.setRowspan(2);
        commentar.setVerticalAlignment(Element.ALIGN_CENTER);
        commentar.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell u1check = new PdfPCell(new Phrase("U1", huFont));
        u1check.setVerticalAlignment(Element.ALIGN_CENTER);
        u1check.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell u2check = new PdfPCell(new Phrase("U2, гр(мл)", huFont));
        u2check.setVerticalAlignment(Element.ALIGN_CENTER);
        u2check.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell u1fact = new PdfPCell(new Phrase("U1", huFont));
        u1fact.setVerticalAlignment(Element.ALIGN_CENTER);
        u1fact.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell u2fact = new PdfPCell(new Phrase("U2, гр(мл)", huFont));
        u2fact.setVerticalAlignment(Element.ALIGN_CENTER);
        u2fact.setHorizontalAlignment(Element.ALIGN_CENTER);

        products.addCell(productH);
        products.addCell(poCheku);
        products.addCell(cost);
        products.addCell(poFactu);
        products.addCell(costPerUnit);
        products.addCell(commentar);
        products.addCell(u1check);
        products.addCell(u2check);
        products.addCell(u1fact);
        products.addCell(u2fact);

        for(Operations op : list) {
            products.addCell(new PdfPCell(new Phrase(op.getStrparameter1(), new Font(bf, 9, Font.BOLDITALIC))));
            PdfPCell u1ch = new PdfPCell(new Phrase(op.getFloatparameter1() > 0.1 ? numFormat(op.getFloatparameter1()) + " " + op.getStrparameter2() : "-", cFont));
            u1ch.setVerticalAlignment(Element.ALIGN_BOTTOM);
            u1ch.setHorizontalAlignment(Element.ALIGN_RIGHT);
            products.addCell(u1ch);
            PdfPCell u2ch = new PdfPCell(new Phrase(numFormat(op.getFloatparameter2().intValue()), cFont));
            u2ch.setHorizontalAlignment(Element.ALIGN_RIGHT);
            u2ch.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(u2ch);
            PdfPCell cst = new PdfPCell(new Phrase(numFormat(op.getSum()), cFont));
            cst.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cst.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(cst);
            PdfPCell u1fac = new PdfPCell(new Phrase(op.getFloatparameter3() > 0.1 ? numFormat(op.getFloatparameter3()) + " " + op.getStrparameter2() : "-", cFont));
            u1fac.setHorizontalAlignment(Element.ALIGN_RIGHT);
            u1fac.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(u1fac);
            PdfPCell u2fac = new PdfPCell(new Phrase(numFormat(op.getFloatparameter4().intValue()), cFont));
            u2fac.setHorizontalAlignment(Element.ALIGN_RIGHT);
            u2fac.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(u2fac);
            PdfPCell cpu = new PdfPCell(new Phrase(op.getIntparameter4() > 0.1 ? numFormat(op.getIntparameter4()) + "" : "-", cFont));
            cpu.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cpu.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(cpu);
            products.addCell(new PdfPCell(new Phrase(op.getDescription2(), cFont)));
        }

        PdfPTable signs = new PdfPTable(3);
        PdfPCell contragent;
        PdfPCell operator;
        PdfPCell cook;

        if (productList.get(0).getBoolparameter1()) {
            contragent = new PdfPCell(new Phrase("Оператор : " + productList.get(0).getStrparameter4() + "\nДо оплати : " + finalSum + " " + productList.get(0).getCurrency(), hFont));
            operator = new PdfPCell(new Phrase("Кухар : " + productList.get(0).getDescription3() + "\n__________________", hFont));
            cook = new PdfPCell(new Phrase("Кур'єр : " + productList.get(0).getDescription2() + "\n__________________", hFont));
        } else {
            contragent = new PdfPCell(new Phrase("Оператор : " + productList.get(0).getOperator() + "\nДо оплати : " + finalSum + " " + productList.get(0).getCurrency(), hFont));
            operator = new PdfPCell(new Phrase("Кухар : " + productList.get(0).getDescription1() + "\n__________________", hFont));
            cook = new PdfPCell(new Phrase("Кур'єр : " + productList.get(0).getDescription2() + "\n__________________", hFont));
        }

        contragent.setVerticalAlignment(Element.ALIGN_BASELINE);
        contragent.setHorizontalAlignment(Element.ALIGN_LEFT);
        contragent.setBorder(Rectangle.NO_BORDER);

        operator.setVerticalAlignment(Element.ALIGN_BASELINE);
        operator.setHorizontalAlignment(Element.ALIGN_LEFT);
        operator.setBorder(Rectangle.NO_BORDER);

        cook.setVerticalAlignment(Element.ALIGN_BASELINE);
        cook.setHorizontalAlignment(Element.ALIGN_LEFT);
        cook.setBorder(Rectangle.NO_BORDER);

        signs.addCell(contragent);
        signs.addCell(operator);
        signs.addCell(cook);

        mainTab.setWidthPercentage(100);
        mainTab.setTotalWidth(100);
        mainTab.setWidths(new int[]{15,77,8});

        PdfPCell tabProd = new PdfPCell();
        tabProd.setRotation(270);
        tabProd.setFixedHeight(check.getPageSize().getHeight());
        tabProd.addElement(products);
        PdfPCell tabSigns = new PdfPCell();
        tabSigns.setRotation(270);
        tabSigns.setFixedHeight(check.getPageSize().getHeight());
        tabSigns.addElement(signs);

        PdfPCell info;
        if(pageNumber < 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            info = new PdfPCell(new Phrase(sdf.format(productList.get(0).getDate()) + " - Переміщення № " + productList.get(0).getCheckId(), hFont));
            info.setRotation(270);
            info.setFixedHeight(check.getPageSize().getHeight());
            info.setHorizontalAlignment(Element.ALIGN_RIGHT);
            info.setBorder(Rectangle.NO_BORDER);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            PdfPTable infoTab = new PdfPTable(2);
            PdfPCell checkNum = new PdfPCell(new Phrase(sdf.format(productList.get(0).getDate()) + " - Переміщення № " + productList.get(0).getCheckId(), hFont));
            PdfPCell pageNum = new PdfPCell(new Phrase("Сторінка № " + pageNumber + "/" + pageCount, hFont ));
            checkNum.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pageNum.setHorizontalAlignment(Element.ALIGN_LEFT);
            checkNum.setBorder(Rectangle.NO_BORDER);
            pageNum.setBorder(Rectangle.NO_BORDER);
            infoTab.addCell(pageNum);
            infoTab.addCell(checkNum);

            info = new PdfPCell(infoTab);
            info.setRotation(270);
            info.setFixedHeight(check.getPageSize().getHeight());
            info.setHorizontalAlignment(Element.ALIGN_RIGHT);
            info.setBorder(Rectangle.NO_BORDER);
        }

        mainTab.addCell(tabSigns);
        mainTab.addCell(tabProd);
        mainTab.addCell(info);

        return mainTab;
    }

    private String numFormat(float f) {
        return NumberFormat.getNumberInstance().format(f);
    }
}

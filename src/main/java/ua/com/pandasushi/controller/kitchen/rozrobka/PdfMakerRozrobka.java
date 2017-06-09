package ua.com.pandasushi.controller.kitchen.rozrobka;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ua.com.pandasushi.database.common.Rozrobka;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


class PdfMakerRozrobka {
    private Document check;
    private ArrayList<Rozrobka> rozrobkaList;
    private PdfWriter writer;
    private SimpleDateFormat sdf;

    PdfMakerRozrobka(ArrayList<Rozrobka> productList) throws DocumentException, IOException {
        this.rozrobkaList = productList;
        check = new Document(PageSize.A5, 25, 25, 25, 25);
        sdf = new SimpleDateFormat("dd.MM.yy");
        writer = PdfWriter.getInstance(check, new FileOutputStream("rozrobka#" + sdf.format(rozrobkaList.get(0).getDate()) + ".pdf"));
    }

    void createPdf() throws DocumentException, IOException {
        check.open();

        if(rozrobkaList.size() < 18) {

            Paragraph first = new Paragraph();
            first.add(getTable( rozrobkaList, 0, 0));
            check.add(first);

        } else if (rozrobkaList.size() < 36) {

            Paragraph first = new Paragraph();
            first.add(getTable(rozrobkaList.subList(0, 18), 1, 2));
            check.add(first);

            check.newPage();

            Paragraph second = new Paragraph();
            second.add(getTable(rozrobkaList.subList(18, rozrobkaList.size()), 2, 2));
            check.add(second);

        } else {

            Paragraph first = new Paragraph();
            first.add(getTable(rozrobkaList.subList(0, 18), 1, 3));
            check.add(first);

            check.newPage();

            Paragraph second = new Paragraph();
            second.add(getTable(rozrobkaList.subList(18, 36), 2, 3));
            check.add(second);

            check.newPage();

            Paragraph third = new Paragraph();
            third.add(getTable(rozrobkaList.subList(36, rozrobkaList.size()), 3, 3));
            check.add(third);

        }

        check.close();

        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }

        try {
            if(desktop != null)
                desktop.open(new File("rozrobka#" + sdf.format(rozrobkaList.get(0).getDate()) + ".pdf"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private PdfPTable getTable (List<Rozrobka> list, int pageNumber, int pageCount) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\Calibri.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font hFont = new Font(bf, 12, Font.BOLD);
        Font huFont = new Font(bf, 10, Font.BOLD);
        Font cFont = new Font(bf, 8, Font.NORMAL);

        PdfPTable mainTab = new PdfPTable(3);


        PdfPTable products = new PdfPTable(8);
        products.setWidthPercentage(100);
        products.setTotalWidth(100);
        int[] widths = {21,9,9,9,21,9,9,13};
        products.setWidths(widths);

        PdfPCell productH = new PdfPCell(new Phrase("Продукт", hFont));
        productH.setVerticalAlignment(Element.ALIGN_CENTER);
        productH.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell poCheku = new PdfPCell(new Phrase("К-ть", hFont));
        poCheku.setVerticalAlignment(Element.ALIGN_CENTER);
        poCheku.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cost = new PdfPCell(new Phrase("Розроб.", hFont));
        cost.setVerticalAlignment(Element.ALIGN_CENTER);
        cost.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell poFactu = new PdfPCell(new Phrase("Отримано", hFont));
        poFactu.setVerticalAlignment(Element.ALIGN_CENTER);
        poFactu.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell costPerUnit = new PdfPCell(new Phrase("Інгред.", hFont));
        costPerUnit.setVerticalAlignment(Element.ALIGN_CENTER);
        costPerUnit.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell commentar = new PdfPCell(new Phrase("Коеф.", hFont));
        commentar.setVerticalAlignment(Element.ALIGN_CENTER);
        commentar.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell u1check = new PdfPCell(new Phrase("Сер. коеф.", huFont));
        u1check.setVerticalAlignment(Element.ALIGN_CENTER);
        u1check.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell u2check = new PdfPCell(new Phrase("Коментар", huFont));
        u2check.setVerticalAlignment(Element.ALIGN_CENTER);
        u2check.setHorizontalAlignment(Element.ALIGN_CENTER);

        products.addCell(productH);
        products.addCell(poCheku);
        products.addCell(cost);
        products.addCell(poFactu);
        products.addCell(costPerUnit);
        products.addCell(commentar);
        products.addCell(u1check);
        products.addCell(u2check);

        for(Rozrobka op : list) {
            products.addCell(new PdfPCell(new Phrase(op.getProductName(), new Font(bf, 9, Font.BOLDITALIC))));
            PdfPCell u1ch = new PdfPCell(new Phrase(numFormat(op.getCountU1()), cFont));
            u1ch.setVerticalAlignment(Element.ALIGN_BOTTOM);
            u1ch.setHorizontalAlignment(Element.ALIGN_RIGHT);
            products.addCell(u1ch);
            PdfPCell u2ch = new PdfPCell(new Phrase(numFormat(op.getCountU2()), cFont));
            u2ch.setHorizontalAlignment(Element.ALIGN_RIGHT);
            u2ch.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(u2ch);
            PdfPCell cst = new PdfPCell(new Phrase(numFormat(op.getCountIng()), cFont));
            cst.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cst.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(cst);
            products.addCell(new PdfPCell(new Phrase(op.getIngredientName(), new Font(bf, 9, Font.BOLDITALIC))));
            PdfPCell u2fac = new PdfPCell(new Phrase(numFormat(op.getCoef()), cFont));
            u2fac.setHorizontalAlignment(Element.ALIGN_RIGHT);
            u2fac.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(u2fac);
            PdfPCell cpu = new PdfPCell(new Phrase(numFormat(GlobalPandaApp.site.getProdIngForProd(op.getProductName()).getAvgCoef()), cFont));
            cpu.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cpu.setVerticalAlignment(Element.ALIGN_BOTTOM);
            products.addCell(cpu);
            products.addCell(new PdfPCell(new Phrase(op.getComment(), cFont)));
        }


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

        PdfPCell info;
        if(pageNumber < 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            info = new PdfPCell(new Phrase(sdf.format(rozrobkaList.get(0).getDate()) + " - Старший зміни : " + GlobalPandaApp.config.getOperator().getName(), hFont));
            info.setRotation(270);
            info.setFixedHeight(check.getPageSize().getHeight());
            info.setHorizontalAlignment(Element.ALIGN_RIGHT);
            info.setBorder(Rectangle.NO_BORDER);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            PdfPTable infoTab = new PdfPTable(2);
            PdfPCell checkNum = new PdfPCell(new Phrase(sdf.format(rozrobkaList.get(0).getDate()) + " - Старший зміни : " + GlobalPandaApp.config.getOperator().getName(), hFont));
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

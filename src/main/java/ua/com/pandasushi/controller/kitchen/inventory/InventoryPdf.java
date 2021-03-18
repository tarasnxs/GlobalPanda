package ua.com.pandasushi.controller.kitchen.inventory;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ua.com.pandasushi.database.common.Inventory;

import com.itextpdf.text.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Тарас on 19.03.2017.
 */

public class InventoryPdf {
    private static final int ROW_COUNT = 18;
    private static final int COLUMN_COUNT = 5;
    private LinkedHashMap<Integer, ArrayList<Inventory>> list;
    private Document check;
    private PdfWriter writer;
    private Inventory inv;
    private int rowCount;
    private Header header;
    private String done;
    SimpleDateFormat sdf;

    InventoryPdf (LinkedHashMap<Integer, ArrayList<Inventory>> list) throws FileNotFoundException, DocumentException {
        this.list = list;
        check = new Document(PageSize.A5, 25, 25, 25, 25);
        System.out.println(list.values().size());
        System.out.println(list.keySet().size());
        inv = list.values().iterator().next().get(0);
        rowCount = 0;
        for (ArrayList<Inventory> invs : this.list.values())
            rowCount += invs.size();
        rowCount += this.list.keySet().size() - 1;
        done = list.values().iterator().next().get(0).getFactU2() != null ? "_done" : "";
        writer = PdfWriter.getInstance(check, new FileOutputStream("inv#" + inv.getCheckId() + done + ".pdf"));
        sdf = new SimpleDateFormat("dd.MM.yy");
        header = new Header("", "Інвентаризація №" + inv.getCheckId() + " Дата:" + sdf.format(inv.getBegin()));
    }



    void createPdf() throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\Calibri.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font hFont = new Font(bf, 12, Font.BOLD);
        Font iFont = new Font(bf, 10, Font.BOLD);
        Font pFont = new Font(bf, 10, Font.NORMAL);
        Font nFont = new Font(bf, 10, Font.ITALIC);
        Font cFont = new Font(bf, 6, Font.NORMAL);
        Font boldFont = new Font(bf, 6, Font.NORMAL);

        check.open();
        check.addHeader("Інвентаризація №" + inv.getCheckId(), "Дата: " + sdf.format(inv.getBegin()) + "; Старший зміни: " + inv.getCook());
        check.add(new Paragraph(new Phrase("Інвентаризація №" + inv.getCheckId() + "\nДата: " + sdf.format(inv.getBegin()) + "; Старший зміни: " + inv.getCook() + "\n\n", new Font(bf, 11, Font.UNDERLINE))));
        PdfPTable prods = new PdfPTable(COLUMN_COUNT);
        prods.setWidthPercentage(100);
        prods.setTotalWidth(100);
        int[] widths = {52,12,12,12,12};
        prods.setWidths(widths);

        PdfPCell hProdIng = new PdfPCell(new Phrase("Продукт/Інгредієнт", hFont));
        hProdIng.setRowspan(2);
        hProdIng.setHorizontalAlignment(Element.ALIGN_CENTER);
        hProdIng.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell hFirst = new PdfPCell(new Phrase("Спроба 1", hFont));
        hFirst.setColspan(2);
        hFirst.setHorizontalAlignment(Element.ALIGN_CENTER);
        hFirst.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell hSecond = new PdfPCell(new Phrase("Спроба 2", hFont));
        hSecond.setColspan(2);
        hSecond.setHorizontalAlignment(Element.ALIGN_CENTER);
        hSecond.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell hU1 = new PdfPCell(new Phrase("U1", hFont));
        hU1.setHorizontalAlignment(Element.ALIGN_CENTER);
        hU1.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell hU2 = new PdfPCell(new Phrase("U2, гр/мл", hFont));
        hU2.setHorizontalAlignment(Element.ALIGN_CENTER);
        hU2.setVerticalAlignment(Element.ALIGN_CENTER);

        prods.addCell(hProdIng);
        prods.addCell(hFirst);
        prods.addCell(hSecond);
        prods.addCell(hU1);
        prods.addCell(hU2);
        prods.addCell(hU1);
        prods.addCell(hU2);

        BaseColor closed = new BaseColor(170, 180, 190);

        int number = 1;

        for (Integer i : list.keySet()) {
            for (Inventory inv : list.get(i)) {
                PdfPCell prodIng = null;
                PdfPCell s1u1 = null;
                PdfPCell s1u2 = null;
                PdfPCell s2u1 = null;
                PdfPCell s2u2 = null;
                if (inv.getProdIngId() < 1700 || (inv.getProdIngId() >= 1900 && inv.getProdIngId() < 3000) ) {
                    prodIng = new PdfPCell(new Phrase(number++ + ". " + inv.getProdIngName(), iFont));

                    s1u1 = new PdfPCell();
                    s1u1.setBackgroundColor(closed);

                    s1u2 = new PdfPCell(new Phrase(inv.getAttemptU2() == null ? "" : NumberFormat.getNumberInstance().format(inv.getAttemptU2()), cFont));

                    s2u1 = new PdfPCell();
                    s2u1.setBackgroundColor(closed);

                    s2u2 = new PdfPCell(new Phrase(inv.getFactU2() == null ? "" : NumberFormat.getNumberInstance().format(inv.getFactU2()), cFont));

                } else if (inv.getProdIngId() > 1700 && inv.getProdIngId() < 1900) {
                    prodIng = new PdfPCell(new Phrase(inv.getProdIngName(), nFont));

                    s1u1 = new PdfPCell();
                    s1u1.setBackgroundColor(closed);

                    s1u2 = new PdfPCell(new Phrase(inv.getAttemptU2() == null ? "" : NumberFormat.getNumberInstance().format(inv.getAttemptU2()), cFont));

                    s2u1 = new PdfPCell();
                    s2u1.setBackgroundColor(closed);

                    s2u2 = new PdfPCell(new Phrase(inv.getFactU2() == null ? "" : NumberFormat.getNumberInstance().format(inv.getFactU2()), cFont));
                } else if (inv.getProdIngId() > 3000) {
                    prodIng = new PdfPCell(new Phrase(inv.getProdIngName(), pFont));

                    s1u1 = new PdfPCell(new Phrase( ( inv.getAttemptU1() == null ? "" : NumberFormat.getNumberInstance().format(inv.getAttemptU1()) ) + " " + inv.getUnit1(), cFont));

                    s1u2 = new PdfPCell(new Phrase(inv.getAttemptU2() == null ? "" : NumberFormat.getNumberInstance().format(inv.getAttemptU2()), cFont));

                    s2u1 = new PdfPCell(new Phrase( ( inv.getFactU1() == null ? "" : NumberFormat.getNumberInstance().format(inv.getFactU1()) ) + " " + inv.getUnit1(), cFont));


                    s2u2 = new PdfPCell(new Phrase(inv.getFactU2() == null ? "" : NumberFormat.getNumberInstance().format(inv.getFactU2()), cFont));

                    if (inv.getProdRelation() != null && inv.getProdRelation() > 0) {
                        s1u2.setBackgroundColor(closed);
                        s2u2.setBackgroundColor(closed);
                    }

                    if (inv.getUnit1() == null || inv.getUnit1().isEmpty() || inv.getUnit1().equals(" ")) {
                        s1u1.setBackgroundColor(closed);
                        s2u1.setBackgroundColor(closed);
                    }
                }

                if (inv.getFirstAttempt() != null && inv.getFirstAttempt()) {
                    s2u1 = new PdfPCell(new Phrase("", cFont));
                    s2u1.setBackgroundColor(closed);
                    s2u2 = new PdfPCell(new Phrase("", cFont));
                    s2u2.setBackgroundColor(closed);
                }

                if (inv.getAttemptU1() != null && !inv.getAttemptU1().equals(inv.getFactU1())) {
                    if (s2u1.getPhrase() != null)
                        s2u1.getPhrase().setFont(boldFont);
                }

                if (inv.getAttemptU2() != null && !inv.getAttemptU2().equals(inv.getFactU2())) {
                    if (s2u2.getPhrase() != null)
                        s2u2.getPhrase().setFont(boldFont);
                }

                prodIng.setHorizontalAlignment(Element.ALIGN_LEFT);
                s1u1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                s1u1.setVerticalAlignment(Element.ALIGN_BOTTOM);
                s1u2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                s1u2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                s2u1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                s2u1.setVerticalAlignment(Element.ALIGN_BOTTOM);
                s2u2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                s2u2.setVerticalAlignment(Element.ALIGN_BOTTOM);


                prods.addCell(prodIng);
                prods.addCell(s1u1);
                prods.addCell(s1u2);
                prods.addCell(s2u1);
                prods.addCell(s2u2);

            }
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(closed);
            cell.setBorderWidthRight(0);
            cell.setBorderWidthLeft(0);
            int n = COLUMN_COUNT;
            while (n-- > 0)
                prods.addCell(cell);
        }

        check.add(prods);

        check.add(new Paragraph(new Phrase("\nСтарший зміни: " + inv.getCook() + " - Підпис:_______________", new Font(bf, 13))));

        check.close();

        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }

        try {
            if(desktop != null)
                desktop.open(new File("inv#" + inv.getCheckId() + done + ".pdf"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

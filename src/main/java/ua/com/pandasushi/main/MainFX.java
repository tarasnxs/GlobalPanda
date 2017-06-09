package ua.com.pandasushi.main;

import com.sun.javafx.application.LauncherImpl;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainFX {

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMM HHmm");
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(  "logs/" + sdf.format(new Date()) + ".log");
            TeeOutputStream myOut = new TeeOutputStream(System.out, fos);
            TeeOutputStream errOut = new TeeOutputStream(System.err, fos);
            PrintStream ps = new PrintStream(myOut);
            PrintStream errPs = new PrintStream(errOut);
            System.setOut(ps);
            System.setErr(errPs);
        } catch (Exception e) {
            File folder = new File("logs");
            folder.mkdir();
            fos = new FileOutputStream(  "logs/" + sdf.format(new Date()) + ".log");
            TeeOutputStream myOut = new TeeOutputStream(System.out, fos);
            TeeOutputStream errOut = new TeeOutputStream(System.err, fos);
            PrintStream ps = new PrintStream(myOut);
            PrintStream errPs = new PrintStream(errOut);
            System.setOut(ps);
            System.setErr(errPs);
        }

        LauncherImpl.launchApplication(GlobalPandaApp.class, GlobalPandaPreloader.class, args);
    }

}

package logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Logger {
    
    private static PrintWriter writer;
    
    private static DateFormat _format = new SimpleDateFormat("mm:ss");
    
    private static long startTime = System.nanoTime();
    
    private static class ExitHook extends Thread {
        public void run() {
            long endTime = System.nanoTime();
            long elapsed = endTime - startTime;
            log("Execution took: " + elapsed / 1000000 + "ms");
            log("==========");
            writer.close();
        }
    }
    
    static {
        DateFormat format = new SimpleDateFormat("MM-dd::HH.mm.ss");
        String s = format.format(new Date());
        s = "github_parser_" + s + ".log";
        
        try {
            writer = new PrintWriter(s);
        } catch (IOException e) {
            System.out.println("Error creating debug logger\n Press Y to continue");
            Scanner scanner = new Scanner(System.in);
            if ( scanner.next().charAt(1) != 'Y') {
                System.exit(1);
            }
            scanner.close();
        }
        
        Runtime.getRuntime().addShutdownHook(new ExitHook());
    }
    
    private static String preface() {
        return "[" + _format.format(new Date()) + "] ";
    }
    
    static int _size = preface().length();
    private static int preface_size() {
        return _size;
    }
    
    public static void log(String s) {
        writer.println(preface() + s);
    }
    
    public static void log(Object o) {
        log(o.toString());
    }
    
    public static void logWithStackTrace(String str) {
        if (str != null)
            log(str);
        
        StackTraceElement[] arr = Thread.currentThread().getStackTrace();
        
        
        //String pref = " ".repeat(preface_size());
        StringBuilder pref = new StringBuilder();
        for (int i = 0; i < preface_size(); i++)
            pref.append(' ');
        
        /// ignore first 2 elements in the stack trace, info and this frame
        for (int i = 2; i < arr.length; i++)
            writer.println(pref.toString() + arr[i].toString());
        
    }
    
    public static void close() {
        writer.close();
    }
}

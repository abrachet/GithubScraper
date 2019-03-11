package web;

import conductor.JobConductor;
import conductor.ParseTask;
import logging.Logger;

import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;

public class ParseFile
        implements ParseTask
{
    public void parse(JobConductor pool, URL url) {
        
        int sloc = getSLOC(url);
        String filename = getFilename(url.toString());
        
        pool.addFileStats(filename, sloc);
    }
    
    
    private static int findSLOC(String line) {
        
        int index = line.indexOf('(');
        
        int i = ++index;
        for (; line.charAt(i) != ' '; i++);
        
        return Integer.parseInt(line.substring(index, i));
    }
    
    private static int getSLOC(URL url) {
        LineNumberReader reader;
        try {
            reader = PageDownload.downloadPage(url);
        } catch(IOException e) {
            Logger.log("couldn't download page " + url.toString());
            return -1;
        }
        
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.contains("<div class=\"file-info")) {
                    line = reader.readLine();
                    return findSLOC(line);
                }
            }
        } catch (IOException e) {
            //
        }
        
        return -1;
    }
    
    private static String getFilename(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
    
}

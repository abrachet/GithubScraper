package web;

import conductor.JobConductor;
import conductor.ParseTask;
import logging.Logger;

import java.io.IOException;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ParseRepository
        implements ParseTask
{
    public void parse(JobConductor pool, URL url) {
        LineNumberReader reader;
        try {
            reader = PageDownload.downloadPage(url);
        } catch(IOException e) {
            Logger.logWithStackTrace("Couldn't download url: " + url.toString());
            return;
        }
    
        Tuple2<ArrayList<URL>, ArrayList<URL>> tuple = findFilesAndDirs(reader);
        
        for (URL file : tuple._1) {
            pool.addTask(new ParseFile(), file);
        }
        
        for (URL dir : tuple._2)
            pool.addTask(new ParseRepository(), dir);
        
    }
    
    static Tuple2<ArrayList<URL>, ArrayList<URL>> findFilesAndDirs(LineNumberReader reader) {
        ArrayList<URL> files = new ArrayList<>();
        ArrayList<URL> dirs  = new ArrayList<>();
        
        final String ariaLabel = "<svg aria-label=\"";
        final String href = "href=\"";
        
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.contains("<tr class=\"js-navigation-item\">")) {
                    reader.readLine();
                    line = reader.readLine();
                    
                    
                    int index = line.indexOf(ariaLabel);
    
                    // the problem is more severe than just a continue, but too much work to deal with
                    if (index == -1)
                        continue;
                    
                    index += ariaLabel.length();
                    
                    String type = UrlHelper.appendUntilQuote(line, index);
                    
                    
                    reader.readLine();
                    reader.readLine();
                    reader.readLine();
                    
                    line = reader.readLine();
                    
                    index = line.indexOf(href) + href.length();
                    
                    String url = UrlHelper.appendUntilQuote(line, index);
                    
                    URL newUrl;
                    
                    try {
                         newUrl = new URL(UrlHelper.GITHUB + url);
                        
                    } catch (MalformedURLException e) {
                        continue;
                    }
                    
                    if (type.equals("directory"))
                        dirs.add(newUrl);
                    else if (type.equals("file"))
                        files.add(newUrl);
                        
                }
            }
        } catch (Exception e) {
            Logger.log("");
        }
        
        
        return new Tuple2<ArrayList<URL>, ArrayList<URL>>(files, dirs);
    }
    
}

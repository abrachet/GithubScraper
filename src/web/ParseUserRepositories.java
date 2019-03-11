package web;

import conductor.JobConductor;
import conductor.ParseTask;
import logging.Logger;

import java.io.IOException;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ParseUserRepositories
        implements ParseTask
{
    
    public void parse(JobConductor pool, URL url) {
        _parse(pool, url);
    }
    
    private static void _parse(JobConductor pool, URL url) {
   
        LineNumberReader reader;
        try {
            reader = PageDownload.downloadPage(url);
        } catch (IOException e) {
            Logger.log("IOException when downloading page");
            return;
        }
    
        Tuple2<ArrayList<String>, String> tuple = findRepositories(reader);
    
        if (tuple == null) {
            // it shouldn't be null it should be <{}, null>
            Logger.log("got null tuple from ParseUserRepositories.findRepositories");
            return;
        }
    
        for (String s : tuple._1) {
            URL new_url;
            try {
                new_url = new URL(UrlHelper.GITHUB + s);
            } catch (MalformedURLException e) {
                Logger.logWithStackTrace("Malformed url from: " + s);
                continue;
            }
        
            pool.addTask(new ParseRepository(), new_url);
        }
    
        if (tuple._2 != null) {
            try {
                url = new URL(UrlHelper.GITHUB + tuple._2);
            } catch (Exception e) {
                Logger.logWithStackTrace("Malformed url from: " + tuple._2);
            }
        
            pool.addTask(new ParseUserRepositories(), url);
        }
    }
    
    public static URL getUserRepoFromHomePage(String str) {
        if (str == null) {
            Logger.logWithStackTrace("Got null String");
            return null;
        }
        
        URL url;
        try {
            url = new URL(str + "?tab=repositories");
        } catch (MalformedURLException e) {
            Logger.log(str + "?tab=repositories was malformed" );
            return null;
        }
        
        return url;
    }
    
    
    private static String urlFromRepoLine(String s) {
        final String find = "href=\"";
        int index = s.indexOf(find);
        
        if (index == -1)
            return null;
        
        return UrlHelper.appendUntilQuote(s, index + find.length());
    }
    
    private static Tuple2<ArrayList<String>, String> findRepositories(LineNumberReader reader) {
        ArrayList<String> arr = new ArrayList<>();
        String nextURL = null;
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()){
                if (line.contains("itemprop=\"name codeRepository\"")) {
                    arr.add(urlFromRepoLine(line));
                } else if (line.contains("Next")) {
                    nextURL = TraversePage.next_URL(line);
                }
            }
        } catch (Exception e) {
            //
        }
        
        return new Tuple2<>(arr, nextURL);
    }
    
}

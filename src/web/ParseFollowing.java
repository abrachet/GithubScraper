package web;

import conductor.JobConductor;
import conductor.ParseTask;
import logging.Logger;

import java.io.IOException;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ParseFollowing
        implements ParseTask
{
    public void parse(JobConductor pool, URL url) {
        while (url != null) {
            LineNumberReader reader;
            try {
                reader = PageDownload.downloadPage(url);
            } catch (IOException e) {
                Logger.log("IOException when downloading page");
                return;
            }
            
            Tuple2<ArrayList<String>, String> tuple = findUsers(reader);
            
            // strings are users
            for (String s : tuple._1) {
                URL newUrl;
                try {
                    newUrl = new URL(UrlHelper.GITHUB + s);
                } catch (MalformedURLException e) {
                    continue;
                }
                
                pool.addTask(new ParseUsername(), newUrl);
            }
            
            if (tuple._2 == null)
                break;
            
            try {
                url = new URL(tuple._2);
            } catch (MalformedURLException e) {
                break;
            }
            
        }
    }
    
    public static URL getUserFollowers(String url) {
        url += "?tab=followers";
        
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            Logger.log(url + "was malformed");
            return null;
        }
    }
    
   
    
    public static Tuple2<ArrayList<String>, String> findUsers(LineNumberReader reader) {
        ArrayList<String> arr = new ArrayList<>();
        
        final String follow_users = "href=\"";
        
        String next_url = null;
        
        try {
            
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                
                if (line.contains("<a class=\"d-inline-block\" data-hovercard-type=\"user\"")) {
                    
                    int index = line.indexOf(follow_users) + follow_users.length();
                    
                    arr.add(UrlHelper.appendUntilQuote(line, index));
                } else if (line.contains("Next")) {
                    next_url = TraversePage.next_URL(line);
                }
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return new Tuple2<>(arr, next_url);
    }
}

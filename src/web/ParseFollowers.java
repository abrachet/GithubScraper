package web;

import conductor.JobConductor;
import conductor.ParseTask;
import logging.Logger;

import java.net.MalformedURLException;
import java.net.URL;

public class ParseFollowers
        implements ParseTask
{
    public void parse(JobConductor pool, URL url) {
        ParseFollowing fakeFollower = new ParseFollowing();
        
        fakeFollower.parse(pool, url);
    }
    
    
    public static URL getUserFollowers(String url) {
        url += "?tab=following";
        
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            Logger.log(url + "was malformed");
            return null;
        }
    }
}

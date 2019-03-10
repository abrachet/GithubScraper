package web;

import conductor.JobConductor;
import conductor.ParseTask;
            
import java.net.URL;

/**
 * Parses a users homepage
 * ie user oracle would be parsing the url https://github.com/oracle
 */
public class ParseUsername
        implements ParseTask
{
    public void parse(JobConductor pool, URL url) {
        
        String user = UrlHelper.stripGithub(url);
        if (pool.alreadySeen(user))
            return;
        
        // mark user so we don't go through their repos again
        pool.markUser(user);
        
        
        String urlStr = url.toString();
        
        URL repo = ParseUserRepositories.getUserRepoFromHomePage(urlStr);
        
        System.out.println(repo);
        
        pool.addTask(new ParseUserRepositories(), repo);
        
        URL following = ParseFollowing.getUserFollowers(urlStr);
        
        pool.addTask(new ParseFollowing(), following);
        
        URL followers = ParseFollowers.getUserFollowers(urlStr);
        
        pool.addTask(new ParseFollowers(), followers);
        
    }
}

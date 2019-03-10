package conductor;

import logging.Logger;

import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public final class JobConductor {
    
    private ForkJoinPool pool;
    // used to see before a url is parsed if it has been used already
    private HashSet<String> users;
    private LineCounter lineCounter;
    
    public JobConductor() {
        pool        = new ForkJoinPool();
        users       = new HashSet<>();
        lineCounter = new LineCounter();
    }
    
    public JobConductor(HashSet<String> set) {
        pool = new ForkJoinPool();
        this.users = set;
    }
    
    public JobConductor(HashSet<String> set, LineCounter lineCounter) {
        pool = new ForkJoinPool();
        this.users = set;
        this.lineCounter = lineCounter;
    }
    
    private class ParseTaskRunnable implements Runnable {
        JobConductor pool;
        ParseTask task;
        URL url;
        
        public ParseTaskRunnable(JobConductor pool, ParseTask task, URL url) {
            this.pool = pool;
            this.task = task;
            this.url = url;
        }
        
        public void run() {
            task.parse(pool, url);
        }
    }
    
    public void addTask(ParseTask run, URL url) {
        if (run == null || url == null) {
            Logger.logWithStackTrace("Got null references");
            return;
        }
        
        pool.execute(new ParseTaskRunnable(this, run, url));
    }
    
    /**
     * Put a username in a set to mark them as seen to not try to parse their repos again
     * @param str the name of the user like "/username"
     */
    public void markUser(String str) {
        if (str.contains("https://www.github.com")) {
            Logger.logWithStackTrace("username was incorrectly marked: " + str);
            return;
        }
        
        if (users.contains(str)) {
            Logger.log("set already contained user: " + str);
            Logger.log(Thread.currentThread().getStackTrace());
        }
    }
    
    public boolean alreadySeen(String str) {
        if (str.contains("https://www.github.com")) {
            Logger.logWithStackTrace("username was incorrectly marked: " + str);
            return true;
        }
        
        return users.contains(str);
    }
    
    public void addFileStats(String filename, int sloc) {
        lineCounter.insert(filename, sloc);
    }
    
    public void printStats() {
        lineCounter.printStats();
    }
    
    
    public void passiveWait() throws java.lang.InterruptedException {
        pool.awaitTermination(1, TimeUnit.DAYS);
    }
}

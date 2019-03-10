package conductor;

import logging.Logger;
import web.ParseUsername;

import java.io.*;
import java.net.URL;

public class Main {
    private static final String serializationFile = "PickUpFromLast.ser";
    private static JobConductor pool = new JobConductor();
    
    // just cleans main up a bit
    static {
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            try {
                FileOutputStream fOut = new FileOutputStream(serializationFile);
                ObjectOutputStream oOut = new ObjectOutputStream(fOut);
                oOut.writeObject(pool);
            } catch (Exception e) {
                Logger.logWithStackTrace("Bad news bears!");
            }
        }));
    
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            pool.printStats();
        }));
    }
    
    public static void main(String[] args) throws Exception {
        
        File f = new File(serializationFile);
        
        if (!f.exists()) {
            URL url = new URL("https://github.com/abrachet");
    
            pool.addTask(new ParseUsername(), url);
        } else {
            FileInputStream file = new FileInputStream(serializationFile);
            ObjectInputStream in = new ObjectInputStream(file);
            pool = (JobConductor) in.readObject();
        }
    
        // doesn't end until there are no more found users on github
        pool.passiveWait();
    }
    
}

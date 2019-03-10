package web;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;

public class PageDownload {
    
    public static LineNumberReader downloadPage(URL url) throws IOException {
        
        InputStream is = url.openStream();
        
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
        
        return reader;
    }
}

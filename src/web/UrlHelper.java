package web;

import java.net.URL;



public class UrlHelper {
    public static final String GITHUB = "https://github.com";
    public static final int PREFACE_LEN = GITHUB.length();
    
    private UrlHelper() {}
    
    
    public static String stripGithub(URL url) {
        String s = url.toString();
        
        return s.substring(PREFACE_LEN);
    }
    
    public static String appendUntilQuote(String s, int start) {
        StringBuilder str = new StringBuilder();
        
        for (int i = start; ; i++) {
            char c = s.charAt(i);
            if (c == '"')
                break;
            
            str.append(c);
        }
        
        return str.toString();
    }
    
}

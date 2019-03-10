package web;

public class TraversePage {
    
    
    public static String next_URL(String lineWithNext) {
        return UrlHelper.appendUntilQuote(lineWithNext, lineWithNext.indexOf("href=\"") + "href=\"".length());
        
    }
    
}

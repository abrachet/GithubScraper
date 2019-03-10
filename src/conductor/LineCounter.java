package conductor;

import languages.Language;
import languages.LanguageExtensions;
import logging.Logger;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LineCounter {
    public static class LanguageData {
        private long totalFiles;
        private int averageLineNum;
        
        public LanguageData(int lineNum) {
            totalFiles = 1;
            averageLineNum = lineNum;
        }
        
        public LanguageData add(int lineSize) {
            BigInteger totalLines = BigInteger.valueOf(totalFiles);
            totalLines = totalLines.multiply( BigInteger.valueOf(averageLineNum));
            
            totalLines = totalLines.add( BigInteger.valueOf(lineSize) );
            
            totalFiles++;
            
            totalLines = totalLines.divide( BigInteger.valueOf(totalFiles) );
            
            averageLineNum = totalLines.intValue();
            
            return this;
        }
    
        public long getTotalFiles() {
            return totalFiles;
        }
    
        public int getAverageLineNum() {
            return averageLineNum;
        }
    }
    
    
    private ConcurrentMap<Language, LanguageData> map;
    
    public LineCounter() {
        map = new ConcurrentHashMap<>();
    }
    
    
    public void insert(String filename, int lineNum) {
        Language lang = LanguageExtensions.getLanguage(filename);
        
        LanguageData from_map = map.get(lang);
        
        if (from_map == null)
            map.put(lang, new LanguageData(lineNum));
        else
            map.put(lang, from_map.add(lineNum));
        
    }
    
    public void mergeCounters(LineCounter other){
        Logger.log("Not implemented");
    }
    
    public void printStats() {
        map.forEach( (lang, data) ->
            System.out.printf("%s has an average of %d sloc\n", lang.toString(), data.getAverageLineNum())
        );
    }
}
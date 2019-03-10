package languages;

import logging.Logger;

import java.util.HashMap;

public class LanguageExtensions {
    
    private static HashMap<String, Language> map = new HashMap<>();
    
    static {
        map.put("c", Language.C);
        map.put("h", Language.C); // most .h files will correspond to C++ though
        
        map.put("js", Language.JavaScript);
        map.put("mjs", Language.JavaScript);
        
        map.put("py", Language.Python);
        map.put("pyc", Language.Python);
        map.put("pyd", Language.Python);
        map.put("pyo", Language.Python);
        
        map.put("java", Language.Java);
        
        map.put("C", Language.CXX);
        map.put("cc", Language.CXX);
        map.put("cpp", Language.CXX);
        map.put("cxx", Language.CXX);
        map.put("c++", Language.CXX);
        map.put("hh", Language.CXX);
        map.put("hpp", Language.CXX);
        map.put("hxx", Language.CXX);
        map.put("h++", Language.CXX);
        
        map.put("swift", Language.Swift);
        
        map.put("ts", Language.TypeScript);
        map.put("tsx", Language.TypeScript);
        
        map.put("go", Language.Go);
        
        map.put("sql", Language.SQL);
        
        map.put("rb", Language.Ruby);
        
        map.put("r", Language.R);
        map.put("R", Language.R);
        
        map.put("php", Language.Php);
        map.put("phtml", Language.Php);
        map.put("php3", Language.Php);
        map.put("php4", Language.Php);
        map.put("php5", Language.Php);
        map.put("php7", Language.Php);
        map.put("phps", Language.Php);
        map.put("php-s", Language.Php);
        map.put("pht", Language.Php);
        
        map.put("pl", Language.Perl);
        map.put("pm", Language.Perl);
        map.put("xs", Language.Perl);
        map.put("t", Language.Perl);
        map.put("pod", Language.Perl);
        
        map.put("kt", Language.Kotlin);
        map.put("kts", Language.Kotlin);
        
        map.put("cs", Language.CSharp);
        
        map.put("rs", Language.Rust);
        
        map.put(".scm", Language.Scheme);
        map.put(".ss", Language.Scheme);
        
        map.put("erl", Language.Erlang);
        map.put("hrl", Language.Erlang);
        
        map.put("scala", Language.Scala);
        map.put("sc", Language.Scala);
        
        map.put("hs", Language.Haskell);
        map.put("lhs", Language.Haskell);
        
        map.put("lisp", Language.Lisp);
        map.put("lsp", Language.Lisp);
        map.put("l", Language.Lisp);
        map.put("cl", Language.Lisp);
        map.put("fasl", Language.Lisp);
        
        map.put("m", Language.ObjectiveC);
        map.put("mm", Language.ObjectiveC);
        map.put("M", Language.ObjectiveC);
    
        map.put("vb", Language.VisualBasic);
        
        map.put("pascal", Language.Pascal);
        map.put("pas", Language.Pascal);
        map.put("inc", Language.Pascal);
        
        map.put("f", Language.Fortran);
        map.put("for", Language.Fortran);
        map.put("f90", Language.Fortran);
        
        map.put("md", Language.Markdown);
        map.put("rst", Language.RestructuredText);
        map.put("txt", Language.Text);
    }
    
    
    /**
     *
     * @return null if there is no extension
     */
    private static String findExtension(String filename) {
        
        if (!filename.contains("."))
            return null;
        
        return filename.substring(filename.indexOf('.') + 1);
    }
    
    /**
     *
     * @param str full filename
     * @return enum Language corresponding to the found langauge
     */
    public static Language getLanguage(String str) {
        if (str == null) {
            Logger.logWithStackTrace("Should not have gotten a null string");
            return Language.Unknown;
        }
        
        if (str.equals("LICENSE"))
            return Language.License;
        
        String extension = findExtension(str);
        if (extension == null)
            return Language.NoExtension;
        
        
        Language lang = map.get(extension);
        
        return lang == null ? Language.Unknown : lang;
    }
    
}

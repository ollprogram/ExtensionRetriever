package fr.ollprogram.extensionretriever;

/**
 * Only supported since java 17.
 */
public sealed interface Result permits AnalyseResult, ApplyResult, SelectResult{
    String toString();
}

/**
 * A result for an analysing.
 * @param filesSelected Number of files selected.
 * @param knownExtensions Number of known extensions (retrieved with the algorithm)
 * @param invalidExtensions Number of extensions which are different of their real extension.
 */
record AnalyseResult(int filesSelected, int knownExtensions, int invalidExtensions) implements Result{
    @Override
    public String toString(){
        return "Files analysed : "+filesSelected()+"\n" +
                "Files with known extensions : "+knownExtensions()+"\n"+
                "Files unknown : "+(filesSelected - knownExtensions())+"\n" +
                "Files with extension different to their real extension :"+invalidExtensions();
    }
}

/**
 * The result of changing extensions.
 * @param filesSelected Number of selected files.
 * @param success Number of files renamed.
 */
record ApplyResult(int filesSelected, int success) implements Result {
    @Override
    public String toString(){
        return "Files selected : "+filesSelected()+"\n" +
                "Files modified : "+success()+"/"+filesSelected();
    }
}

/**
 * Indicating how many files are selected.
 * @param filesSelected Number of selected files.
 */
record SelectResult(int filesSelected) implements Result {
    @Override
    public String toString(){
        return "Files selected : "+filesSelected();
    }
}
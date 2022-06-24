package fr.ollprogram.extensionretriever;

/**
 * A simple progress bar for the console.
 */
public class ProgressBar {
    private int step;
    private final String elementName;
    private final String title;
    private final int finalStep;

    /**
     *
     * @param from Where to start counting.
     * @param to The end number for counting to.
     * @param elementName The name of the element which we are counting.
     * @param title The title for this progressBar.
     */
    public ProgressBar(int from, int to, String elementName, String title){
        step = from;
        finalStep = to;
        this.title = title;
        this.elementName = elementName;
        System.out.print(title+" [                                        ]["+step+"/"+finalStep+"]["+elementName+"]\r");
        //containing 40 spaces for the progress bar
    }

    /**
     * Step over.
     * @param n Number of steps to pass.
     */
    public void stepOver(int n){
        step+=n;
        System.out.print(title+" ["+steps()+spaces()+"]["+step+"/"+finalStep+"]["+elementName+"]\r");
        if(step == finalStep) System.out.println();
    }

    /**
     *
     * @return The spaces for the end of the progress bar.
     */
    private String spaces(){
        StringBuilder buff = new StringBuilder();
        for(int i = (step*40)/finalStep; i<40; i++){
            buff.append(" ");
        }
        return buff.toString();
    }

    /**
     *
     * @return The characters representing all steps of the progress bar.
     */
    private String steps(){
        StringBuilder buff = new StringBuilder();
        for(int i = 0; i<(step*40)/finalStep; i++){
            buff.append("=");
        }
        return buff.toString();
    }

}

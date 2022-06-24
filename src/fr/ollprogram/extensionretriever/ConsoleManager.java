package fr.ollprogram.extensionretriever;

import java.util.Scanner;

/**
 * A simple tool to manipulate system input and output from the console easier.
 */
public class ConsoleManager {
    private final Scanner scanner;

    /**
     * Constructor
     */
    public ConsoleManager(){
        scanner = new Scanner(System.in);
    }

    /**
     *
     * @param s A string which may contain a number sequence.
     * @return True if s is only composed of numbers.
     */
    private static boolean isNumbers(String s){
        if(s == null) return false;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if (c > '9' || c < '0') return false;
        }
        return true;
    }

    /**
     *
     * @param from The first number inside the interval
     * @param to THe last number inside the interval
     * @return The integer number inside the interval [from;to] chosen by the user.
     */
    public int numberInput(int from, int to){
        String line;
        System.out.println("Input a number between "+from+" and "+to+" : ");
        while(!isNumbers((line = scanner.nextLine()))
                || (Integer.parseInt(line) < from || Integer.parseInt(line) > to)){
            System.out.println("Wrong input. \n Input a number between "+from+" and "+to+" : ");
        }
        return Integer.parseInt(line);
    }

    /**
     *
     * @param title The title to write before the list.
     * @param choices The different choices names.
     * @return The index in choices of the choice made by the user.
     */
    public int choiceList(String title, String[]choices){
        System.out.println(title);
        for(int i = 0; i < choices.length; i++){
            System.out.println(i+". "+choices[i]);
        }
        return numberInput(0, choices.length-1);
    }

    /**
     *
     * @param title The title (message) to write before the input.
     * @return The string input by the user.
     */
    public String stringInput(String title){
        System.out.println(title);
        return scanner.nextLine();
    }

    /**
     * Print a message to the console.
     * @param message Message to be printed.
     */
    public void println(String message){System.out.println(message);}

    /**
     * Close the console.
     */
    public void close(){
        scanner.close();
    }
}

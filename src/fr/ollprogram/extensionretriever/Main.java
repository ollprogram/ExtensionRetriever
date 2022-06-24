package fr.ollprogram.extensionretriever;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ConsoleManager console = new ConsoleManager();
        console.println("Bonjour "+System.getProperty("user.name")+"!");
        try {//TODO mettre à jour le xml.
            ExtensionRetriever extR = new ExtensionRetriever();
            boolean running = true;
            while(running) {
                String[] choices = {"Add file to selection",
                        "Add all files from a directory to a selection",
                        "Analyse extensions", "Change all extensions (rename)","Clear selection", "Exit"};
                int choice = console.choiceList("Choose an option :", choices);
                Result result = null;
                switch (choice) {
                    case 0 -> result = extR.selectFile(console.stringInput("Absolute path for this file :"));
                    case 1 -> result = extR.selectDirectory(console.stringInput("Absolute path for a directory :"));
                    case 2 -> result = extR.analyse();
                    case 3 -> result = extR.applyExtensions();
                    case 4 -> result = extR.clearSelection();
                    case 5 -> running = false;
                }
                if(result != null) {System.out.println(result);}
            }
        } catch (IOException e){
            console.println("Missing fileTypes.xml");
        } catch(ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        console.close();
    }
}

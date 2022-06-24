package fr.ollprogram.extensionretriever;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A tool to retrieve the true extension of a file.
 */
public class ExtensionRetriever {

    /**
     * Represent a file signature header (offset and signature)
     */
    private static class Head implements Comparable<Head>{
        String hex;
        int offset;
        public Head(String hex, int offset){
            this.hex = hex;
            this.offset = offset;
        }
        @Override
        public int compareTo(Head o) {
            return (hex).compareTo(o.hex);
        }
    }

    private ArrayList<File> selection;
    private final Document xml;
    private Map<Head, String> mappedXML;
    private Map<String, String> result;

    /**
     * Constructor
     * @throws IOException if a file can't be accessed.
     * @throws ParserConfigurationException If the XML document can't be parsed.
     * @throws SAXException if there is a problem with the XML parser.
     * @throws XPathExpressionException if the xpath expressions used are wrong.
     */
    public ExtensionRetriever() throws IOException,
            ParserConfigurationException, SAXException, XPathExpressionException {
        FileInputStream fileIS = new FileInputStream("filesTypes.xml");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        xml = builder.parse(fileIS);
        initMappedXMLDoc();
        result = new TreeMap<>();
        selection = new ArrayList<>();
    }

    /**
     * Analyse all the files selected. For each file, test every signature from the XML document until one match.
     * @return An analysing result.
     */
    public Result analyse(){
        int invalid = 0;
        int known = 0;
        ProgressBar pb = new ProgressBar(0, selection.size(), "Files", "Analysing files...");
        for(File f : selection){
            String insideExtension = null;
            try {
                insideExtension = retrieveFileExtension(f);
            } catch (XPathExpressionException e) {
                System.out.println("Analysing problem from XPath...");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Can't access this file");
            }
            if(insideExtension != null) {
                known++;
                result.put(f.getAbsolutePath(), insideExtension);
                if (!insideExtension.equalsIgnoreCase(Utils.fileCurrentExtension(f))) {
                    invalid++;
                }
            }
            pb.stepOver(1);
        }
        return new AnalyseResult(selection.size(), known, invalid);
    }

    /**
     * add one file to the selection.
     * @param filePath The pathname of this file.
     * @return The number of selected files.
     */
    public Result selectFile(String filePath){
        File f = new File(filePath);
        if(f.isFile()) {
            selection.add(f);
        }
        return new SelectResult(selection.size());
    }

    /**
     * Add all files from a directory to the selection.
     * @param directoryPath The pathname of a directory.
     * @return The number of selected files.
     */
    public Result selectDirectory(String directoryPath){
        File dir = new File(directoryPath);
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            if(files != null) {
                selection.addAll(List.of(files));
            }
        }
        return new SelectResult(selection.size());
    }

    /**
     * Clear the selection.
     * @return The number of selected files.
     */
    public Result clearSelection(){
        selection = new ArrayList<>();
        return new SelectResult(0);
    }

    /**
     *
     * @param file The file to be analysed
     * @return The file extension of this file. null if unknown.
     */
    private String retrieveFileExtension(File file) throws IOException, XPathExpressionException {
        FileInputStream reader = new FileInputStream(file);
        byte[] bFile = new byte[(int) file.length()];
        reader.read(bFile);
        reader.close();
        return searchIntoXML(bFile);
    }

    /**
     * Search the signature of the file from the XML database
     * @param hex The file bytes.
     * @return The extension commonly used for this signature.
     */
    public String searchIntoXML(byte[] hex){
        for(Head head : mappedXML.keySet()){
            if(Utils.retrieveHex(hex, head.hex, head.offset)){
                return mappedXML.get(head);
            }
        }
        return null;
    }

    /**
     * Initialize the treeMap to store values from xml document.
     */
    public void initMappedXMLDoc() throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "//type";
        NodeList types = (NodeList) xpath.compile(expression).evaluate(xml, XPathConstants.NODESET);
        mappedXML = new TreeMap<>();
        for(int i = 0; i< types.getLength(); i++){
            String hex = null, offset = null, extension = null;
            NodeList children = types.item(i).getChildNodes();
            for(int j = 0; j < children.getLength(); j++){
                Node child = children.item(j);
                if(child.getNodeName().equalsIgnoreCase("hex")){
                    hex = child.getTextContent();
                }
                if(child.getNodeName().equalsIgnoreCase("offset")){
                    offset = child.getTextContent();
                    if(offset.equalsIgnoreCase("any") || offset.equals("")){
                        offset = "-1";
                    }
                }
                if(child.getNodeName().equalsIgnoreCase("extension")){
                    extension = child.getTextContent();
                }
            }
            if(hex != null && offset != null && extension != null)
                mappedXML.put(new Head(hex, Integer.decode(offset)), extension.toLowerCase());
        }
    }

    /**
     * Rename all selected files with known extension when analysed by replacing their actual extension to their real extension.
     * @return The result containing number of files renamed.
     */
    public Result applyExtensions(){
        int success = 0;
        if(result.isEmpty()){
            System.out.println("Analyse the directory first");
            return new ApplyResult(0, success);
        }
        ProgressBar pb = new ProgressBar(0, result.size(),  "Files", "Renaming files...");
        for(File f : selection){
            String ext = result.get(f.getAbsolutePath());
            if(changeExtension(f, ext)) success++;
            pb.stepOver(1);
        }
        result = new TreeMap<>();
        clearSelection();
        return new ApplyResult(selection.size(), success);
    }

    /**
     * Change the extension of a file
     * @param f a file.
     * @param ext The extension to set.
     * @return true if the extension of this file has been changed.
     */
    private boolean changeExtension(File f, String ext){
        if(ext != null){
            String ext2 = Utils.fileCurrentExtension(f);
            if(!ext2.equals("")){
                return f.renameTo(new File(f.getParentFile().getAbsolutePath()+"/"+f.getName().replace(ext2, ext)));
            }
            else {
                return f.renameTo(new File(f.getParentFile().getAbsolutePath()+"/"+f.getName()+ "."+ext));
            }
        }
        return false;
    }
}

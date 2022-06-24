package fr.ollprogram.extensionretriever;

import java.io.File;
import java.util.HexFormat;

public class Utils {
    /**
     *
     * @param hexFile The hex representation of the file data.
     * @param signatureHex The hex of the signature retrieved in the XML file.
     * @return True if the file contains the signature at any offset.
     */
    public static boolean retrieveHexAnyOffset(String hexFile, String signatureHex){
        for(int i = 0, j = 0; i< hexFile.length(); i++){
            if(signatureHex.charAt(j) == ' ') i--;
            if(hexFile.charAt(i) == signatureHex.charAt(j) || signatureHex.charAt(j) == 'n'){
                j++;
            }
            else{
                j = 0;
            }
            if(j == signatureHex.length()-1)return true;
        }
        return false;
    }

    /**
     *
     * @param file A file.
     * @return The current extension of the file.
     */
    public static String fileCurrentExtension(File file){
        String name = file.getName();
        StringBuilder ext = new StringBuilder();
        boolean hasExtension = false;
        for(int i = 0; i < name.length(); i++){
            if(hasExtension){
                ext.append(name.charAt(i));
            }
            else if(name.charAt(i) == '.'){
                hasExtension = true;
            }
        }
        return ext.toString();
    }

    /**
     *
     * @param bytes The byte from the data of a file.
     * @param signatureHex A file type signature.
     * @param offset The offset commonly used for this file type. -1 if offset is any.
     * @return True if the file has the file type signature at the specified offset.
     */
    public static boolean retrieveHex(byte[] bytes, String signatureHex, int offset){
        signatureHex = signatureHex.toLowerCase().replace(" ", "");
        String fileHex = HexFormat.of().formatHex(bytes).toLowerCase();
        if(signatureHex.length()/2 > bytes.length) return false;
        if(offset == -1) return retrieveHexAnyOffset(fileHex, signatureHex);
        for(int i = offset/4, j = 0; j < signatureHex.length(); i++, j++){
            if(signatureHex.charAt(j) != 'n') {
                if (signatureHex.charAt(j) != fileHex.charAt(i)) return false;
            }
        }
        return true;
    }
}

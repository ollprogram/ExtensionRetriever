import fr.ollprogram.extensionretriever.Utils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class Comparison {
    @Test
    public void comp1(){
        byte[] bytes = {0x3c, 0x21};
        assertTrue(Utils.retrieveHex(bytes, "3c21", 0));
    }
    @Test
    public void comp2(){
        byte[] bytes = {0x57, 0x12};
        assertTrue(Utils.retrieveHex(bytes, "57", 0));
    }

    @Test
    public void comp3(){
        byte[] bytes = {0x57, 0x12, 0x32};
        assertTrue(Utils.retrieveHex(bytes, "57 nn 32", 0));
    }
}

import org.ubg.GraphManager;
import org.ubg.builder.connection.exception.UException;

import java.io.IOException;

/**
 * Created by dincaus on 2/2/17.
 */
public class main {

    public static void main(String[] chars) {
        try {

            GraphManager gm = new GraphManager();
            // arangoInstance.createDatabase("test1");
            // arangoInstance.createCollection("test2", "testCollection");

            gm.processCategory("1234", "Top");
            gm.processCategory("1234", "Top/Business");
            gm.processCategory("1234", "Top/Business/Automotive");
            System.out.println("");
        } catch(UException uexception) {
            System.out.println(uexception.getMessage());
        } catch(IOException ioexcp) {

        }
    }

}

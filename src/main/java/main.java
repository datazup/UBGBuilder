import org.ubg.org.ubg.graph.GraphManager;
import org.ubg.builder.connection.exception.UException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dincaus on 2/2/17.
 */
public class main {

    public static void main(String[] chars) {
        try {

            GraphManager gm = new GraphManager("localhost");
            // arangoInstance.createDatabase("test1");
            // arangoInstance.createCollection("test2", "testCollection");

            gm.processCategory("1234", "Top");
            gm.processCategory("1234", "Top/Business");
            gm.processCategory("1234", "Top/Business/Automotive");
            gm.processCategory("1234", "Top/Computer/Languages");

            gm.processCategory("12345", "Top");
            gm.processCategory("12345", "Top/Business");
            gm.processCategory("12345", "Top/Business/Automotive");

            Collection<String> testCategory = new ArrayList<>();
            testCategory.add("Top/Arts");
            testCategory.add("Top/News");
            gm.processCategory("123456", testCategory);

            System.out.println("");
        } catch(UException uexception) {
            System.out.println(uexception.getMessage());
        } catch(IOException ioexcp) {

        }
    }

}

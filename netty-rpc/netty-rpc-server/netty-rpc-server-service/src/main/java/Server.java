import com.osiris.netty.rpc.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author lijia
 */
public class Server {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(SpringConfig.class).start();
    }

}

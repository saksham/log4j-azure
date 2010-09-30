import org.apache.log4j.Logger;

public class LogExample {

    public LogExample() {
    }
    
    static Logger log = Logger.getLogger(LogExample.class);

    public static void main(String argsp[]) {

        log.debug("Here is some DEBUG");
        log.info("Here is some INFO");
        log.warn("Here is some WARN");
        log.error("Here is some ERROR");
        log.fatal("Here is some FATAL");
    }
} 
package example.steps;

import org.apache.log4j.Logger;

public class Steps {

    private final Logger logger = Logger.getLogger(getClass());
    
    protected Logger logger() {
        return this.logger;
    }
    
}

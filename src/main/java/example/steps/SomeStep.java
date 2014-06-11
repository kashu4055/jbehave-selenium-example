package example.steps;

import org.jbehave.core.annotations.When;

public final class SomeStep extends Steps {
    
    @When("ich etwas tue")
    public void iDoSomething() throws InterruptedException {
        Thread.sleep(3000);
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

}

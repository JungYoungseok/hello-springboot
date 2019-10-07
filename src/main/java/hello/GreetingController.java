package hello;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class GreetingController {

	private static final Logger logger = LogManager.getLogger(GreetingController.class);
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping("/sleep")
    public void sleep(@RequestParam(value="sleeptime", defaultValue="1000") String sleeptime) {
	System.out.println("We will pause this for " + sleeptime + " millisec"); 
	int milliseconds = Integer.parseInt(sleeptime);
        try{
    		Thread.sleep(milliseconds);
	}catch(InterruptedException ex){
    		Thread.currentThread().interrupt();
	}

        return;
    }

    @RequestMapping("/sum")
    public String sum(@RequestParam(value="num", defaultValue="1000") String num) throws IOException {
        System.out.println("We will sum from 1 to " + num );
        long num_to_sum = Long.parseLong(num);

        long sum = 0;
        for(long i = 1; i <= num_to_sum; ++i)
        {
            sum += i;
        }
        System.out.println("Sum = " + sum +" " + System.getProperty("log4j.configurationFile"));
        logger.debug("Sum = " + sum + " " + System.getProperty("log4j.configurationFile"));
        LambdaGateway.sendResult("https://1ynnpnccud.execute-api.ap-northeast-2.amazonaws.com/vpcLinkId/index.html");
        
        return Long.toString(sum);
    }


}

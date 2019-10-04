package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping("/sleep")
    public void sleep(@RequestParam(value="sleeptime", defaultValue="1000") String sleeptime) {
	System.out.println("We will pause this for" + sleeptime + " millisec"); 
	int milliseconds = Integer.parseInt(sleeptime);
        try{
    		Thread.sleep(milliseconds);
	}catch(InterruptedException ex){
    		Thread.currentThread().interrupt();
	}

        return;
    }

    @RequestMapping("/sum")
    public String sum(@RequestParam(value="num", defaultValue="1000") String num) {
        System.out.println("We will sum from 1 to " + num );
        long num_to_sum = Long.parseLong(num);

        long sum = 0;
        for(long i = 1; i <= num_to_sum; ++i)
        {
            sum += i;
        }
        System.out.println("Sum = " + sum);

        return Long.toString(sum);
    }


}

package hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.Instant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import datadog.opentracing.DDTracer;
import datadog.trace.api.DDTags;
import datadog.trace.bootstrap.instrumentation.api.AgentTracer.SpanBuilder;
import io.opentracing.Scope;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.rabbitmq.RabbitMqSendTracingHelper;



@RestController
public class GreetingController {

	private static final Logger logger = LogManager.getLogger(GreetingController.class);
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @Autowired
    RabbitTemplate rabbitTemplate;

        
//    @Autowired
//    Tracer tracer;
    
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping("/sleep")
    public void sleep(@RequestParam(value="sleeptime", defaultValue="1000") String sleeptime) {
   	logger.info("We will pause this for " + sleeptime + " millisec"); 
	int milliseconds = Integer.parseInt(sleeptime);
        try{
    		Thread.sleep(milliseconds);
	}catch(InterruptedException ex){
    		Thread.currentThread().interrupt();
	}

        return;
    }

    @RequestMapping("/largememory")
    public void largememory(@RequestParam(value="size", defaultValue="100000") String size) {
        logger.info("We will allocate " + size + " list");
        int arraySize = Integer.parseInt(size);

        ArrayList<Integer> arrayList = new ArrayList<>();

        for (int i = 0; i < arraySize; i++) {
            arrayList.add(i);
        }
        return;
    }

    @RequestMapping("/generatefileio")
    public void generatefileio(@RequestParam(value="level", defaultValue="20") String level) {
        logger.info("We will generate file io");
        int iteration_num = Integer.parseInt(level);
        for (int i = 0; i < iteration_num; i++) {
            FileIOGenerator.generateFileIO();
        }

        FileIOGenerator.deleteFile();
        return;
    }

    @RequestMapping("/highcpu")
    public float highcpu(@RequestParam(value="level", defaultValue="1") String level) {
        logger.info("We will consume cpu with " + level + " level");
        int weigh = Integer.parseInt(level);

        ArrayList<Integer> arrayList = new ArrayList<>();
        float result = 1;
        for (float x = 0; x < weigh * 100000; x++){
            for (float y = 0; y < 100000; y++) {
                result = result*x*y;
            }
        }
        return result;
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
        logger.info("Sum = " + sum + " " + System.getProperty("log4j.configurationFile"));
        LambdaGateway.sendResult("https://1ynnpnccud.execute-api.ap-northeast-2.amazonaws.com/vpcLinkId/index.html");
        crawlhttp();
        
        return Long.toString(sum);
    }

    @RequestMapping("/crawlhttp")
    public void crawlhttp() throws IOException {
        logger.debug("Crawling websites");
        LambdaGateway.sendGET("https://www.naver.com");
        LambdaGateway.sendGET("https://google.com");
        LambdaGateway.sendGET("https://daum.net");
        LambdaGateway.sendGET("https://1ynnpnccud.execute-api.ap-northeast-2.amazonaws.com/vpcLinkId/index.html");
    }

    @RequestMapping("/currenttime_in_mil")
    public long currenttime_in_mil() throws IOException {
        logger.debug("Getting currenttime in millisecond");

        return Instant.now().toEpochMilli();
    }
    
    @RequestMapping("/currenttime_in_sec")
    public long currenttime_in_sec() throws IOException {
        logger.debug("Getting currenttime in second");

        return Instant.now().getEpochSecond();
    }
    
    @RequestMapping("/add_two")
    public long add_two(@RequestParam(value="first", defaultValue="0") String first, @RequestParam(value="second", defaultValue="0") String second) {
        long first_param = Long.parseLong(first);
        long second_param = Long.parseLong(second);
        long sum = first_param + second_param;
        logger.info("first[" + first_param + "] + second[" + second_param + "] = sum[" + sum + "]..");
        return sum;
    }
  
    @RequestMapping("/test")
    public String index() throws InterruptedException {
        System.out.println("Sending message from controller...");
        //rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
//        ScopeManager sm = tracer.scopeManager();
//        Tracer.SpanBuilder tb = tracer.buildSpan("servlet.request");
//        Span span = tb.start();
//        try(Scope scope = sm.activate(span)) {
//            span.setTag(DDTags.SERVICE_NAME, "springrabbitmqprod");
//            span.setTag(DDTags.RESOURCE_NAME, "GET /test");
//            span.setTag(DDTags.SPAN_TYPE, "web");
//            publishToRabbitMQ(span.context().toTraceId());
//            //Thread.sleep(20);
//            span.finish();
//        }

        return "\ntest";
    }
    public void publishToRabbitMQ(String trace_id){
    	rabbitTemplate.convertAndSend(Application.topicExchangeName, "foo.bar.baz", "[" + trace_id + "]Hello from RabbitMQ!");
    
    	//rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
    }



    
}

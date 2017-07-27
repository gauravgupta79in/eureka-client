package hello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration 
@EnableEurekaClient
@SpringBootApplication
public class EurekaClientApplication {
	
	private @Autowired AutowireCapableBeanFactory beanFactory;

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }
}

@RefreshScope
@RestController
class ServiceInstanceRestController {

    private WhoAmI whoAmI;

    private DiscoveryClient discoveryClient;
    
    
    @Value("${message1:Hello default}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }
    
    @RequestMapping("/secure/message")
    String getMessageSecure() {
        return this.message;
    }
    
    @RequestMapping("/secure/booking")
    String bookTicket() {
        return this.message;
    }

    @Autowired
    public ServiceInstanceRestController(WhoAmI whoAmI, DiscoveryClient discoveryClient) {
        this.whoAmI = whoAmI;
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping("/")
    public String index() {
        return
                "<ul>" +
                   "<li><a href=\"/whoami\">whoami</a>" +
                   "<li><a href=\"/instances\">instances</a>" +
                "</ul>";
    }

    @RequestMapping("/instances")
    public List<ServiceInstance> clients() {
        return this.discoveryClient.getInstances(whoAmI.springApplicationName);
    }

    @RequestMapping("/whoami")
    public WhoAmI whoami() {
        return whoAmI;
    }
}

@Component
class WhoAmI {
    @Value("${spring.application.name}")
    public String springApplicationName;

    @Value("${server.port:8080}")
    public String serverPort;
}
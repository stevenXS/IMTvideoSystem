package vsst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import vsst.sevice.NettyServer;

@SpringBootApplication
@EnableDiscoveryClient
public class realTimeMonitoring9070 {
    public static void main(String[] args) {
        SpringApplication.run(realTimeMonitoring9070.class,args);
        try {
            new NettyServer(9080).start();
        }catch(Exception e) {
            System.out.println("NettyServerError:"+e.getMessage());
        }
    }
}

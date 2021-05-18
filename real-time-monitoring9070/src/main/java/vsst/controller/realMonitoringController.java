package vsst.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import vsst.commonResult.CommonResult;
import javax.annotation.Resource;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@RestController
@Slf4j
public class realMonitoringController {

    private static final String PLAY_URL = "http://python-flask-bobo";

    @Resource
    private RestTemplate restTemplate;
    @Value("server.port")
    private String serverPort;


    /**
    * @description: 获取服务器websocket的连接的ip和port
    * @param:
    * @return: vsst.commonResult.CommonResult
    * @author: Ziqiang Lee
    * @date: 2021/3/23
    */
    @GetMapping(value = "/realtime/getWs")
    public CommonResult getWebsocketPort(){
        Map<String,String> websocket = new HashMap<>();
        try {
            //获取当前服务器的ip地址，在linux环境下未测试过，需注意ip获取错误
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            websocket.put("ip", ip.getHostAddress());
                        }
                    }
                }
            }
        } catch (Exception e) {
           log.error("IP地址获取失败" + e.toString());
        }
        websocket.put("port","9080");
        return CommonResult.success(websocket);
    }

}

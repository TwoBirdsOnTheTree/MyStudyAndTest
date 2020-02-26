package com.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/zuul")
public class ZuulTestController {
    private Logger logger = LoggerFactory.getLogger(ZuulTestController.class);

    // 不带LoadBalanced注解的RestTemplate bean
    @Component
    @SuppressWarnings("InnerClassMayBeStatic")
    class RestTemplateWithNoLoadBalanced {
        // 使用Zuul ip地址访问， RestTemplate不能再，也不需要再使用@LoadBalanced注解了！加了反而出错
        private RestTemplate restTemplate = new RestTemplate();

        public <T> T getForObject(String url, Class<T> tClass) {
            return restTemplate.getForObject(url, tClass);
        }
    }

    // 支持Zuul的Feign Client
    // 注解value是zuul的服务id
    @FeignClient("zuul-server")
    interface ZuulFeignClient {
        // 对应的完整url是： http://zuul-server/service-b/hi
        @RequestMapping("/service-b/hi")
        String getHi();
    }

    @Autowired
    private RestTemplate restTemplate;// 带@LoadBalanced注解的RestTemplate
    @Autowired
    private RestTemplateWithNoLoadBalanced restTemplateWithNoLoadBalanced;// 不带@LoadBalanced注解的RestTemplate
    @Autowired
    private ZuulFeignClient zuulFeignClient;

    @RequestMapping("/manual-route-test")
    public String manualRouteTest() {
        // 通过ip地址访问zuul server
        String zuulServerUrl = "http://localhost:8861";
        // String forObject = restTemplate.getForObject(zuulServerUrl + "/service-b/hi", String.class);
        // 通过ip地址访问，restTemplate不能加@LoadBalanced注解了
        String forObject = restTemplateWithNoLoadBalanced.getForObject(zuulServerUrl + "/service-b/hi", String.class);
        System.out.println("forObject: " + forObject);
        return forObject;
    }

    @RequestMapping("/request_by_restTemplate_and_ip")
    public String request_by_restTemplate_and_ip() {
        // No instances available for blog.csdn.net
        // 也是失败，不能通过@LoadBalanced注解的restTemplate 请求非服务ID的URL
        return restTemplate.getForObject("https://blog.csdn.net/zhxdick/article/details/78560993", String.class);
    }

    @RequestMapping("/requestZuulByServiceIdNotIp")
    public String requestZuulByServiceIdNotIp() {
        // ok
        logger.info("EurekaServiceB发送请求：requestZuulByServiceIdNotIp");
        return restTemplate.getForObject("http://zuul-server/service-b/hi", String.class);
    }

    @RequestMapping("requestByFeignAndZuul")
    public String requestByFeignAndZuul() {
        // ok
        return zuulFeignClient.getHi();
    }
}
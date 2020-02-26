package com.test.zuul.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Configuration
public class ZuulTest {
    @Autowired
    private RestTemplate restTemplateWithLoadBalanced;
    @Autowired
    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "RedundantSuppression"})
    private RestTemplateWithNoooLoadBalanced restTemplateWithNoooLoadBalanced;
    @Value("${server.port}")
    private String port;

    @RequestMapping("/test")
    public String test() {
        String forObject = restTemplateWithLoadBalanced.getForObject("http://eureka-service-b/hi", String.class);
        System.out.println("测试结果：" + forObject);
        return "这里是Zuul Server";
    }

    @RequestMapping("/auto-route-test")
    public String autoRouteTest() {
        String zuulServerUrl = "http://localhost:" + port;
        System.out.println("zuulServerUrl: " + zuulServerUrl);
        // http://localhost:8861/eureka-service-b/hi
        return restTemplateWithNoooLoadBalanced.getForObject(zuulServerUrl + "/eureka-service-b/hi", String.class);
    }

    @RequestMapping("/manual-route-test")
    public String manualRouteTest() {
        String zuulServerUrl = "http://localhost:" + port;
        return restTemplateWithNoooLoadBalanced.getForObject(zuulServerUrl + "/service-b/hi", String.class);
    }

    @RequestMapping("/request-by-zuul-service-id-not-ip")
    public String request_by_zuul_service_id_not_ip() {
        // No instances available for zuul-server
        // 看来zuul sever本地不能通过服务id访问到, 只能通过ip地址访问到zuul server 。。。
        // 另外一个服务 {com.test.controller.ZuulTestController.requestZuulByServiceIdNotIp} 访问是成功的
        // 失败
        // 更新，添加另外一个服务的 ignored-services 的配置后，再测试这里反而OK了。。同时看/routes，发现自动多了一条/zuul-serve/**的路由
        // 半成功
        // 再更新。。 多刷新几遍，发现都OK了。。是Eureka都延迟导致都吗。。
        // OK
        return restTemplateWithLoadBalanced.getForObject("http://zuul-server/service-b/hi", String.class);
    }

    @RequestMapping("/test_url_route_url")
    public String test_url_route_url() {
        return restTemplateWithLoadBalanced.getForObject("http://zuul-server/test-url-route/hi", String.class);
    }

    @LoadBalanced
    @Bean("restTemplate")
    public RestTemplate returnRestTemplateThatSupportEureka() {
        return new RestTemplate();
    }

    @Component
    @SuppressWarnings("InnerClassMayBeStatic")
    class RestTemplateWithNoooLoadBalanced {
        // 通过IP地址访问zuul server，不能使用@LoadBalanced注解，因为使用这个注解后，会尝试从本地localhost查找服务
        // 使用Zuul ip地址访问， RestTemplate不能再，也不需要再使用@LoadBalanced注解了！加了反而出错
        private RestTemplate restTemplate = new RestTemplate();

        public <T> T getForObject(String url, Class<T> tClass) {
            return restTemplate.getForObject(url, tClass);
        }
    }
}

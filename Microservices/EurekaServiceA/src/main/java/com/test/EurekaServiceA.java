package com.test;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.test.com.test.util.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
// 启用Eureka客户端
@EnableEurekaClient

// 启动 DiscoveryClient
// @EnableDiscoveryClient

// 启动FeignClient
@EnableFeignClients

// 启用Hystrix断路器
@EnableCircuitBreaker
@DefaultProperties() // 默认配置

// 做为服务客户端测试
@RestController
public class EurekaServiceA {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceA.class, args);
    }

    private Logger logger = LoggerFactory.getLogger(EurekaServiceA.class);
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "RedundantSuppression"})
    @Autowired
    private ServiceBsFeignClient serviceBsFeignClient;

    @RequestMapping("/hi")
    public String sayHi() {
        return "这个是EurekaServiceA服务！";
    }

    /**
     * 测试 ThreadLocal传递
     */
    @RequestMapping("test_thread_local")
    public String test_thread_local() {
        UserContextHolder.context.get().setUserId("set in thread");
        System.out.println("currentThread: " + Thread.currentThread().getId());
        return getByHystrixCommand();
    }

    //TODO 已经注解@HystrixCommand，为啥是一样的。。。不知道啊
    @HystrixCommand
    public String getByHystrixCommand() {
        System.out.println("currentThread: " + Thread.currentThread().getId());
        return UserContextHolder.context.get().getUserId();
    }

    /**
     * 测试 Feign 超时
     */
    @RequestMapping("test_timeout_by_feign_client")
    public String test_timeout() {
        // FeignClient会超时，实际上，feign client集成了Hystrix
        return serviceBsFeignClient.testTimeout();
    }

    /**
     * 测试 Rest 超时
     * 测试 HystrixCommand 后备/船舱/超时
     */
    @HystrixCommand(
            fallbackMethod = "hystrixCommandFallbackMethod",
            threadPoolKey = "hystrix-thread-pool-key",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            }
    )
    @RequestMapping("test_timeout_by_rest")
    public String test_timeout_by_rest() {
        // 测试默认情况下，停工RestTemplate不会超时
        return restTemplate.getForObject("http://eureka-service-b/test_timeout", String.class);
    }

    // 后备模式方法
    private String hystrixCommandFallbackMethod() {
        return "Hystrix后备模式返回结果：HystrixCommandFallbackMethod";
    }

    /**
     * 测试 Feign
     */
    @RequestMapping("test_feign_client")
    public String test_feign_client() {
        return serviceBsFeignClient.getServiceBsHi("aaa", "bbb");
    }

    /**
     * 测试 Discovery Client
     */
    @RequestMapping("test_discovery_client")
    public String hi() {
        List<ServiceInstance> instances = discoveryClient.getInstances("eureka-service-b");

        return instances.stream()
                .map(i -> {
                    return i.getHost() + "," + i.getPort() + "," + i.getMetadata();
                }).collect(Collectors.joining());
    }

    /**
     * 测试 Rest
     */
    @RequestMapping("test_rest_template")
    public String hi2() {
        // 请求url抽象为：http://应用名/hi，
        // 而不是：http://ip地址/hi
        String forObject = restTemplate.getForObject("http://eureka-service-b/hi", String.class);
        return forObject;
    }

    // 使用注解LoadBalance，生成RestTemplate bean
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 拦截器
        restTemplate.setInterceptors(Collections.singletonList((httpRequest, body, execution) -> {
            logger.info("RestTemplate拦截器：" + httpRequest.getURI());
            return execution.execute(httpRequest, body);
        }));
        return restTemplate;
    }

    // FeignClient fallback，需要生成bean (或者@Component注解下）
    @Bean
    public FeignFallbackClass createFeignFallbackClass() {
        return new FeignFallbackClass();
    }
}

/**
 * FeignClient接口
 */
// 通过接口，并注解FeignClient
// SpringCloud将动态生成代理，用于调用目标Rest服务
@FeignClient(name = "eureka-service-b", fallback = FeignFallbackClass.class)
interface ServiceBsFeignClient {

    // 对应一个Rest请求
    @RequestMapping(
            value = "/hi?testParam={testParam}"
    )
    String getServiceBsHi(@PathVariable("testParam") String testParam, @RequestBody String body);

    // @HystrixCommand // 不能在FeignClient里使用@HystrixCommand了，那Feign应该如何控制Hystrix呢？
    @RequestMapping("/test_timeout")
    String testTimeout();
}

/**
 * FeignClient fallback类，实现FeignClient接口
 */
class FeignFallbackClass implements ServiceBsFeignClient {

    @Override
    public String getServiceBsHi(String testParam, String body) {
        return "FeignFallbackClass getServiceBsHi";
    }

    @Override
    public String testTimeout() {
        return "FeignFallbackClass testTimeout";
    }
}

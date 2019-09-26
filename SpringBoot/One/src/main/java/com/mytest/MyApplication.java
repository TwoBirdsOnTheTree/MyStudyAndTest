package com.mytest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytest.test.AcmPropertiesTest;
import com.mytest.test.OtherBean;
import com.mytest.test.ThirdBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@SpringBootApplication
@ServletComponentScan
public class MyApplication {

    @RequestMapping("/hi")
    String home() {
        return "HelloWorld";
    }

    public static void main(String[] args) throws Exception {

        SpringApplication springApplication = new SpringApplication(MyApplication.class);

        // 代码生成Banner
        /*springApplication.setBanner(new Banner() {
            @Override
            public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
                out.println("Hello Set Banner By Programmatically");
            }
        });*/

        // Listener
        springApplication.addListeners(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent applicationEvent) {
                // System.out.println("      applicationEvent: " + applicationEvent.toString());
            }
        });

        // 两种方式运行
        ConfigurableApplicationContext context = springApplication.run(args);
        // context = SpringApplication.run(Application.class, args);
        System.out.println("SpringBoot applicationContext created");

        // 测试
        // test(context);
    }

    /**
     * Spring boot 添加Servlet
     * @return 返回结果是一个ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        Servlet servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                // 目前打印中文时异常
                resp.getOutputStream().print("Spring Boot add Servlet, not support chinese character now");
                // super.doGet(req, resp);
            }
        };

        // 返回结果是ServletRegistrationBean
        // 需要对这个Bean设置下
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        // 设置添加的Servlet
        servletRegistrationBean.setServlet(servlet);
        // 设置Servlet匹配的Url, 不设置的话, 所有的请求都会被这个Servlet处理
        servletRegistrationBean.setUrlMappings(Arrays.asList("/myServlet"));
        return servletRegistrationBean;
    }

    /*测试方法*/
    private static void test(ApplicationContext context) throws Exception {
        OtherBean bean = context.getBean(OtherBean.class);
        ThirdBean bean1 = context.getBean(ThirdBean.class);

        System.out.println("bean.bean: " + bean.bean);
        System.out.println("bean.bean == bean1.bean ? " + (bean.bean == bean1.bean));

        AcmPropertiesTest test = context.getBean(AcmPropertiesTest.class);
        System.out.println("ConfigurationProperties acm: "
            + new ObjectMapper().writeValueAsString(test));

        @SuppressWarnings("unchecked")
        Map<String, String> map = context.getBean("GetMapBean", Map.class);
        System.out.println("ConfigurationProperties to map: "
            + map);

        @SuppressWarnings("unchecked")
        List<Map<String, String>> list = context.getBean("MergeTest", List.class);
        System.out.println("Merging Complex Types list: "
            + list);

        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> mergingMap = context.getBean("MergeMapTest", Map.class);
        System.out.println("Merging Complex Types map: "
            + mergingMap);
    }

    @ConfigurationProperties(prefix = "acm.security")
    @Bean(name = "GetMapBean")
    public Map<String, String> getMap() {
        return new HashMap<>();
    }

    @ConfigurationProperties("merge-list-test")
    @Bean(name = "MergeTest")
    public List<Map<String, String>> getMergeTestList() {
        return new ArrayList<>();
    }

    @ConfigurationProperties("merge-map-test")
    @Bean(name = "MergeMapTest")
    public Map<String, Map<String, String>> getMergeMapTest() {
        return new HashMap<>();
    }

    // 异常处理
    /*@Bean
    public ErrorController errorController() {
        return () -> {
            return "/test";
        };
    }*/
}
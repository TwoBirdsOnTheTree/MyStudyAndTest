package com.mytest.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 在任意请求方法执行前执行
     */
    @ModelAttribute("parameter")
    public String getModelAttributeParamter(
            @RequestParam(required = false) String parameter,
            Model model) {
        System.out.println("@ModelAttribute请求参数的parameter: " + parameter);
        model.addAttribute("setInModelAttribute", "ddd");
        return "returnFromModelAttribute";
    }

    /**
     * '@ModelAttribute'注解参数后, 参数就会从同名的ModelAttribute中获取(而不是Request中了)
     */
    @RequestMapping("/modelAttributeTest")
    public String test(
            @ModelAttribute("parameter") String parameterXX,
            HttpServletRequest request,
            Model model) {
        System.out.println("从request获取到的parameter: " + request.getParameter("parameter"));
        System.out.println("从@ModelAttribute获取到的parameter: " + parameterXX);
        model.addAttribute("parameter", null == parameterXX ? "null" : parameterXX);
        return model.toString();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<String> getPathVariable(@PathVariable String id) {
        System.out.println("获取到的PathVariable(请求路径变量)是: " + id);
        if ("404".equals(id)) {
            return new ResponseEntity<String>(id + "_xxxx", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(id + "_dddd", HttpStatus.OK);
    }

    @RequestMapping("restTemplate")
    public void restTemplateTest(String type) {
        type = null == type ? "getForEntity" : type;
        String requestUrl = "http://localhost:8888/hi";
        Map<String, String> param = new HashMap<String, String>() {{
            put("param", "request param");
        }};
        class RequestBody {
            private String one = "Put Request Body";

            public String getOne() {
                return one;
            }

            public void setOne(String one) {
                this.one = one;
            }
    }

        switch (type) {
            case "getForEntity":
                @SuppressWarnings("rawtypes")
                ResponseEntity<List> forEntity =
                        restTemplate.getForEntity(requestUrl, List.class);
                Object body = forEntity.getBody();
                System.out.println("forEntity: " + body + ", body类型是: " + body.getClass().getName());
                break;
            case "getForObject":
                String forObject = restTemplate.getForObject(requestUrl, String.class);
                System.out.println("getForObject: " + forObject);
                break;
            case "put":
                Map<String, String> map = new HashMap<String, String>() {{
                    put("one", "Put Request Body");
                }};

                // restTemplate.put(URI.create(requestUrl), map); // map是可以的, String不行
                restTemplate.put(requestUrl + "?param={param}", new RequestBody(), param);
                break;
            case "postForObject":
                String s = restTemplate.postForObject(requestUrl + "?param={param}", new RequestBody(), String.class, param);
                System.out.println("postForObject: " + s);
                break;
        }
    }
}

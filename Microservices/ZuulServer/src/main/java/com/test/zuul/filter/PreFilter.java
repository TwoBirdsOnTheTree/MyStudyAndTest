package com.test.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PreFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(PreFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        logger.info("前置过滤器：请求的url：" + request.getRequestURL());
        // 添加参数试试
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.containsKey("testParam")) {
            // 这种方式添加参数并不能成功！
            // parameterMap.put("testParam", new String[]{"zuul前置过滤器添加参数成功"});
            // 这种方式添加参数成功了
            context.setRequestQueryParams(new HashMap<String, List<String>>() {{
                put("testParam", Collections.singletonList("zuul前置过滤器添加参数成功"));
            }});
        }
        // logger.info("parameterMap: " + parameterMap);

        return null;
    }
}

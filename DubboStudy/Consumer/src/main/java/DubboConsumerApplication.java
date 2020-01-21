import com.test.dubbo.beans.NotifyImpl;
import com.test.dubbo.service.*;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.EchoService;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DubboConsumerApplication {
    public static void main(String[] args) throws IOException {
        String xmlPath = "application.xml";
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(xmlPath);

        context.start();

        System.out.println("-------------通过Dubbo调用Service-------------");

        // 隐式传参
        RpcContext.getContext().setAttachment("atta", "RpcContext.getContext().setAttachment");

        // 测试
        DemoService demoService = context.getBean("demoService", DemoService.class);
        String get = demoService.sayHello();
        System.out.println("1. 接收到Provider: " + get);

        // 泛化引用 (消费端reference使用generic="true")
        // 新版本Dubbo不行了??? 不是不行了, 是GenericService路径变化了, 需要重新引入apache路径下的
        GenericService genericService = context.getBean("demoService1", GenericService.class);
        Object genericResult = genericService.$invoke("testMethod",
                new String[]{"java.lang.String"}, new Object[]{"invoke方法参数"});
        System.out.println("2. 泛化引用, 返回结果: " + genericResult);

        // 泛化实现 (提供端实现GenericService接口)
        DemoService1 service1 = context.getBean("demoService2", DemoService1.class);
        service1.testMethod("泛化实现, 方法参数");

        // echo service
        // 任意一个Bean, 直接强转为EchoService
        EchoService echoService = (EchoService) context.getBean("demoService");
        Object ok = echoService.$echo("OK??");
        System.out.println("3. EchoService.$echo: " + ok);

        // 异步, 这里通过的是返回结构是ConpletableFuture实现的
        // 这种方式, 不需要指定xml async=true
        AsyncService asyncService = context.getBean("asyncService", AsyncService.class);
        CompletableFuture<String> future = asyncService.asyncMethod();
        future.whenComplete((providerResult, throwable) -> {
            System.out.println("4. consumer接收到provider的异步结果: " + providerResult);
        });
        System.out.println("5. 验证异步");

        // 参数回调: provider回调consumer的代码
        CallbackService callbackService = context.getBean("callbackService", CallbackService.class);
        callbackService.addListener("key", msg -> {
            System.out.println("6. callbackService, msg: " + msg);
        });

        // 事件通知
        //TODO 没有执行notifyImpl.onreturn, 为啥..算了
        DemoService notifyTestBean = context.getBean("notifyTestBean", DemoService.class);
        NotifyImpl notifyImpl = context.getBean("notifyImpl", NotifyImpl.class);
        String asyncReturn = notifyTestBean.sayHello();
        System.out.println("7. asyncReturn: " + asyncReturn);

        // provider端的本地存根, stub在dubbo.reference
        // 对应的还有consumer的本地存根
        // 先执行stub (LocalStubConsumerImpl的sayHello), 再手动调用dobbo(可选)
        LocalStubService localStubService = context.getBean("localStubConsumerService", LocalStubService.class);
        String localStubReturn = localStubService.sayHello("local stub参数");
        System.out.println("8. localStubReturn: " + localStubReturn);

        SYSTEM_EXIT();
        System.in.read();
    }

    private static void SYSTEM_EXIT() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            System.out.println("-------------系统退出-------------");
            System.exit(0);
        }, 15, TimeUnit.SECONDS);
        executor.shutdown();
    }
}

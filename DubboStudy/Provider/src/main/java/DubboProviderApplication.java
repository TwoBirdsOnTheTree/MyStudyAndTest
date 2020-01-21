import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class DubboProviderApplication {
    public static void main(String[] args) throws IOException {
        String xmlPath = "application.xml";
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(xmlPath);

        context.start();
        System.out.println("Dubbo Provider启动成功");
        System.in.read();
    }
}

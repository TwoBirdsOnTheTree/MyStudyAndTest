package com.mytest.proxy;

import com.alibaba.fastjson.JSON;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理
 */
public class ProxyTest {
    public static void main(String[] args) {
        ProxyTest t = new ProxyTest();
        // t.test1();
        t.test3();
    }

    //
    void test3() {
        class TargetObject {
            public String getStr(String str) {
                return str;
            }
        }
        TargetObject target = new TargetObject() {
            public String getStr(String str) {
                return "Hello CgLib";
            }
        };

        TargetObject proxy = (TargetObject) getProxyByCgLib(target);
        System.out.println( proxy.getStr("dd") );
    }

    // TODO 通过 CgLib: new Enhancer().e() + MethodInterceptor
    private Object getProxyByCgLib(Object target) {
        MethodInterceptor interceptor = new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
                System.out.println("class: " + method.getDeclaringClass().getName());
                System.out.println("method: " + method.getName());
                // return methodProxy.invokeSuper(target, arguments);
                return method.invoke(target, arguments);
            }
        };

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(interceptor);

        System.out.println("target: " + target.getClass().getEnclosingClass().getName());

        //TODO 如果是内部类, Enhancer.create需要指定外部类类型和实例
        Object proxy = enhancer.create(new Class[]{ProxyTest.class}, new Object[]{this});
        return proxy;
    }

    //TODO 通过Proxy.newProxyInstance + InvocationHandler
    void test1() {
        TargetInterface target = ProxyTest.target;

        TargetInterface proxy = (TargetInterface)

                Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{TargetInterface.class},
                        new InvocationHandler() {

                            private TargetInterface targetObject = target;

                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                System.out.println("\n");
                                System.out.println("proxy: " + JSON.toJSONString(proxy));
                                System.out.println("method: " + method.getName());
                                System.out.println("args: " + JSON.toJSONString(args));
                                return method.invoke(targetObject, args);
                            }
                        });

        System.out.println(proxy.getStr("sf"));
    }

    interface TargetInterface {
        String getStr(String str);
    }

    static TargetInterface target = new TargetInterface() {
        @Override
        public String getStr(String str) {
            return "吼吼, 我是target";
        }
    };

    void test2() {
        TargetInterface target = new TargetInterface() {
            public String getStr(String str) {
                return str;
            }
        };

        TargetInterface proxy = (TargetInterface)
                Proxy.newProxyInstance(
                        target.getClass().getClassLoader(),
                        new Class[]{TargetInterface.class},
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
                                System.out.println("调用: " + method.getName());
                                return method.invoke(target, arguments);
                            }
                        }
                );

        System.out.println(proxy.getStr("Hello Proxy"));
    }
}

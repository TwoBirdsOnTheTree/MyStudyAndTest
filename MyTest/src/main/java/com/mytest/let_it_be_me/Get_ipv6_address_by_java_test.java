package com.mytest.let_it_be_me;

import org.junit.jupiter.api.Test;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Supplier;

public class Get_ipv6_address_by_java_test {
    @Test
    void get_ipv6_address() throws SocketException {

        Supplier<List<String>> getIpv6Addresses = () -> {
            List<String> result = new ArrayList<>();
            try {
                Enumeration<NetworkInterface> networkInterfaces =
                        NetworkInterface.getNetworkInterfaces();

                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address instanceof Inet6Address) {
                            boolean isLocalAddress = address.isLinkLocalAddress();
                            boolean loopbackAddress = address.isLoopbackAddress();
                            if (!isLocalAddress && !loopbackAddress) {
                                result.add(address.getHostAddress());
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            return result;
        };

        getIpv6Addresses.get().forEach(i -> System.out.println(i));
    }
}

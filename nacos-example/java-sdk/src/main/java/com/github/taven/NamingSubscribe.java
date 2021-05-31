package com.github.taven;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class NamingSubscribe {
    public static void main(String[] args) throws NacosException, InterruptedException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", "localhost:8848");

        NamingService naming = NamingFactory.createNamingService(properties);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(() -> {
            try {
                naming.subscribe("sp-provider", new EventListener() {
                    @Override
                    public void onEvent(Event event) {
                        System.out.println(((NamingEvent)event).getServiceName());
                        System.out.println(((NamingEvent)event).getInstances());
                    }
                });
            } catch (NacosException e) {
                e.printStackTrace();
            }
        }).start();

        countDownLatch.await();
    }
}

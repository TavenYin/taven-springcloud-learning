package com.github.taven;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Properties;

@Component
public class NacosRegister {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Environment env;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup(ApplicationReadyEvent applicationReadyEvent) throws NacosException {
        int serverPort = Integer.parseInt(Objects.requireNonNull(env.getProperty("server.port")));

        String nacosAddr = env.getProperty("nacos.addr");
        String registerIp = env.getProperty("nacos.registerIp");
        String serviceName = env.getProperty("nacos.serviceName");

        Properties properties = new Properties();
        properties.setProperty("serverAddr", nacosAddr);
        NamingService naming = NamingFactory.createNamingService(properties);

        naming.registerInstance(serviceName, registerIp, serverPort, "TEST1");
//        naming.registerInstance(serviceName, registerIp, serverPort, "TEST2");

        log.info("*********** Nacos register success ***********");
    }

}

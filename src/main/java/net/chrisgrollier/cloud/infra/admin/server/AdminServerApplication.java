package net.chrisgrollier.cloud.infra.admin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

/**
 * Main class.
 * @author Christophe Grollier
 *
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
@RefreshScope
public class AdminServerApplication {
	
    /**
     * To launch this app.
     * 
     * @param args String[]
     */
    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }

}

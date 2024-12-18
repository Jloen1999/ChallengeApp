package es.uex.challengeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ChallengeappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChallengeappApplication.class, args);
    }

    @Component
    static
    class ServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        @Override
        public void customize(ConfigurableServletWebServerFactory factory) {
            String port = System.getenv("PORT");
            if (port != null) {
                factory.setPort(Integer.parseInt(port));
            }
        }
    }
}

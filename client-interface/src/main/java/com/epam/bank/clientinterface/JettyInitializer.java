package com.epam.bank.clientinterface;

import java.io.IOException;
import java.util.stream.Stream;
import javax.servlet.Servlet;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Slf4j
public class JettyInitializer {
    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH = "/";
    private static final String CONFIG_LOCATION = "com.epam.clientinterface";
    private static final String MAPPING_URL = "/*";
    private static final String DEFAULT_PROFILE = "default";

    public static void main(String[] args) throws Exception {
        new JettyInitializer().startJetty(args);
    }

    private static int getPortFromArgs(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException ignore) {
                return DEFAULT_PORT;
            }
        }
        return DEFAULT_PORT;
    }

    private void startJetty(String[] args) throws Exception {
        Log.setLog(new Slf4jLog());
        Server server = new Server(getPortFromArgs(args));
        server.setHandler(getServletContextHandler(getContext(args)));
        server.start();
        server.join();
    }

    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        Servlet servlet = new DispatcherServlet(context);
        contextHandler.addServlet(new ServletHolder(servlet), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
        contextHandler.setResourceBase(new ClassPathResource("/").getURI().toString());
        return contextHandler;
    }

    private static WebApplicationContext getContext(String[] args) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
        // TODO won't work with war in Tomcat
        Stream.of(args).filter(arg -> arg.startsWith("-Dspring.profiles.active"))
            .findFirst()
            .map(arg -> arg.replaceFirst("-Dspring\\.profiles\\.active=", ""))
            .ifPresent(profiles -> context.getEnvironment()
                .setActiveProfiles(profiles.replaceAll("\\s", "").split(",")));
        return context;
    }
}

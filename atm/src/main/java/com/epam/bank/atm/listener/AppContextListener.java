package com.epam.bank.atm.listener;

import com.epam.bank.atm.di.DiContainer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import liquibase.Liquibase;
import lombok.SneakyThrows;

public class AppContextListener implements ServletContextListener {
    private final Liquibase liquibase = DiContainer.instance().getSingleton(Liquibase.class);

    @SneakyThrows
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        this.liquibase.update("");
    }
}

package com.earandap.vehiculos.config;

import org.atmosphere.cpr.SessionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.support.ApplicationContextEventBroker;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Configuration
@EnableAspectJAutoProxy
public class EventConfiguration {

    @Autowired
    EventBus.ApplicationEventBus applicationEventBus;

    @Bean
    ApplicationContextEventBroker applicationContextEventBroker() {
        return new ApplicationContextEventBroker(applicationEventBus);
    }

    @Bean
    public SessionSupport atmosphereSessionSupport() {
        return new SessionSupport();
    }
}

package com.moksha.raspberrypi.server.dao;

import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiceInjector {

    private static final Logger logger = LoggerFactory.getLogger(GuiceInjector.class);
    private static Injector injector = null;

    public static void assignInjector(Injector newInjector) {
        if (injector == null) {
            synchronized (GuiceInjector.class) {
                logger.info("assigning injector for the first time");
                injector = newInjector;
            }
        } else {
            logger.warn("replacing old injector with this - be prepared for the consequences");
        }

    }

    public static Injector getInjector() {
        if (injector == null) {
            synchronized (GuiceInjector.class) {
                if (injector == null) {
                    logger.error("NO INJECTOR FOUND");
                    throw new IllegalStateException("Injector instance is not assigned. Please assign first");
                }
            }
        }
        return injector;
    }
}

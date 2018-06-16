package com.moksha.raspberrypi.server;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPiManage implements Managed {
    private static final Logger logger = LoggerFactory.getLogger(RPiManage.class);

    @Override
    public void start() throws Exception {
        logger.info("<<<<<<<<<<<<<<<<<<  Starting Raspberry Pi Server >>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void stop() throws Exception {
        logger.info("<<<<<<<<<<<<<<<<<< Gracefully Stopping Raspberry Pi Server >>>>>>>>>>>>>>>>>>>>>>");
    }
}

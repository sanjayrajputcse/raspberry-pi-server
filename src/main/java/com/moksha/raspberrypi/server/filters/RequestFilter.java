package com.moksha.raspberrypi.server.filters;

import com.google.inject.Inject;
import com.moksha.raspberrypi.server.dao.ApplicationDAO;
import com.moksha.raspberrypi.server.dao.DeviceDAO;
import com.moksha.raspberrypi.server.dao.HSession;
import com.moksha.raspberrypi.server.models.entities.Application;
import com.moksha.raspberrypi.server.models.entities.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Provider
@PreMatching
public class RequestFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Inject
    private ApplicationDAO applicationDAO;

    @Inject
    private DeviceDAO deviceDAO;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        logger.info("<<<<<<<<<< Got New Request >>>>>>>>>>>");
        MultivaluedMap<String, String> requestHeaders = containerRequestContext.getHeaders();
        HSession hSession = new HSession();
        hSession.open();
        requestHeaders.forEach((key, value) -> {
            if (key.equalsIgnoreCase("DEVICE_ID")) {
                Optional<Device> device = deviceDAO.getByName(value.get(0));
                if (device.isPresent()) {
                    containerRequestContext.setProperty("device", device.get());
                }
            } else if (key.equalsIgnoreCase("APP_ID")) {
                Optional<Application> app = applicationDAO.getByName(value.get(0));
                if (app.isPresent()) {
                    containerRequestContext.setProperty("application", app.get());
                }
            }
        });
        hSession.close();
    }
}

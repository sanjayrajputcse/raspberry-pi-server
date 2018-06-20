package com.moksha.raspberrypi.server.filters;

import com.google.inject.Inject;
import com.moksha.raspberrypi.server.dao.ApplicationDAO;
import com.moksha.raspberrypi.server.dao.DeviceDAO;
import com.moksha.raspberrypi.server.dao.HSession;
import com.moksha.raspberrypi.server.models.entities.Application;
import com.moksha.raspberrypi.server.models.entities.Device;
import com.moksha.raspberrypi.server.utils.TimeUtil;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = containerRequestContext.getEntityStream();
        ReaderWriter.writeTo(in, out);

        byte[] requestEntity = out.toByteArray();
        logger.info("Method : " + containerRequestContext.getMethod());
        logger.info("Url : " + containerRequestContext.getUriInfo().getAbsolutePath().toString());
        logger.info("Body : " + out);
        logger.info("Headers: " + containerRequestContext.getHeaders());

        HSession hSession = new HSession();
        hSession.open();
        requestHeaders.forEach((key, value) -> {
            if (key.equalsIgnoreCase("DEVICE_ID")) {
                Optional<Device> device = deviceDAO.getByName(value.get(0));
                if (device.isPresent()) {
                    containerRequestContext.setProperty("device", device.get());
                    device.get().setUpdatedOn(TimeUtil.now());
                    deviceDAO.update(device.get());
                }
            } else if (key.equalsIgnoreCase("APP_ID")) {
                Optional<Application> app = applicationDAO.getByName(value.get(0));
                if (app.isPresent()) {
                    containerRequestContext.setProperty("application", app.get());
                }
            }
        });
        hSession.close();
        containerRequestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
    }
}

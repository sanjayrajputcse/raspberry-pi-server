package com.moksha.raspberrypi.server.resources;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.moksha.raspberrypi.server.dao.DeviceDAO;
import com.moksha.raspberrypi.server.models.entities.Device;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v0/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource {

    @Inject
    private DeviceDAO deviceDAO;

    @GET
    @UnitOfWork
    public List<Device> getAll(@QueryParam("include") @DefaultValue("") String include) {
        List<String> includes = parse(include);
        List<Device> devices = deviceDAO.getAll(Device.class);
        devices.forEach(d -> {
            if (includes.contains("sensors") || includes.contains("all")) {
                d.getSensors().iterator();
            }
            if (includes.contains("specs") || includes.contains("specifications") || includes.contains("all")) {
                d.getSpecifications().iterator();
            }

        });
        return devices;
    }

    private List<String> parse(String include) {
        return Lists.newArrayList(include.split(","));
    }
}

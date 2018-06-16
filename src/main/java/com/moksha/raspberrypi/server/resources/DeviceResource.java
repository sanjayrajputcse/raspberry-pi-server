package com.moksha.raspberrypi.server.resources;

import com.google.inject.Inject;
import com.moksha.raspberrypi.server.dao.DeviceDAO;
import com.moksha.raspberrypi.server.models.entities.Device;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public List<Device> getAll() {
        return deviceDAO.getAll(Device.class);
    }
}

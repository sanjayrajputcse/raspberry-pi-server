package com.moksha.raspberrypi.server.resources;

import com.google.common.collect.Lists;

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

    @GET
    public List<String> getAll() {
        return Lists.newArrayList("1", "2", "3");
    }
}

package com.moksha.raspberrypi.server.resources;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.dao.ApplicationActionDAO;
import com.moksha.raspberrypi.server.dao.ApplicationDAO;
import com.moksha.raspberrypi.server.models.entities.Application;
import com.moksha.raspberrypi.server.models.entities.ApplicationAction;
import com.moksha.raspberrypi.server.utils.TimeUtil;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/v0/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApplicationResource {

    @Inject
    private ApplicationDAO applicationDAO;
    @Inject
    private ApplicationActionDAO applicationActionDAO;

    @GET
    @UnitOfWork
    public List<Application> getAllApplication() {
        return applicationDAO.getAll(Application.class);
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Application getAllApplication(@PathParam("id") String applicationId) {
        return applicationDAO.get(applicationId);
    }

    @GET
    @UnitOfWork
    @Path("/actions")
    public List<ApplicationAction> getNewActions(@QueryParam("is_done") Boolean done,
                                                 @QueryParam("order_by") String orderBy,
                                                 @QueryParam("order_type") String orderType,
                                                 @QueryParam("limit") int limit,
                                                 @Context ContainerRequestContext crc) throws Exception {
        Application application = (Application) crc.getProperty("application");
        if (application == null) {
            throw new Exception("APP_ID not passed in header, or invalid");
        }
        return applicationActionDAO.getActions(application, done, orderBy, orderType, limit);
    }

    @POST
    @UnitOfWork
    @Path("/actions/create")
    public ApplicationAction createAction(ApplicationAction applicationAction,
                                       @Context ContainerRequestContext crc) throws Exception {
        Application application = (Application) crc.getProperty("application");
        if (application == null) {
            throw new Exception("APP_ID not passed in header, or invalid");
        }
        applicationAction.setApplication(application);
        applicationAction.setStatus("ADDED");
        return applicationActionDAO.create(applicationAction);
    }

    @PUT
    @UnitOfWork
    @Path("/actions/{id}/start")
    public ApplicationAction startAction(@PathParam("id") Long actionId) {
        ApplicationAction applicationAction = applicationActionDAO.get(actionId);
        applicationAction.setStatus("STARTED");
        applicationAction.setActionStartedAt(TimeUtil.now());
        applicationActionDAO.update(applicationAction);
        return applicationAction;
    }

    @PUT
    @UnitOfWork
    @Path("/actions/{id}/end")
    public ApplicationAction endAction(@PathParam("id") Long actionId) {
        ApplicationAction applicationAction = applicationActionDAO.get(actionId);
        applicationAction.setStatus("END");
        applicationAction.setActionEndedAt(TimeUtil.now());
        applicationAction.setDone(true);
        applicationActionDAO.update(applicationAction);
        return applicationAction;
    }
}

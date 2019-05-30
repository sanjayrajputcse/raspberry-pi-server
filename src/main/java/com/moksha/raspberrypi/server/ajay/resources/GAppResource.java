package com.moksha.raspberrypi.server.ajay.resources;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.ajay.dao.ListDetailDAO;
import com.moksha.raspberrypi.server.ajay.dao.UserActionDAO;
import com.moksha.raspberrypi.server.ajay.models.entities.Action;
import com.moksha.raspberrypi.server.ajay.models.entities.ListDetail;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * Created by ajay.agarwal on 31/05/19.
 */
@Path("/v0/gapp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GAppResource {

    @Inject
    private ListDetailDAO listDetailDAO;

    @Inject
    private UserActionDAO userActionDAO;

    @GET
    @UnitOfWork
    @Path("/doAction")
    public long doAction(@QueryParam("fk_account_id") String fkAccountId,
                         @QueryParam("action_name") String actionName,
                         @QueryParam("action_value") String actionValue,
                         @QueryParam("list_id") String listId,
                         @Context ContainerRequestContext crc) throws Exception {

        if (Action.getActionFromString(actionName) == Action.INVALID)
            return -1;

        UserAction userAction = new UserAction();
        userAction.setFkAccountId(fkAccountId);
        userAction.setActionName(actionName);
        userAction.setActionValue(actionValue);
        userAction.setListId(listId);
        return userActionDAO.create(userAction).getId();
    }

    @GET
    @UnitOfWork
    @Path("/showLists")
    public List<ListDetail> showLists(@QueryParam("fk_account_id") String fkAccountId,
                                      @Context ContainerRequestContext crc) throws Exception {

        return listDetailDAO.getAll(fkAccountId);
    }

    @GET
    @UnitOfWork
    @Path("/sendListToPN/")
    public long sendListToPN(@QueryParam("list_id") String listId, @QueryParam("fk_account_id") String fkAccountId,
                             @Context ContainerRequestContext crc) throws Exception {

        UserAction sendListToDeviceUserAction = new UserAction();
        sendListToDeviceUserAction.setFkAccountId(fkAccountId);
        sendListToDeviceUserAction.setActionName(Action.SEND_LIST_TO_PN.getValue());
        sendListToDeviceUserAction.setListId(listId);

        return userActionDAO.create(sendListToDeviceUserAction).getId();

    }

    @GET
    @UnitOfWork
    @Path("/isDone")
    public boolean isDone(@QueryParam("action_id") String actionId,
                          @Context ContainerRequestContext crc) throws Exception {

        UserAction userAction = userActionDAO.get(actionId);
        return userAction.isDone();
    }
}
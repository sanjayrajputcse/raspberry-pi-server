package com.moksha.raspberrypi.server.ajay.resources;

import com.google.inject.Inject;

import com.moksha.raspberrypi.server.ajay.dao.ListDetailDAO;
import com.moksha.raspberrypi.server.ajay.dao.UserActionDAO;
import com.moksha.raspberrypi.server.ajay.models.entities.Action;
import com.moksha.raspberrypi.server.ajay.models.entities.ActionRequest;
import com.moksha.raspberrypi.server.ajay.models.entities.ListDetail;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
    @Path("/actions/create")
    public long doAction(ActionRequest actionRequest,
                                     @Context ContainerRequestContext crc) throws Exception {
        UserAction userAction = new UserAction();
        userAction.setFkAccountId(actionRequest.getFkAccountId());
        userAction.setListId(actionRequest.getListName());
        userAction.setActionName(actionRequest.getActionName());
        userAction.setActionValue(actionRequest.getActionValue());

        switch (Action.getActionFromString(actionRequest.getActionName())) {
            case CREATE_LIST:
                userActionDAO.create(userAction);
                break;
            case REMOVE_LIST:
                userActionDAO.delete(userAction);
                break;
            case ADD_ITEM_TO_LIST:
            case REMOVE_ITEM_FROM_LIST:
                userActionDAO.update(userAction);
                break;
        }
        UserAction userAction1 = userActionDAO.create(userAction);

        return userAction1.getId();
    }

    @GET
    @UnitOfWork
    @Path("/lists")
    public List<ListDetail> getList(@QueryParam("list_id") String listId, @QueryParam("fk_account_id") String fkAccountId,
                                    @Context ContainerRequestContext crc) throws Exception {

        if (listId == null) {
            // send list of list names
            return listDetailDAO.getAll(fkAccountId);
        } else {
            ListDetail listDetail = listDetailDAO.get(listId);
            return Arrays.asList(listDetail);
        }
    }

    @GET
    @UnitOfWork
    @Path("/sendlists/")
    public int sendList(@QueryParam("list_id") String listId, @QueryParam("fk_account_id") String fkAccountId,
                        @Context ContainerRequestContext crc) throws Exception {

        if (listId == null) {
            // send list of list names
            List<ListDetail> all = listDetailDAO.getAll(fkAccountId);
            if (all == null || all.size() == 0) {
                return -1;
            }
            if (all.size() == 1) {
                sendListUserAction(all.get(0).getListId(), fkAccountId);
                return 1;
            }
            return -2;

        } else {
            ListDetail listDetail = listDetailDAO.get(listId);
            if (listDetail == null) {
                return -1;
            }
            sendListUserAction(listId, fkAccountId);
            return -2;
        }
    }

    private long sendListUserAction(String listId, String fkAccountId) {
        UserAction sendListToDeviceUserAction = new UserAction();
        sendListToDeviceUserAction.setActionName(Action.SEND_LIST.getValue());
        sendListToDeviceUserAction.setListId(listId);
        sendListToDeviceUserAction.setFkAccountId(fkAccountId);

        return userActionDAO.create(sendListToDeviceUserAction).getId();
    }

    @GET
    @UnitOfWork
    @Path("/isDone/")
    public boolean sendList(@QueryParam("action_id") String action_id,
                            @Context ContainerRequestContext crc) throws Exception {

        UserAction userAction = userActionDAO.get(action_id);
        return userAction.isDone();
    }
}
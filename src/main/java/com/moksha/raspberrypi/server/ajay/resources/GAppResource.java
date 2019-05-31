package com.moksha.raspberrypi.server.ajay.resources;

import com.google.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.moksha.raspberrypi.server.ajay.dao.ListDetailDAO;
import com.moksha.raspberrypi.server.ajay.dao.UserAccountDAO;
import com.moksha.raspberrypi.server.ajay.dao.UserActionDAO;
import com.moksha.raspberrypi.server.ajay.models.entities.Action;
import com.moksha.raspberrypi.server.ajay.models.entities.Item;
import com.moksha.raspberrypi.server.ajay.models.entities.ListDetail;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAccount;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;
import com.moksha.raspberrypi.server.utils.MyObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Inject
    private UserAccountDAO userAccountDAO;

    @GET
    @UnitOfWork
    @Path("/doAction")
    public long doAction(@QueryParam("fk_account_id") String fkAccountId,
                         @QueryParam("action_name") String actionName,
                         @QueryParam("action_value") String actionValue,
                         @QueryParam("list_id") String listId,
                         @Context ContainerRequestContext crc) throws Exception {

        Action action = Action.getActionFromString(actionName);
        if (action == Action.INVALID)
            return -1;
        long actionId = -1;
        switch (action) {
            case CREATE_LIST:
                UserAction userAction = new UserAction();
                userAction.setFkAccountId(fkAccountId);
                userAction.setActionName(actionName);
                userAction.setListId(listId);
                userActionDAO.create(userAction);
                actionId = userActionDAO.create(userAction).getId();

                ListDetail listDetail = new ListDetail();
                listDetail.setFkAccountId(fkAccountId);
                listDetail.setListId(listId);
                listDetail.setListItems(MyObjectMapper.getJsonString(Arrays.asList()));
                listDetailDAO.create(listDetail);
                break;

            case REMOVE_LIST:
                UserAction userAction4 = new UserAction();
                userAction4.setFkAccountId(fkAccountId);
                userAction4.setActionName(actionName);
                userAction4.setListId(listId);
                userActionDAO.create(userAction4);
                actionId = userActionDAO.create(userAction4).getId();

                ListDetail listDetail4 = listDetailDAO.get(listId);
                listDetailDAO.delete(listDetail4);
                break;

            case ADD_ITEM_TO_LIST:
                ListDetail listDetail2 = listDetailDAO.get(listId);
                List<Item> items = MyObjectMapper.getClassObject(listDetail2.getListItems(), new TypeReference<List<Item>>() {
                });
                items.add(new Item(actionValue, null, null));
                listDetail2.setListItems(MyObjectMapper.getJsonString(items));

                UserAction userAction2 = new UserAction();
                userAction2.setFkAccountId(fkAccountId);
                userAction2.setActionName(actionName);
                userAction2.setActionValue(actionValue);
                userAction2.setListId(listId);
                actionId = userActionDAO.create(userAction2).getId();
                break;

            case REMOVE_ITEM_FROM_LIST:
                ListDetail listDetail3 = listDetailDAO.get(listId);
                List<Item> items2 = MyObjectMapper.getClassObject(listDetail3.getListItems(), new TypeReference<List<Item>>() {
                });
                items2 = items2.stream()
                        .filter(item -> item.getProductTitle() == null || !item.getProductTitle().toLowerCase().contains(actionValue.toLowerCase()))
                        .collect(Collectors.toList());
                listDetail3.setListItems(MyObjectMapper.getJsonString(items2));

                UserAction userAction3 = new UserAction();
                userAction3.setFkAccountId(fkAccountId);
                userAction3.setActionName(actionName);
                userAction3.setActionValue(actionValue);
                userAction3.setListId(listId);
                actionId = userActionDAO.create(userAction3).getId();
                break;
        }
        return actionId;
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
    public boolean isDone(@QueryParam("action_id") long actionId,
                          @Context ContainerRequestContext crc) throws Exception {

        UserAction userAction = userActionDAO.get(actionId);
        return userAction.isDone();
    }

    @GET
    @UnitOfWork
    @Path("/getUserAccountId")
    public UserAccount getUserAccount() throws Exception {
        return userAccountDAO.getUserAccount();
    }


    @GET
    @UnitOfWork
    @Path("/getPendingTasks")
    public List<UserAction> getPendingTasks(@Context ContainerRequestContext crc) throws Exception {
        return userActionDAO.getPendingUserActions();
    }

    @GET
    @UnitOfWork
    @Path("/updateTask")
    public boolean updateTaskStatus(@QueryParam("action_id") long actionId,
                                    @QueryParam("talk_back_text") String talkBackText,
                                    @Context ContainerRequestContext crc) throws Exception {
        final String talkBackText2 = talkBackText.replaceAll("\"", "");
        UserAction userAction = userActionDAO.get(actionId);
        userAction.setDone(true);
        userAction.setTalkBackText(talkBackText2);

        ListDetail listDetail = listDetailDAO.get(userAction.getListId());
        List<Item> items = MyObjectMapper.getClassObject(listDetail.getListItems(), new TypeReference<List<Item>>() {});
        items.forEach(item -> {
            if (item.getQueryName().equalsIgnoreCase(userAction.getActionValue())) {
                item.setProductTitle(talkBackText2);
            }
        });
        listDetail.setListItems(MyObjectMapper.getJsonString(items));
        return true;
    }
}
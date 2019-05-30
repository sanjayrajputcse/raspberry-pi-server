package com.moksha.raspberrypi.server.ajay.models.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moksha.raspberrypi.server.models.entities.AbstractTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_actions")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAction extends AbstractTimeEntity {

    @Column(name = "id")
    private Long id;

    @Column(name = "fk_account_id")
    private String fkAccountId;

    @Column(name = "is_done")
    private boolean isDone;

    @Column(name = "action_name")
    private String actionName;

    @Column(name = "action_value")
    private String actionValue;

    @Column(name = "talk_back_text")
    private String talkBackText;
}

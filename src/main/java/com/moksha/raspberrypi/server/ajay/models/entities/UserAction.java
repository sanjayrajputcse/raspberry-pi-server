package com.moksha.raspberrypi.server.ajay.models.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moksha.raspberrypi.server.models.entities.AbstractTimeEntity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "user_actions")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAction extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fk_account_id")
    private String fkAccountId;

    @Column(name = "action_name")
    private String actionName;

    @Column(name = "action_value")
    private String actionValue;

    @Column(name = "list_id")
    private String listId;

    @Column(name = "is_done")
    private boolean isDone;

    @Column(name = "talk_back_text")
    private String talkBackText;
}

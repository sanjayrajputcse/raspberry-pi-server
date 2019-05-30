package com.moksha.raspberrypi.server.ajay.models.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActionRequest {


    private String fkAccountId;

    private String actionName;

    private String actionValue;

    private String listName;
}

package com.moksha.raspberrypi.server.models.entities.fvo.backend;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@Table(name = "active_accounts")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActiveAccounts {

    @Column(name = "fk_account_id")
    private String fkAccountId;

    @Column(name = "device_id")
    private String device_id;

}
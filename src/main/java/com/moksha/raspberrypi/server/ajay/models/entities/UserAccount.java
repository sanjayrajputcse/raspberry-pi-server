package com.moksha.raspberrypi.server.ajay.models.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moksha.raspberrypi.server.models.entities.AbstractTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "user_accounts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccount {

    @Id
    @Column(name = "fk_account_id")
    private String fkAccountId;

}

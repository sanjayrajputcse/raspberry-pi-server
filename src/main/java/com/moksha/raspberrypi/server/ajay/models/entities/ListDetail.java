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

import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "list_details")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ListDetail extends AbstractTimeEntity implements Serializable {

    @Id
    @Column(name = "list_id")
    private String listId;

    @Id
    @Column(name = "fk_account_id")
    private String fkAccountId;

    @Column(name = "list_items", columnDefinition = "TEXT DEFAULT NULL")
    private String listItems;
}

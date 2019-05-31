package com.moksha.raspberrypi.server.models.entities.fvo.backend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moksha.raspberrypi.server.models.entities.AbstractTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "materialized_collections")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MaterializedCollection extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fk_account_id")
    private String fkAccountId;

    @Column(name = "description")
    private String device_id;

    @Column(name = "list_name")
    private String listName;

    @Column(name = "url")
    private String url;

    @Column(name = "collection_id")
    private String collectionId;

}

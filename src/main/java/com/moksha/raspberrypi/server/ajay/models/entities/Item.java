package com.moksha.raspberrypi.server.ajay.models.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by ajay.agarwal on 31/05/19.
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Item {
    private String queryName;
    private String resolvedName;
    private String quantity;
}

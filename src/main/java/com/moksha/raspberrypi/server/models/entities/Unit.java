package com.moksha.raspberrypi.server.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "units")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Unit extends AbstractTimeEntity {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "unit")
    private String unit;

    @Column(name = "type")
    private String type;

    @Column(name = "notion")
    private String notion;
}

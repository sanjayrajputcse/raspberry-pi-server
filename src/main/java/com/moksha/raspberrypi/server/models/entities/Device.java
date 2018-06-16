package com.moksha.raspberrypi.server.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devices")
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Device extends AbstractTimeEntity {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "gen")
    private String generation;

    @Column(name = "model")
    private String model;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeviceSensor> sensors;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeviceSpecification> specifications;
}

package com.moksha.raspberrypi.server.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "gen")
    private String generation;

    @Column(name = "model")
    private String model;

    @Column(name = "bought_on")
    private Timestamp boughtOn;
}

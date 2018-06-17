package com.moksha.raspberrypi.server.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device_sensors")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceSensor extends AbstractTimeEntity {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private Device device;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "deviceSensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SensorPin> pins;
}

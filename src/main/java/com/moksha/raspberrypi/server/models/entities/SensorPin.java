package com.moksha.raspberrypi.server.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensor_pins")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorPin extends AbstractTimeEntity {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "GPIO_Pin")
    private String GPIOPin;

    @Column(name = "type")
    private String type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_sensor_id")
    private DeviceSensor deviceSensor;
}

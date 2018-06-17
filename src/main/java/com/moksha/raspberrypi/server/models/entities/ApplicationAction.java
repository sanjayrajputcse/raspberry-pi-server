package com.moksha.raspberrypi.server.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_actions")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApplicationAction extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private String time;

    @Column(name = "time_unit")
    @Enumerated(EnumType.STRING)
    private TimeUnit timeUnit;

    @Column(name = "is_done", nullable = false)
    private boolean done;

    @Column(name = "status")
    private String status;

    @Column(name = "action_started_at")
    private Timestamp actionStartedAt;

    @Column(name = "action_ended_at")
    private Timestamp actionEndedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id")
    private Application application;

}

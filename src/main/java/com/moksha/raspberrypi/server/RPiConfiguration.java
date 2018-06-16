package com.moksha.raspberrypi.server;

import io.dropwizard.Configuration;
import lombok.Data;

@Data
public class RPiConfiguration extends Configuration {

    private String application;

}

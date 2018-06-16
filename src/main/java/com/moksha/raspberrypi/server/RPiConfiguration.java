package com.moksha.raspberrypi.server;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;

@Data
public class RPiConfiguration extends Configuration {

    private String application;
    private DataSourceFactory database;

}

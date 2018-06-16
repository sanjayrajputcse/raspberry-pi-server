package com.moksha.raspberrypi.server;

import com.moksha.raspberrypi.server.resources.DeviceResource;
import io.dropwizard.setup.Environment;

public class RPiApplication extends io.dropwizard.Application<RPiConfiguration> {

    public static void main(String[] args) throws Exception {
        new RPiApplication().run(args);
    }

    @Override
    public String getName() {
        return "Raspberry Pi Server";
    }

    @Override
    public void run(RPiConfiguration rPiConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new DeviceResource());
        environment.lifecycle().manage(new RPiManage());
    }
}

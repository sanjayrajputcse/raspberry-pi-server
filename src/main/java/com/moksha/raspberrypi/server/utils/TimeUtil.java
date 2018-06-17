package com.moksha.raspberrypi.server.utils;

import java.sql.Timestamp;
import java.util.Date;

public class TimeUtil {
    public static Timestamp now() {
        return new Timestamp(new Date().getTime());
    }

    public static Long currentEpoch() {
        return new Date().getTime();
    }

    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }


}

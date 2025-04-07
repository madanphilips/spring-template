package com.example.springtemplate.common.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeUtil {

    public static String calculateDuration(LocalDateTime fromDateTime) {
        Duration duration = Duration.between(fromDateTime, LocalDateTime.now());

        if (duration.toDays() > 0) {
            return duration.toDays() + " days ago";
        } else if (duration.toHours() > 0) {
            return duration.toHours() + " hours ago";
        } else if (duration.toMinutes() > 0) {
            return duration.toMinutes() + " minutes ago";
        } else {
            return "Just now";
        }
    }
}
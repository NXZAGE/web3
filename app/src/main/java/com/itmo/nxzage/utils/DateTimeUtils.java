package com.itmo.nxzage.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("datetime")
@ViewScoped
public class DateTimeUtils implements Serializable {
    public String formatTimestamp(LocalDateTime timestamp) {
        return DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/YYYY").format(timestamp);
    }
}

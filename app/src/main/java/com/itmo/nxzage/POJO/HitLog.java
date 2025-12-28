package com.itmo.nxzage.POJO;

import java.time.LocalDateTime;

public record HitLog(double x, double y, double r, boolean result, long execTime, LocalDateTime timestamp) {}
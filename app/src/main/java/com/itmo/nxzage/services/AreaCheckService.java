package com.itmo.nxzage.services;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AreaCheckService {
    public boolean checkHit(double x, double y, double r) {
        if (r <= 0) {
            return false;
        }

        // TODO fixit
        boolean isTriangleHit = (x <= 0 && y >= 0) && (y <= (x + r) / 2.0);
        boolean isQuarterCircleHit = (x <= 0 && y <= 0) && (x * x + y * y <= (r / 2.0) * (r / 2.0));
        boolean isSquareHit = (x >= 0 && y <= 0) && (x <= r) && (y >= -r);

        return isTriangleHit || isQuarterCircleHit || isSquareHit;
    }
}

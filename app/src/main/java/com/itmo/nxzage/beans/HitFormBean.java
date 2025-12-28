package com.itmo.nxzage.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.itmo.nxzage.entities.Hit;
import com.itmo.nxzage.services.HitAcceptService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;

@Named
@RequestScoped
@Data
@NoArgsConstructor
public class HitFormBean implements Serializable {
    @Inject HitAcceptService service;
    @DecimalMin("-2.0") @DecimalMax("2.0")
    double x;
    @DecimalMin("-3.0") @DecimalMax("5.0")
    double y;
    @DecimalMin("1.0") @DecimalMax("4.0")
    double r;
    boolean result;
    long exectime;
    LocalDateTime timestamp;

    public void submit() {
        Hit hit = service.accept(x, y, r);
        result = hit.isHit();
        exectime = hit.getExectime();
        timestamp = hit.getTimestamp();
    }
}

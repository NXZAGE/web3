package com.itmo.nxzage.beans;

import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

@Named
@RequestScoped
@Data
@NoArgsConstructor
public class HitFormBean implements Serializable {
    @Inject GlobalBean globalBean;
    double x;
    double y;
    double r;

    public void submit() {
        globalBean.setFormStatus("penis");
    }
}

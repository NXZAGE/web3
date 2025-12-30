package com.itmo.nxzage.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.primefaces.PrimeFaces;
import com.itmo.nxzage.entities.Hit;
import com.itmo.nxzage.services.HitAcceptService;
import com.itmo.nxzage.utils.DateTimeUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.validation.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;

@Named
@RequestScoped
@Data
@NoArgsConstructor
public class HitFormBean implements Serializable {
    @Inject
    private HitAcceptService service;
    @Inject
    private RegistryBean registry;
    @Inject
    private DateTimeUtils datetime;
    @Inject
    private Validator validator;
    @DecimalMin("-2.0")
    @DecimalMax("2.0")
    private double x;
    @DecimalMin("-3.0")
    @DecimalMax("5.0")
    private double y;
    @DecimalMin("1.0")
    @DecimalMax("4.0")
    double r;
    boolean hit;
    long exectime;
    LocalDateTime timestamp;

    public void submit() {
        applyPrimeFacesArg("validated", true);
        Hit hitObject = service.accept(x, y, r);
        applyPrimeFacesArgs(hitObject);
        registry.applyPrimefacesHitsArg();
    }

    public void remoteSubmit() {
        try {
            applyRequestArguments();
        } catch (IllegalArgumentException e) {
            addErrorMessage(e.getMessage());
            return;
        }
        if (isValid()) {
            submit();
        }
    }

    private void applyRequestArguments() throws IllegalArgumentException {
        Map<String, String> params =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        try {
            double newX = Double.parseDouble(params.get("x"));
            double newY = Double.parseDouble(params.get("y"));
            double newR = Double.parseDouble(params.get("r"));
            setX(newX);
            setY(newY);
            setR(newR);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid params (x, y, r)");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Missed required request params (x, y, r)");
        }
    }

    private void applyPrimeFacesArgs(Hit hit) {
        applyPrimeFacesArg("lastHit", hit);
        applyPrimeFacesArg("hits", new ArrayList<>(registry.getHitLog()));
    }

    private boolean isValid() {
        Set<ConstraintViolation<HitFormBean>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<HitFormBean> violation : violations) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, violation.getMessage(), null));
            }
            return false;
        }
        return true;
    }

    private void applyPrimeFacesArg(String key, Object value) {
        PrimeFaces.current().ajax().addCallbackParam(key, value);
    }

    private void addErrorMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }
}


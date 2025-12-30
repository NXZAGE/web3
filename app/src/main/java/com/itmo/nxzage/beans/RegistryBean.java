package com.itmo.nxzage.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmo.nxzage.entities.Hit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

@Named("registry")
@ApplicationScoped
@Data
@NoArgsConstructor
public class RegistryBean implements Serializable {
    private transient final ObjectMapper mapper = new ObjectMapper();
    Deque<Hit> hitLog = new ConcurrentLinkedDeque<>();

    public void getHits() {
        PrimeFaces.current().ajax().addCallbackParam("noHits", hitLog.isEmpty());
        applyPrimefacesHitsArg();
    }

    public void applyPrimefacesHitsArg() {
        try {
            PrimeFaces.current().ajax().addCallbackParam("hits",
                    mapper.writeValueAsString(new ArrayList<>(hitLog)));
        } catch (JsonProcessingException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to share hitlog", null));
        }
    }
}

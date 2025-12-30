package com.itmo.nxzage.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.primefaces.PrimeFaces;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmo.nxzage.entities.Hit;
import com.itmo.nxzage.repository.HitRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

@Named("registry")
@ApplicationScoped
@Data
@NoArgsConstructor
public class RegistryBean implements Serializable {
    @Inject
    private transient HitRepository repository;
    private transient final ObjectMapper mapper = new ObjectMapper();
    private transient Deque<Hit> hitLog = new ConcurrentLinkedDeque<>();

    @PostConstruct
    public void init() {
        hitLog.addAll(repository.readAll());
    }

    public void getHitsRemote() {
        PrimeFaces.current().ajax().addCallbackParam("noHits", hitLog.isEmpty());
        applyPrimefacesHitsArg();
    }

    public List<Hit> getHits() {
        return new ArrayList<>(hitLog);
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

package com.itmo.nxzage.beans;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import com.itmo.nxzage.entities.Hit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

@Named("registry")
@ApplicationScoped
@Data
@NoArgsConstructor
public class RegistryBean {
    Deque<Hit> hitLog = new ConcurrentLinkedDeque<>();
}
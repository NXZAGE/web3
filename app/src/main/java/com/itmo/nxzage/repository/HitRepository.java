package com.itmo.nxzage.repository;

import java.util.stream.Stream;
import com.itmo.nxzage.entities.Hit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class HitRepository {
    @PersistenceContext
    private EntityManager em;

    public Hit save(Hit hit) {
        return hit;
        // em.persist(hit);
        // return hit;
    }

    public Stream<Hit> readAll() {
        String jpqlString = "SELECT h FROM Hit h";
        return em.createQuery(jpqlString, Hit.class).getResultStream();
    }
}

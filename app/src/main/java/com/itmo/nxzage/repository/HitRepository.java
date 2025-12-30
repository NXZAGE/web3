package com.itmo.nxzage.repository;

import java.util.List;
import com.itmo.nxzage.entities.Hit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class HitRepository {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Transactional
    public Hit save(Hit hit) {
        em.persist(hit);
        return hit;
    }

    @Transactional
    public List<Hit> readAll() {
        return em.createQuery("SELECT h FROM Hit h", Hit.class).getResultList();
    }
}

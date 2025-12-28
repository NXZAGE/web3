package com.itmo.nxzage.services;

import java.time.LocalDateTime;
import com.itmo.nxzage.beans.RegistryBean;
import com.itmo.nxzage.entities.Hit;
import com.itmo.nxzage.repository.HitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class HitAcceptService {
    @Inject AreaCheckService areaChecker;
    @Inject HitRepository repository;
    @Inject RegistryBean registry;

    public Hit accept(double x, double y, double r) {
        long startStamp = System.nanoTime();
        var hit = new Hit();
        hit.setX(x);
        hit.setY(y);
        hit.setR(r);
        hit.setTimestamp(LocalDateTime.now());
        hit.setHit(areaChecker.checkHit(x, y, r));
        hit.setExectime(System.nanoTime() - startStamp);

        return commit(hit);
    }

    @Transactional
    private Hit commit(Hit hit) {
        hit = repository.save(hit);
        registry.getHitLog().push(hit);
        return hit;
    }
}

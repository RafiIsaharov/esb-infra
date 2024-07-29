package com.rafi.esb.infra.db.model;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RouteRepository implements PanacheRepository<RouteEntity> {
    public RouteEntity findById(String uuid) {
        return find("uuid", uuid).firstResult();
    }

    public RouteEntity findByMatchingId(String matchingId) {
        return find("matchingId", matchingId).firstResult();
    }

    public RouteEntity findBySecondMatchingId(String secMatchingId) {
        return find("secMatchingId", secMatchingId).firstResult();
    }

    public void deleteById(String uuid) {
        delete("uuid", uuid);
    }
}
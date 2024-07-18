package com.rafi.esb.infra.db.service;

import com.rafi.esb.infra.db.model.RouteEntity;
import com.rafi.esb.infra.db.model.RouteRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoutePersistentService {
    final RouteRepository routeRepository;
    @SuppressWarnings("unused")
    public RoutePersistentService(RouteRepository routeRepository){
       this.routeRepository = routeRepository;
    }
    /**
     * insert
     **/
    public String insert(RouteEntity routeEntity) {
        routeRepository.persist(routeEntity);
        return routeEntity.getUuid();
    }

    /**
     * select
     **/
    public RouteEntity select(String uuid) {
        return routeRepository.findById(uuid);
    }

    public RouteEntity selectByMatchingId(String matchingId) {
        return routeRepository.findByMatchingId(matchingId);
    }

    public RouteEntity selectBySecMatchingId(String secMatchingId) {
        return routeRepository.findBySecondMatchingId(secMatchingId);
    }

    /**
     * update
     *
     * @return
     */
    public boolean update(RouteEntity routeEnt) {
        RouteEntity persitedEnt = routeRepository.findById(routeEnt.getUuid());
        if (persitedEnt != null) {
            persitedEnt = routeEnt;
            routeRepository.persist(persitedEnt);
            return true;
        } else {
            return false;
        }
    }

    public void updateTransformedRequest(String uuid, String transformedRequest) {
        RouteEntity persistent = routeRepository.findById(uuid);
        if (persistent != null) {
            persistent.setTransformedRequest(transformedRequest);
            routeRepository.persist(persistent);
        }
    }

    public void updateOrigResponse(String uuid, String origResponse) {
        RouteEntity persistent = routeRepository.findById(uuid);
        if (persistent != null) {
            persistent.setOrigResponse(origResponse);
            routeRepository.persist(persistent);
        }
    }

    public void updateTransformedResponse(String uuid, String transformedResponse) {
        RouteEntity persistent = routeRepository.findById(uuid);
        if (persistent != null) {
            persistent.setTransformedResponse(transformedResponse);
            routeRepository.persist(persistent);
        }
    }

    public void updateOrigSecResponse(String uuid, String origSecResponse) {
        RouteEntity persistent = routeRepository.findById(uuid);
        if (persistent != null) {
            persistent.setOrigSecResponse(origSecResponse);
            routeRepository.persist(persistent);
        }
    }

    public void updateTransformedSecResponse(String uuid, String transformedSecResponse) {
        RouteEntity persistent = routeRepository.findById(uuid);
        if (persistent != null) {
            persistent.setTransformedSecResponse(transformedSecResponse);
            routeRepository.persist(persistent);
        }
    }

    /** delete **/
    public void deleteRouteEntity(RouteEntity routeEntity) {
        deleteRouteById(routeEntity.getUuid());
    }

    public void deleteRouteById(String uuid) {
        routeRepository.deleteById(uuid);
    }

    public void deleteAll() {
        routeRepository.deleteAll();
    }

}
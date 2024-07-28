package com.rafi.esb.infra.handlers.persistent;

import com.rafi.esb.infra.db.model.RouteEntity;
import com.rafi.esb.infra.db.service.RoutePersistentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.tooling.model.Strings;

@SuppressWarnings("unused")
@ApplicationScoped
public class RequestHandlerTo implements RouteHandlerInterface {
    private final RoutePersistentService routePersistentService;
    public RequestHandlerTo(RoutePersistentService routePersistentService){
        this.routePersistentService = routePersistentService;
    }

    @Override
    @Transactional
    public void handle(Exchange exchange) {
        RouteEntity routeEntity = routePersistentService.select(exchange.getProperty(UUID, String.class));
        String body = exchange.getIn().getBody(String.class);
        routeEntity.setTransformedRequest(body);
        String matchingId = exchange.getProperty(MATCHING_ID, String.class);
        routeEntity.setMatchingId(Strings.isNullOrEmpty(matchingId)?exchange.getExchangeId():matchingId);
    }
}
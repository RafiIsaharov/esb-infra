package com.rafi.esb.infra.handlers.persistent;

import com.rafi.esb.infra.db.model.RouteEntity;
import com.rafi.esb.infra.db.service.RoutePersistentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.tooling.model.Strings;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@SuppressWarnings("unused")
@ApplicationScoped
public class RequestHandlerFrom implements RouteHandlerInterface {

    private final RoutePersistentService routePersistentService;
    private final String targetName;
    public RequestHandlerFrom(@ConfigProperty(name = REQUEST_VENDOR_NAME,
            defaultValue = DEFAULT) String targetName,
            RoutePersistentService routePersistentService){
        this.targetName = targetName;
        this.routePersistentService = routePersistentService;
    }
    @Override
    @Transactional
    public void handle(Exchange exchange) {
        RouteEntity routeEntity= new RouteEntity();
        String endpointUri = exchange.getFromEndpoint().getEndpointUri();
        routeEntity.setContextId(endpointUri.substring(endpointUri.lastIndexOf(":")+1));
        routeEntity.setOrigRequest(exchange.getIn().getBody(String.class));
        routeEntity.setSourceId(exchange.getExchangeId());
        routeEntity.setSourceName(exchange.getFromRouteId());
        String matchingId = exchange.getProperty(MATCHING_ID, String.class);
        routeEntity.setMatchingId(Strings.isNullOrEmpty(matchingId)?exchange.getExchangeId():matchingId);
        routeEntity.setTargetName(targetName);
        String retUuid = routePersistentService.insert(routeEntity);
        exchange.setProperty(UUID,retUuid);
    }
}
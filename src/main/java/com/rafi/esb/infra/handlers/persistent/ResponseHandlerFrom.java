package com.rafi.esb.infra.handlers.persistent;

import com.rafi.esb.infra.db.model.RouteEntity;
import com.rafi.esb.infra.db.service.RoutePersistentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.tooling.model.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
@ApplicationScoped
public class ResponseHandlerFrom implements RouteHandlerInterface {

    private final Logger logger = LoggerFactory.getLogger(ResponseHandlerFrom.class);
    private final RoutePersistentService routePersistentService;
    public ResponseHandlerFrom(RoutePersistentService routePersistentService){
        this.routePersistentService = routePersistentService;
    }
    @Override
    @Transactional
    public void handle(Exchange exchange) {
        String matchingId = exchange.getProperty(MATCHING_ID, String.class);
        if (Strings.isNullOrEmpty(matchingId)){
            logger.error("matchingId is null");
            exchange.setRouteStop(true);
            return;
        }
        logger.info("matchingId is {}",matchingId);
        RouteEntity routeEntity = routePersistentService.selectByMatchingId(matchingId);
        if(routeEntity == null){
            logger.error("routeEntity is null");
            exchange.setRouteStop(true);
            return;
        }
        routeEntity.setOrigResponse(exchange.getIn().getBody(String.class));
        routeEntity.setSecMatchingId(exchange.getProperty(SECOND_MATCHING_ID, String.class));
        exchange.setProperty(UUID,routeEntity.getUuid());
        exchange.setProperty(ORIG_MESSAGE,routeEntity.getOrigRequest());
    }
}
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
public class SecondResponseHandlerFrom implements RouteHandlerInterface {

    private final Logger logger = LoggerFactory.getLogger(SecondResponseHandlerFrom.class);
    private final RoutePersistentService routePersistentService;
    public SecondResponseHandlerFrom(RoutePersistentService routePersistentService){
        this.routePersistentService = routePersistentService;
    }
    @Override
    @Transactional
    public void handle(Exchange exchange) {
        String matchingId = exchange.getProperty(SECOND_MATCHING_ID, String.class);
        if (Strings.isNullOrEmpty(matchingId)){
            logger.error("matchingId is null");
            exchange.setRouteStop(true);
            return;
        }
        logger.info("Second matchingId is {}",matchingId);
        RouteEntity routeEntity = routePersistentService.selectBySecMatchingId(matchingId);
        if(routeEntity == null){
            logger.error("routeEntity is null");
            exchange.setRouteStop(true);
            return;
        }
        routeEntity.setOrigSecResponse(exchange.getIn().getBody(String.class));
        exchange.setProperty(UUID,routeEntity.getUuid());
        exchange.setProperty(ORIG_MESSAGE,routeEntity.getOrigRequest());
    }
}
package com.rafi.esb.infra.handlers.persistent;


import com.rafi.esb.infra.db.model.RouteEntity;
import com.rafi.esb.infra.db.service.RoutePersistentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.tooling.model.Strings;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@SuppressWarnings("unused")
@ApplicationScoped
public class ResponseHandlerTo implements RouteHandlerInterface {
    private final RoutePersistentService routePersistentService;
    private final boolean persistAfterCompletion;
    @Inject
    public ResponseHandlerTo(RoutePersistentService routePersistentService,
                             @ConfigProperty(name = PERSIST_AFTER_COMPLETION, defaultValue = "true") boolean persistAfterCompletion){
        this.routePersistentService = routePersistentService;
        this.persistAfterCompletion = persistAfterCompletion;
    }



    @Override
    @Transactional
    public void handle(Exchange exchange) {
        RouteEntity routeEntity = routePersistentService.select(exchange.getProperty(UUID, String.class));
        if(Strings.isNullOrEmpty(routeEntity.getOrigSecResponse())){
            routeEntity.setTransformedResponse(exchange.getIn().getBody(String.class));
        }
        else{
            routeEntity.setTransformedSecResponse(exchange.getIn().getBody(String.class));
        }

        if (!persistAfterCompletion)
        {
            routePersistentService.deleteRouteEntity(routeEntity);
        }
    }
}
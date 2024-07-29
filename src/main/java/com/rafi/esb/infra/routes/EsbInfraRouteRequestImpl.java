package com.rafi.esb.infra.routes;

import com.rafi.esb.infra.handlers.matching.RequestMatchingHandler;
import com.rafi.esb.infra.handlers.persistent.RequestHandlerFrom;
import com.rafi.esb.infra.handlers.persistent.RequestHandlerTo;
import com.rafi.esb.infra.handlers.transformation.RequestTransformationHandler;
import com.rafi.esb.infra.handlers.transformation.services.TransformationServiceInterface;
import com.rafi.esb.infra.routes.config.RequestRouteConfig;
import com.rafi.esb.infra.routes.handlers.RequestRouteHandlers;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class EsbInfraRouteRequestImpl extends BaseEsbRoute {
    private final RequestRouteHandlers requestRouteHandlers;

    public EsbInfraRouteRequestImpl(RequestHandlerFrom requestHandlerFrom,
                                    RequestHandlerTo requestHandlerTo,
                                    RequestTransformationHandler requestTransformationHandler,
                                    RequestMatchingHandler requestMatchingHandler){
        this.requestRouteHandlers = new RequestRouteHandlers(requestHandlerFrom,
                requestHandlerTo,
                requestTransformationHandler,
                requestMatchingHandler);
    }
    @Override
    public void configure() {
        log.debug("Starting route configuration in EsbInfraRouteResponseImpl");
        for (RequestRouteConfig config : RequestRouteConfig.values())
            if (Boolean.parseBoolean(getContext().resolvePropertyPlaceholders(config.getProperty())))
                config.configureRoute(this, requestRouteHandlers);
    }
    @SuppressWarnings("unused")
    public void addRequestTransformationService(TransformationServiceInterface newTransformationService){
        requestRouteHandlers.requestTransformationHandler().addTransformationService(newTransformationService);
    }
}

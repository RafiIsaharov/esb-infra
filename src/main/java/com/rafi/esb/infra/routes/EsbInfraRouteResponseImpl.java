package com.rafi.esb.infra.routes;

import com.rafi.esb.infra.handlers.matching.ResponseMatchingHandler;
import com.rafi.esb.infra.handlers.matching.SecondResponseMatchingHandler;
import com.rafi.esb.infra.handlers.persistent.ResponseHandlerFrom;
import com.rafi.esb.infra.handlers.persistent.ResponseHandlerTo;
import com.rafi.esb.infra.handlers.persistent.SecondResponseHandlerFrom;
import com.rafi.esb.infra.handlers.transformation.ResponseTransformationHandler;
import com.rafi.esb.infra.handlers.transformation.SecondResponseTransformationHandler;
import com.rafi.esb.infra.handlers.transformation.services.TransformationServiceInterface;
import com.rafi.esb.infra.routes.config.ResponseRouteConfig;
import com.rafi.esb.infra.routes.handlers.ResponseRouteHandlers;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class EsbInfraRouteResponseImpl extends BaseEsbRoute {
    private final ResponseRouteHandlers responseRouteHandlers;
    public EsbInfraRouteResponseImpl(ResponseHandlerFrom responseHandlerFrom,
                                     SecondResponseHandlerFrom secondResponseHandlerFrom,
                                     ResponseHandlerTo responseHandlerTo,
                                     ResponseMatchingHandler responseMatchingHandler,
                                     SecondResponseMatchingHandler secondResponseMatchingHandler,
                                     ResponseTransformationHandler responseTransformationHandler,
                                     SecondResponseTransformationHandler secondResponseTransformationHandler){
        this.responseRouteHandlers = new ResponseRouteHandlers(responseHandlerFrom,
                secondResponseHandlerFrom,
                responseHandlerTo,
                responseMatchingHandler,
                secondResponseMatchingHandler,
                responseTransformationHandler,
                secondResponseTransformationHandler);
    }
    @Override
    public void configure() {
        log.debug("Starting route configuration in EsbInfraRouteResponseImpl");
        for (ResponseRouteConfig config : ResponseRouteConfig.values())
            if (Boolean.parseBoolean(getContext().resolvePropertyPlaceholders(config.getProperty())))
                config.configureRoute(this, responseRouteHandlers);
    }
    @SuppressWarnings("unused")
    public void addResponseTransformationService(TransformationServiceInterface newTransformationService){
        responseRouteHandlers.responseTransformationHandler().addTransformationService(newTransformationService);
    }
    @SuppressWarnings("unused")
    public void addSecondResponseTransformationService(TransformationServiceInterface newTransformationService){
        responseRouteHandlers.secondResponseTransformationHandler().addTransformationService(newTransformationService);
    }
}
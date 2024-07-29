package com.rafi.esb.infra.routes.handlers;

import com.rafi.esb.infra.handlers.matching.RequestMatchingHandler;
import com.rafi.esb.infra.handlers.persistent.RequestHandlerFrom;
import com.rafi.esb.infra.handlers.persistent.RequestHandlerTo;
import com.rafi.esb.infra.handlers.transformation.RequestTransformationHandler;

public record RequestRouteHandlers(RequestHandlerFrom requestHandlerFrom,
                                   RequestHandlerTo requestHandlerTo,
                                   RequestTransformationHandler requestTransformationHandler,
                                   RequestMatchingHandler requestMatchingHandler) {}

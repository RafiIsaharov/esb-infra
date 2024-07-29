package com.rafi.esb.infra.routes.handlers;

import com.rafi.esb.infra.handlers.matching.ResponseMatchingHandler;
import com.rafi.esb.infra.handlers.matching.SecondResponseMatchingHandler;
import com.rafi.esb.infra.handlers.persistent.ResponseHandlerFrom;
import com.rafi.esb.infra.handlers.persistent.ResponseHandlerTo;
import com.rafi.esb.infra.handlers.persistent.SecondResponseHandlerFrom;
import com.rafi.esb.infra.handlers.transformation.ResponseTransformationHandler;
import com.rafi.esb.infra.handlers.transformation.SecondResponseTransformationHandler;

public record ResponseRouteHandlers(ResponseHandlerFrom responseHandlerFrom,
                                    SecondResponseHandlerFrom secondResponseHandlerFrom,
                                    ResponseHandlerTo responseHandlerTo,
                                    ResponseMatchingHandler responseMatchingHandler,
                                    SecondResponseMatchingHandler secondResponseMatchingHandler,
                                    ResponseTransformationHandler responseTransformationHandler,
                                    SecondResponseTransformationHandler secondResponseTransformationHandler) {}

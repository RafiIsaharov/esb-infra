package com.rafi.esb.infra.handlers.transformation;


import com.rafi.esb.infra.handlers.transformation.services.TransformationServiceInterface;
import org.apache.camel.Exchange;

public interface TransformationInterface {
    String TRANSFORMING_METHOD_REQUEST = "esb.infra.transforming.method.request";
    String TRANSFORMING_METHOD_RESPONSE = "esb.infra.transforming.method.response";
    String TRANSFORMING_METHOD_SECOND_RESPONSE = "esb.infra.transforming.method.second.response";
    String DEFAULT = "default";
    @SuppressWarnings("unused")
    void doTransform(Exchange exchange);
    @SuppressWarnings("unused")
    void addTransformationService(TransformationServiceInterface newTransformationService);
}

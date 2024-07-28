package com.rafi.esb.infra.handlers.transformation;

import com.rafi.esb.infra.handlers.transformation.services.TransformationService;
import com.rafi.esb.infra.handlers.transformation.services.TransformationServiceInterface;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@SuppressWarnings("unused")
@ApplicationScoped
public class RequestTransformationHandler implements TransformationInterface{

    private final TransformationService transformationService;
    private final String transformationMethod;

    public RequestTransformationHandler(TransformationService transformationService,
                                        @ConfigProperty(name = TRANSFORMING_METHOD_REQUEST,
                                                defaultValue = DEFAULT) String transformationMethod){
        this.transformationService = transformationService;
        this.transformationMethod = transformationMethod;
    }
    @Override
    public void doTransform(Exchange exchange) {
        transformationService.transform(exchange,transformationMethod);
    }

    public void addTransformationService(TransformationServiceInterface newTransformationService){
        transformationService.addTransformationService(transformationMethod,newTransformationService);
    }
}
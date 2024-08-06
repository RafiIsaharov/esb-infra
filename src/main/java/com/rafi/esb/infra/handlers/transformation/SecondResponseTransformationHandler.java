package com.rafi.esb.infra.handlers.transformation;

import com.rafi.esb.infra.handlers.transformation.services.TransformationService;
import com.rafi.esb.infra.handlers.transformation.services.TransformationServiceInterface;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@SuppressWarnings("unused")
@ApplicationScoped
public class SecondResponseTransformationHandler implements TransformationInterface{
    private final TransformationService transformationService;
    private String transformationMethod;

    public SecondResponseTransformationHandler(TransformationService transformationService,
                                               @ConfigProperty(name = TRANSFORMING_METHOD_SECOND_RESPONSE,
                                                 defaultValue = DEFAULT) String transformationMethod){
        this.transformationService = transformationService;
        this.transformationMethod = transformationMethod;
    }
    @Override
    public void doTransform(Exchange exchange) {
        transformationService.transform(exchange,transformationMethod);
    }
    @Override
    public void addTransformationService(TransformationServiceInterface newTransformationService){
        transformationMethod = newTransformationService.getTransformationMethod();
        transformationService.addTransformationService(newTransformationService);
    }
}

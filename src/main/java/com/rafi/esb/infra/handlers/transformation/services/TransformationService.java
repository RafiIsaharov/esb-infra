package com.rafi.esb.infra.handlers.transformation.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.tooling.model.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@ApplicationScoped
public class TransformationService {
    public static final Logger LOG = LoggerFactory.getLogger(TransformationService.class);
    public static final String ORIG_MSG = "orig_msg";
    private final Map<String, TransformationServiceInterface> transformationServices = new HashMap<>();

    public TransformationService(LowercaseTransformationService lowercaseTransformationService,
                                 UppercaseTransformationService uppercaseTransformationService,
                                 DefaultTransformationService defaultTransformationService
                                 ){
        transformationServices.put(lowercaseTransformationService.getTransformationMethod(),lowercaseTransformationService);
        transformationServices.put(uppercaseTransformationService.getTransformationMethod(),uppercaseTransformationService);
        transformationServices.put(defaultTransformationService.getTransformationMethod(),defaultTransformationService);
    }

    public void addTransformationService(TransformationServiceInterface transformationService ){
        LOG.info("Adding transformation service: {}",transformationService.getTransformationMethod());
        transformationServices.put(transformationService.getTransformationMethod(),transformationService);
    }
    public void transform(Exchange exchange, String transformationMethod){
        TransformationServiceInterface transformationServiceInterface = transformationServices.get(transformationMethod);
        LOG.info("Transformation method: {}",transformationMethod);
        if (transformationServiceInterface == null) {
            throw new IllegalArgumentException(String.format("Invalid transformation method: %s",transformationMethod));
        }
        String origMsg = exchange.getProperty(ORIG_MSG, String.class);
        exchange.removeProperty(ORIG_MSG);
        exchange.getIn().setBody(transformationServiceInterface.transform(
                new TransformMessage(exchange.getIn().getBody(String.class), Strings.isNullOrEmpty(origMsg)?"":origMsg)));
    }
}

package com.rafi.esb.infra.handlers.transformation.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.tooling.model.Strings;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@ApplicationScoped
public class TransformationService {
    private final Map<String, TransformationServiceInterface> transformationServices = new HashMap<>();

    public TransformationService(LowercaseTransformationService lowercaseTransformationService,
                                 UppercaseTransformationService uppercaseTransformationService,
                                 DefaultTransformationService defaultTransformationService
                                 ){
        transformationServices.put("lowercase",lowercaseTransformationService);
        transformationServices.put("uppercase",uppercaseTransformationService);
        transformationServices.put("default",defaultTransformationService);
    }

    public void addTransformationService(String transformationMethod, TransformationServiceInterface transformationService ){
        transformationServices.put(transformationMethod,transformationService);
    }
    public void transform(Exchange exchange, String transformationMethod){
        TransformationServiceInterface transformationServiceInterface = transformationServices.get(transformationMethod);
        if (transformationServiceInterface == null) {
            throw new IllegalArgumentException(String.format("Invalid transformation method: %s",transformationMethod));
        }
        String origMsg = exchange.getProperty("orig_msg", String.class);
        exchange.removeProperty("orig_msg");
        exchange.getIn().setBody(transformationServiceInterface.transform(
                new TransformMessage(exchange.getIn().getBody(String.class), Strings.isNullOrEmpty(origMsg)?"":origMsg)));
    }
}

package com.rafi.esb.infra.handlers.transformation.services;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LowercaseTransformationService implements TransformationServiceInterface {
    @Override
    public String transform(TransformMessage transformMessage) {
        return transformMessage.message().toLowerCase();
    }
}

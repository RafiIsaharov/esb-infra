package com.rafi.esb.infra.handlers.transformation.services;


public interface TransformationServiceInterface {
    String UPPERCASE = "uppercase";
    String LOWERCASE = "lowercase";
    String DEFAULT = "default";

    String transform(TransformMessage transformMessage);
    default String getTransformationMethod(){
        return this.getClass().getSimpleName();
    }
}

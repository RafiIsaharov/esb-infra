package com.rafi.esb.infra.handlers.matching.services;


import com.rafi.esb.infra.utils.JsonProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MatchingService implements MatchingServiceInterface {
    private final JsonProcessor jsonProcessor;

    @Inject
    public MatchingService(JsonProcessor jsonProcessor){
        this.jsonProcessor = jsonProcessor;
    }

    @Override
    public String extractMatchingIdFromMessage(String message, String matchingIdPath) {
        return jsonProcessor.extractTagFromJson(message, matchingIdPath);
    }

}
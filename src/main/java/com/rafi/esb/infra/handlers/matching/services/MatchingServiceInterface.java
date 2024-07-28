package com.rafi.esb.infra.handlers.matching.services;

public interface MatchingServiceInterface {
    @SuppressWarnings("unused")
    String extractMatchingIdFromMessage(String message, String matchingIdPath);
}
package com.rafi.esb.infra.handlers.matching;

import org.apache.camel.Exchange;

public interface MatchingInterface {
    String MATCHING_REQUEST_PATH = "esb.infra.matching.request";
    String MATCHING_RESPONSE_PATH = "esb.infra.matching.response";
    String MATCHING_SECOND_RESPONSE_PATH = "esb.infra.matching.second.response";
    @SuppressWarnings("unused")
    void match(Exchange exchange);
}

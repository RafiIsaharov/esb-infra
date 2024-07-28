package com.rafi.esb.infra.handlers.persistent;


import org.apache.camel.Exchange;

public interface RouteHandlerInterface {
    String UUID = "UUID";
    String MATCHING_ID = "matching_id";
    String SECOND_MATCHING_ID = "second_matching_id";
    String ORIG_MESSAGE ="orig_msg";
    String DEFAULT = "vendor";
    String REQUEST_VENDOR_NAME = "esb.infra.vendor.name";
    String PERSIST_AFTER_COMPLETION = "esb.infra.persistence.persistAfterCompletion";

    @SuppressWarnings("unused")
    void handle(Exchange exchange);
}


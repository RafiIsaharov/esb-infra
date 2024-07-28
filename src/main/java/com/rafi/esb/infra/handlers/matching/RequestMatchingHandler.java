package com.rafi.esb.infra.handlers.matching;

import com.rafi.esb.infra.handlers.matching.services.MatchingService;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.tooling.model.Strings;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static com.rafi.esb.infra.handlers.persistent.RouteHandlerInterface.MATCHING_ID;


@SuppressWarnings("unused")
@ApplicationScoped
public class RequestMatchingHandler implements MatchingInterface{
    private final MatchingService matchingService;
    private final String matchingMessageRequestPath;
    public RequestMatchingHandler(MatchingService matchingService,
                                  @ConfigProperty(name = MATCHING_REQUEST_PATH) String matchingMessageRequestPath){
        this.matchingService = matchingService;
        this.matchingMessageRequestPath = matchingMessageRequestPath;
    }


    @Override
    public void match(Exchange exchange) {
        String matchingIdFromMessage = matchingService.extractMatchingIdFromMessage(exchange.getMessage().getBody(String.class),
                matchingMessageRequestPath);
        exchange.setProperty(MATCHING_ID, Strings.isNullOrEmpty(matchingIdFromMessage)?exchange.getExchangeId(): matchingIdFromMessage);
    }
}

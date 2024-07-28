package com.rafi.esb.infra.handlers.matching;

import com.rafi.esb.infra.handlers.matching.services.MatchingService;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static com.rafi.esb.infra.handlers.persistent.RouteHandlerInterface.SECOND_MATCHING_ID;

@SuppressWarnings("unused")
@ApplicationScoped
public class SecondResponseMatchingHandler implements MatchingInterface{
    private final MatchingService matchingService;
    private final String matchingMessageSecondResponsePath;
    public SecondResponseMatchingHandler(MatchingService matchingService,
                                         @ConfigProperty(name = MATCHING_SECOND_RESPONSE_PATH)
                                         String matchingMessageSecondResponsePath){
        this.matchingService = matchingService;
        this.matchingMessageSecondResponsePath = matchingMessageSecondResponsePath;
    }
    @Override
    public void match(Exchange exchange) {
            exchange.setProperty(SECOND_MATCHING_ID,
                    matchingService.extractMatchingIdFromMessage(exchange.getMessage().getBody(String.class),
                    matchingMessageSecondResponsePath));

    }
}

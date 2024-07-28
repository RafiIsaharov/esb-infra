package com.rafi.esb.infra.handlers.matching;

import com.rafi.esb.infra.handlers.matching.services.MatchingService;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.tooling.model.Strings;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static com.rafi.esb.infra.handlers.persistent.RouteHandlerInterface.MATCHING_ID;
import static com.rafi.esb.infra.handlers.persistent.RouteHandlerInterface.SECOND_MATCHING_ID;

@SuppressWarnings("unused")
@ApplicationScoped
public class ResponseMatchingHandler implements MatchingInterface{
    private final MatchingService matchingService;
    private final String matchingMessageResponsePath;
    public ResponseMatchingHandler(MatchingService matchingService,
                                   @ConfigProperty(name = MATCHING_RESPONSE_PATH) String matchingMessageResponsePath){
        this.matchingService = matchingService;
        this.matchingMessageResponsePath = matchingMessageResponsePath;
    }
    @Override
    public void match(Exchange exchange) {
        String[] responsePathSplit = matchingMessageResponsePath.split(",");

        String matchingIdFromMessage = matchingService.extractMatchingIdFromMessage(exchange.getMessage().getBody(String.class),
                responsePathSplit[0]);
        matchingIdFromMessage = Strings.isNullOrEmpty(matchingIdFromMessage)?exchange.getProperty(MATCHING_ID, String.class):
                matchingIdFromMessage;
        exchange.setProperty(MATCHING_ID, Strings.isNullOrEmpty(matchingIdFromMessage)?exchange.getExchangeId():matchingIdFromMessage);

        if(responsePathSplit.length>1) {
            exchange.setProperty(SECOND_MATCHING_ID, matchingService.extractMatchingIdFromMessage(exchange.getMessage().getBody(String.class),
                    responsePathSplit[1]));
        }

    }
}

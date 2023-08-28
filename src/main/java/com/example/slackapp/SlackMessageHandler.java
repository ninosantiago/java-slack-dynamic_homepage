package com.example.slackapp;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.example.slackapp.service.AzureQna;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.request.Request;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.MessageEvent;

public class SlackMessageHandler {
    final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    BoltEventHandler<MessageEvent> eventHandler() {
        return (req, ctx) -> {
            try {
                sendSlackMessage(req, ctx);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // return a successful response
            return ctx.ack();
        };
    }

    @Async
    private CompletableFuture<Object> sendSlackMessage(EventsApiPayload<MessageEvent> req, EventContext ctx)
            throws Exception {
        // get the text of the message
        String text = req.getEvent().getText();
        // log the message
        logger.info("Received message: {}", text);

        // get intent
        AzureQna qna = new AzureQna();
        String intent = qna.getIntent(text);
        logger.info("intent: " + intent);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(intent);
        String answer = jsonNode.at("/answers/0/answer").asText("no answer found");
        logger.info("got answer: " + answer);

        String finalAnswer = answer.equals("") ? "no answer found" : answer;
        // String finalAnswer="blah";
        try {
            // send a reply
            ChatPostMessageResponse res = ctx.client().chatPostMessage(r -> r
                    .channel(req.getEvent().getChannel())
                    .token(ctx.getBotToken())
                    .text(finalAnswer));
            logger.info("response:" + res.toString());
            return CompletableFuture.completedFuture(res);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return a successful response
        return CompletableFuture.completedFuture(null);

    }
}

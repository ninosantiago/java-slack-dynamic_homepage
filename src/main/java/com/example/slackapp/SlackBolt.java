package com.example.slackapp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.socket_mode.SocketModeClient;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.view.Views.*;
import static com.slack.api.model.block.element.BlockElements.*;

public class SlackBolt {
    // declare the logger for this class
    final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    final String SLACK_APP_TOKEN = System.getenv("SLACK_APP_TOKEN");

    public SocketModeApp boltApp() throws IOException {
        // create appconfig
        App app = new App();

        // create /test slash command handler
        app.command("/test", (req, ctx) -> {
            logger.info("got context");
            logger.info(ctx.toString());
            return ctx.ack("Hello World!");
        });

        // create event handler for app_home_opened
        app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
            logger.info("got home open event");

            var appHomeView = view(view -> view
                    .type("home")
                    .blocks(asBlocks(
                            section(section -> section
                                    .text(markdownText(mt -> mt.text("*Welcome to your _App's Home_* :tada:")))),
                            divider(),
                            section(section -> section.text(markdownText(mt -> mt.text(
                                    "This button won't do much for now but you can set up a listener for it using the `actions()` method and passing its unique `action_id`. See an example on <https://slack.dev/java-slack-sdk/guides/interactive-components|slack.dev/java-slack-sdk>.")))),
                            actions(actions -> actions
                                    .elements(asElements(
                                            button(b -> b.text(plainText(pt -> pt.text("Click me!"))).value("button1")
                                                    .actionId("button_1"))))))));

            var res = ctx.client().viewsPublish(r -> r
                    .token(SLACK_BOT_TOKEN)
                    .userId(payload.getEvent().getUser())
                    .view(appHomeView));
            logger.info("response:" + res.toString());
            return ctx.ack();
        });

        SocketModeApp socketModeApp = new SocketModeApp(
                SLACK_APP_TOKEN,
                SocketModeClient.Backend.JavaWebSocket,
                app);

        return socketModeApp;
    }
}

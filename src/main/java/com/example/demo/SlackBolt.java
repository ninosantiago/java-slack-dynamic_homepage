package com.example.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.socket_mode.SocketModeClient;

public class SlackBolt {
    // declare the logger for this class
    final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    public SocketModeApp boltApp() throws IOException {
        App app = new App();

        app.command("/test", (req, ctx) -> {
            logger.info("got context");
            logger.info(ctx.toString());
            return ctx.ack("Hello World!");
        });

        SocketModeApp socketModeApp = new SocketModeApp(
                System.getenv("SLACK_APP_TOKEN"),
                SocketModeClient.Backend.JavaWebSocket,
                app);
                
        return socketModeApp;
    }
}

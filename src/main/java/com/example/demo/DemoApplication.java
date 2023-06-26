package com.example.demo;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws IOException, Exception {
		SpringApplication.run(DemoApplication.class, args);

		SlackBolt slackBolt = new SlackBolt();

		SocketModeApp boltApp = slackBolt.boltApp();

		// check if environment variable is set for runSlackBolt as true
		if (Boolean.parseBoolean(System.getProperty("runSlackBolt"))) {
			System.out.println("runSlackBolt is true");
			boltApp.startAsync();
		} else {
			System.out.println("runSlackBolt is false");
		}

	}

}

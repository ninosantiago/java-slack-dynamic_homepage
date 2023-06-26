package com.example.slackapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

    // create unit test for the getListSample() method
	@Test
    void testGetListSample() {
        // create a new instance of the Controller class
        Controller controller = new Controller();
        // call the getListSample() method
        try {
            controller.getListSample();
            // check if the output is correct
            List<String> strings = new ArrayList<>();

            strings.add("Hello");
            strings.add("World");
            strings.add("Howdy");

            assertEquals(strings, controller.getListSample());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // create unit test for the getParamSample() method


}

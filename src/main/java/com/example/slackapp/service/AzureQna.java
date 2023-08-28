package com.example.slackapp.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AzureQna {
    // create a method that will call the Azure QnA Maker service
    // this method will accept a String as input and return a String as output
    // the input String is the question to be sent to the QnA Maker service
    // the output String is the answer from the QnA Maker service
    // the method will be called from the SlackBolt class

    final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    String AZURE_QNA_KEY=System.getenv("AZURE_QNA_KEY");

    // create get intent method
    public String getIntent(String questionString) {
        // create a String variable to hold the intent
        ObjectMapper mapper = new ObjectMapper();

        // call
        // https://lang-bot-1.cognitiveservices.azure.com/language/:query-knowledgebases?projectName=microsoft-qna-sample1&api-version=2021-10-01&deploymentName=production
        // using RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://lang-bot-1.cognitiveservices.azure.com/language/:query-knowledgebases?projectName=microsoft-qna-sample1&api-version=2021-10-01&deploymentName=production";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key", AZURE_QNA_KEY);

        Map<String, Object> map = new HashMap<>();
        map.put("enable", true);
        map.put("topAnswersWithSpan", 1);
        map.put("confidenceScoreThreshold", 0.1);

        //convert map to jsonNode
        JsonNode mapJsonNode= mapper.convertValue(map, JsonNode.class);

        JsonNode jsonNode = mapper.createObjectNode()
                .put("question", questionString)
                .put("includeUnstructuredSources", true)
                .put("confidenceScoreThresh ", "0.1")
                .set("answerSpanRequest",mapJsonNode);

        logger.info("payload: ");
        logger.info(jsonNode.toString());

        String payload = jsonNode.toString();
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        String response = restTemplate.postForObject(url, request, String.class);
        System.out.println(response);

        // return the intent
        return response;
    }
}

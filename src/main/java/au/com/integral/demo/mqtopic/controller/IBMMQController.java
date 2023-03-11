package au.com.integral.demo.mqtopic.controller;

import au.com.integral.demo.mqtopic.model.SampleMessage;
import au.com.integral.demo.mqtopic.model.SampleResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IBMMQController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping(value = "/sendMessage", produces = "application/json")
    public ResponseEntity<String> sendMessage(@RequestBody SampleMessage message) throws Exception {

        log.info("Incoming Request: {}", message);
        try {
            String sampleResponse = producerTemplate.requestBody("direct:requestMessage", message, String.class);
            return new ResponseEntity<>(sampleResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/hello", produces = "application/json")
    public String hello() {
        return "{ \"body\": \"hello world\" }";
    }
}

package au.com.integral.demo.mqtopic.controller;

import au.com.integral.demo.mqtopic.model.SampleMessage;
import au.com.integral.demo.mqtopic.model.SampleResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class IBMMQController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping(value = "/sendMessage", produces = "application/json")
    public ResponseEntity<String> sendMessage(@RequestBody SampleMessage message,
                                              @RequestHeader("RequestID") String requestID) throws Exception {

        log.info("Incoming Request: {}", message);
        try {
            if (StringUtils.isNotEmpty(requestID)) {
                String sampleResponse = producerTemplate.requestBodyAndHeader("direct:requestMessage", message, "RequestID", requestID, String.class);
                return new ResponseEntity<>(sampleResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Empty Request ID Header", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/hello", produces = "application/json")
    public String hello() {
        return "{ \"body\": \"hello world\" }";
    }
}

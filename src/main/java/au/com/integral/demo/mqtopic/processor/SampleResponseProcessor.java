package au.com.integral.demo.mqtopic.processor;

import au.com.integral.demo.mqtopic.model.SampleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;


@Component
public class SampleResponseProcessor implements Processor {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        SampleResponse sampleResponse = new SampleResponse("IBM MQ pub/sub test worked");
        String response = objectMapper.writeValueAsString(sampleResponse);
        exchange.getIn().setBody(response, String.class);
    }
}

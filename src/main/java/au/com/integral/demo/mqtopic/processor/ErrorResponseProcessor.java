package au.com.integral.demo.mqtopic.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorResponseProcessor implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {

        Exception exception = exchange.getException();

        log.error("Encountered error: {}", exception);

        exchange.getIn().setBody("", String.class);

    }
}

package au.com.integral.demo.mqtopic.route;

import au.com.integral.demo.mqtopic.model.SampleMessage;
import au.com.integral.demo.mqtopic.model.SampleResponse;
import au.com.integral.demo.mqtopic.processor.ErrorResponseProcessor;
import au.com.integral.demo.mqtopic.processor.SampleResponseProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jms.JmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SampleMessageRoute extends RouteBuilder {

    // JSON Data Format
    JacksonDataFormat sampleMessageFormat = new JacksonDataFormat(SampleMessage.class);

    // JSON Data Format
    JacksonDataFormat jsonDataFormat = new JacksonDataFormat(SampleResponse.class);

    @Autowired
    ErrorResponseProcessor errorResponseProcessor;

    @Autowired
    SampleResponseProcessor sampleResponseProcessor;

    @Value("${ibm.mq.deadLetterUri}")
    private String deadLetterUri;

    @Value("${ibm.mq.url}")
    private String ibmMqUrl;

    @Value("${ibm.sub.url}")
    private String ibmSub1Url;

    @Value("${ibm.sub2.url}")
    private String ibmSub2Url;

    @Override
    public void configure() throws Exception {

        errorHandler(deadLetterChannel(deadLetterUri)
                .maximumRedeliveryDelay(5000)
                .redeliveryDelay(1000)
                .maximumRedeliveries(5));

        onException(Exception.class)
                .process(errorResponseProcessor);

        from("direct:requestMessage").routeId("producerRoute")
                .log("Request: ${body}")
                .marshal(sampleMessageFormat)
                .setHeader(JmsConstants.JMS_X_GROUP_ID, simple("JC_TEST"))
                .to(ibmMqUrl+"?exchangePattern=InOnly").id("sendQueue")
                .process(sampleResponseProcessor)
                .log("Response: ${body}")
                .end();

        from(ibmSub1Url).routeId("sub1Route")
                .log("Camel Headers: ${headers}")
                .log("Sub1 received the payload: ${body}")
                .end();

        from(ibmSub2Url).routeId("sub2Route")
                .log("Camel Headers: ${headers}")
                .log("Sub2 received the payload: ${body}")
                .end();
    }
}

package au.com.integral.demo.mqtopic.config;

import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.processor.errorhandler.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedeliveryConfig {

    @Value("${ibm.mq.deadLetterUri}")
    private String deadLetterUri;

    @Bean
    public ErrorHandlerBuilder errorHandlerBuilder() {
        DeadLetterChannelBuilder deadLetterChannel = new
                DeadLetterChannelBuilder();
        deadLetterChannel.setDeadLetterUri(deadLetterUri);
        deadLetterChannel.setRedeliveryPolicy(redeliveryPolicy());
        deadLetterChannel.setUseOriginalBody(true);
        return deadLetterChannel;
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(2);
        redeliveryPolicy.setMaximumRedeliveryDelay(5000);
        redeliveryPolicy.setRedeliveryDelay(10000);
        return redeliveryPolicy;
    }
}

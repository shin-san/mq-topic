package au.com.integral.demo.mqtopic.config;

import org.apache.camel.ErrorHandlerFactory;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.model.RedeliveryPolicyDefinition;
import org.apache.camel.processor.errorhandler.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedeliveryConfig {

    @Value("${ibm.mq.deadLetterUri}")
    private String deadLetterUri;

    @Bean
    public DeadLetterChannelBuilder errorHandlerBuilder() {
        DeadLetterChannelBuilder deadLetterChannel = new
                DeadLetterChannelBuilder();
        deadLetterChannel.setDeadLetterUri(deadLetterUri);
        deadLetterChannel.setRedeliveryPolicy(redeliveryPolicy());
        deadLetterChannel.setUseOriginalBody("true");
        return deadLetterChannel;
    }


    @Bean
    public RedeliveryPolicyDefinition redeliveryPolicy() {
        RedeliveryPolicyDefinition redeliveryPolicyDefinition = new RedeliveryPolicyDefinition();
        redeliveryPolicyDefinition.setMaximumRedeliveries("2");
        redeliveryPolicyDefinition.setMaximumRedeliveryDelay("5000");
        redeliveryPolicyDefinition.setRedeliveryDelay("10000");

        return redeliveryPolicyDefinition;
    }
}

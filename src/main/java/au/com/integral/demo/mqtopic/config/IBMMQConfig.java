package au.com.integral.demo.mqtopic.config;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;

@Slf4j
@Configuration
public class IBMMQConfig {

    @Value("${ibm.mq.queueManager}")
    private String queueManager;

    @Value("${ibm.mq.channel}")
    private String channel;

    @Value("${ibm.mq.connName}")
    private String connName;

    @Value("${ibm.mq.host}")
    private String host;

    @Value("${ibm.mq.port}")
    private int port;

    @Value("${ibm.mq.user}")
    private String user;

    @Value("${ibm.mq.password}")
    private String password;

    @Value("${ibm.mq.sslCipherSuite}")
    private String sslCipherSuite;

    @Primary
    @Bean(name = "mqQueueConnectionFactory")
    public MQConnectionFactory mqQueueConnectionFactory() {

        MQConnectionFactory mqQueueConnectionFactory = new MQConnectionFactory();
        try {
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            mqQueueConnectionFactory.setChannel(channel);
            mqQueueConnectionFactory.setQueueManager(queueManager);
//            mqQueueConnectionFactory.setSSLCipherSuite(sslCipherSuite);
//            mqQueueConnectionFactory.setHostName(host);
//            mqQueueConnectionFactory.setPort(port);
            mqQueueConnectionFactory.setConnectionNameList(connName);
            mqQueueConnectionFactory.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
        } catch (Exception e) {
            log.error("Could not connect to MQ!", e);
            throw new RuntimeException("Could not connect to MQ!", e);
        }

        return mqQueueConnectionFactory;
    }

    @Bean(name = "userCredentialsConnectionFactoryAdapter")
    public UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter() {
        UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter =
                new UserCredentialsConnectionFactoryAdapter();
        userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory());
        userCredentialsConnectionFactoryAdapter.setUsername(user);
        userCredentialsConnectionFactoryAdapter.setPassword(password);
        return userCredentialsConnectionFactoryAdapter;
    }

    @Bean(name = "ibmMq")
    public JmsComponent jmsComponent() {

        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConnectionFactory(userCredentialsConnectionFactoryAdapter());

        return jmsComponent;
    }

}

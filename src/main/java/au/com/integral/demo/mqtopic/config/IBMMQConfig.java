package au.com.integral.demo.mqtopic.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.jms.ConnectionFactory;

@Slf4j
@Configuration
public class IBMMQConfig {

//    @Value("${ibm.mq.queueManager}")
//    private String queueManager;
//
//    @Value("${ibm.mq.channel}")
//    private String channel;
//
//    @Value("${ibm.mq.connName}")
//    private String connName;
//
//    @Value("${ibm.mq.host}")
//    private String host;
//
//    @Value("${ibm.mq.port}")
//    private int port;
//
//    @Value("${ibm.mq.user}")
//    private String user;
//
//    @Value("${ibm.mq.password}")
//    private String password;
//
//    @Value("${ibm.mq.sslCipherSuite}")
//    private String sslCipherSuite;

    @Value("${mq.jndi}")
    private String jndiName;

//    @Primary
//    @Bean(name = "mqQueueConnectionFactory")
//    public MQConnectionFactory mqQueueConnectionFactory() {
//
//        MQConnectionFactory mqQueueConnectionFactory = new MQConnectionFactory();
//        try {
//            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
//            mqQueueConnectionFactory.setChannel(channel);
//            mqQueueConnectionFactory.setQueueManager(queueManager);
////            mqQueueConnectionFactory.setSSLCipherSuite(sslCipherSuite);
////            mqQueueConnectionFactory.setHostName(host);
////            mqQueueConnectionFactory.setPort(port);
//            mqQueueConnectionFactory.setConnectionNameList(connName);
//            mqQueueConnectionFactory.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
//        } catch (Exception e) {
//            log.error("Could not connect to MQ!", e);
//            throw new RuntimeException("Could not connect to MQ!", e);
//        }
//
//        return mqQueueConnectionFactory;
//    }

//    @Bean(name = "userCredentialsConnectionFactoryAdapter")
//    public UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter() {
//        UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter =
//                new UserCredentialsConnectionFactoryAdapter();
//        userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory());
//        userCredentialsConnectionFactoryAdapter.setUsername(user);
//        userCredentialsConnectionFactoryAdapter.setPassword(password);
//        return userCredentialsConnectionFactoryAdapter;
//    }

    @Primary
    @Bean(name = "mqConnectionFactory")
    public ConnectionFactory mqConnectionFactory() {
        try {
            JndiObjectFactoryBean jndiBean = new JndiObjectFactoryBean();
            jndiBean.setJndiName(jndiName);
            jndiBean.setLookupOnStartup(false);
            jndiBean.setCache(true);
            jndiBean.setProxyInterface(ConnectionFactory.class);
            jndiBean.afterPropertiesSet();
            return (ConnectionFactory) jndiBean.getObject();
        } catch (Exception e) {
            log.error("Could not get JNDI Object", e);
            throw new RuntimeException("Could not get JNDI Object", e);
        }
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(mqConnectionFactory());
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean(name = "ibmMq")
    public JmsComponent jmsComponent() {

        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setMessageIdEnabled(true);
        jmsComponent.setIncludeSentJMSMessageID(true);
        jmsComponent.setIncludeAllJMSXProperties(true);
        jmsComponent.setConnectionFactory(mqConnectionFactory());

//        jmsComponent.setConnectionFactory(userCredentialsConnectionFactoryAdapter());

        return jmsComponent;
    }

}

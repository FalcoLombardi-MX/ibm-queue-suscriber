package com.mx.ibm.queue.connection;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

@Getter
@Slf4j
@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Connection {

    @Autowired
    Environment env;

    @Bean
    public QueueConnection get() throws JMSException {
        return (QueueConnection) getQueueConnection();
    }

    @Bean
    public QueueConnection getQueueConnection() throws JMSException {
        QueueConnectionFactory qcf = new MQQueueConnectionFactory();
        ((MQQueueConnectionFactory) qcf).setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        ((MQQueueConnectionFactory) qcf).setHostName(env.getProperty("queue.hostname"));
        ((MQQueueConnectionFactory) qcf).setPort(Integer.parseInt(env.getProperty("queue.port")));
        ((MQQueueConnectionFactory) qcf).setQueueManager(env.getProperty("queue.manager"));
        ((MQQueueConnectionFactory) qcf).setChannel(env.getProperty("queue.channel"));

        return qcf.createQueueConnection();
    }
}

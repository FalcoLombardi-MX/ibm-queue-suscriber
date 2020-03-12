package com.mx.ibm.queue.queue;

import com.mx.ibm.queue.connection.Connection;
import com.mx.ibm.queue.entity.XMLResponse;
import com.mx.ibm.queue.util.Constants;
import com.ibm.jms.JMSBytesMessage;
import com.ibm.mq.jms.MQQueueReceiver;
import com.ibm.mq.jms.MQQueueSession;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QueueService {

    @Autowired
    Environment env;

    @Autowired
    Connection connection;

    public XMLResponse getMessages() throws JMSException, JAXBException {
        MQQueueReceiver mQQueueReceiver = (MQQueueReceiver) getQueueReceiver();
        JMSBytesMessage receivedMessage
                = (JMSBytesMessage) mQQueueReceiver.receive(Constants.RECEIVER_TIMEOUT);

        String responseXml = getXML(receivedMessage)
                .replace(Constants.NAMESPACE_CEPMESAGGE, Constants.NAMESPACE_EMPTY);
        
        log.info("XML: {}", responseXml);

        InputStream stream = new ByteArrayInputStream(responseXml.getBytes(StandardCharsets.UTF_8));

        JAXBContext jaxbContext = JAXBContext.newInstance(XMLResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        //TicketResponse ticket = (TicketResponse) unmarshaller.unmarshal(new File("C:/tmp/test.xml"));
        XMLResponse ticket = (XMLResponse) unmarshaller.unmarshal(stream);
        log.info("Ticket: {}", ticket);
        
        return ticket;
    }

    @Bean
    public MQQueueReceiver getQueueReceiver() throws JMSException {

        QueueConnection queueConnection = connection.get();
        queueConnection.start();

        MQQueueSession session = (MQQueueSession) queueConnection.createQueueSession(
                false,
                Session.CLIENT_ACKNOWLEDGE
        );

        Queue responseQueue = ((MQQueueSession) session).createQueue(
                env.getProperty("queue.protocol")
                        .concat(env.getProperty("queue.manager"))
                        .concat("/")
                        .concat(env.getProperty("queue.name"))
        );

        return (MQQueueReceiver) session.createReceiver(responseQueue);
    }

    private String getXML(JMSBytesMessage receivedMessage) {
        String responseXml = "";
        if (receivedMessage instanceof BytesMessage) {
            BytesMessage bm = (BytesMessage) receivedMessage;
            byte[] bys = null;
            try {
                bys = new byte[(int) bm.getBodyLength()];
                bm.readBytes(bys);
                responseXml = new String(bys);
            } catch (JMSException e) {
                log.error(e.getMessage());
            }
        } else {
            TextMessage bm = (TextMessage) receivedMessage;

            try {
                responseXml = bm.getText();
            } catch (JMSException e) {
                log.error(e.getMessage());
            }
        }
        return responseXml;
    }
}

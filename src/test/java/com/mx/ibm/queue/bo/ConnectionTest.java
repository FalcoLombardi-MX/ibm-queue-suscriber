package com.mx.ibm.queue.bo;

import com.mx.ibm.queue.queue.QueueService;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ConnectionTest extends TestCase {

    @Autowired
    QueueService queueService;

}

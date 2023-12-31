package com.emre.employeeAPI.consumer;



import com.emre.employeeAPI.constants.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.emre.employeeAPI.consumer.*;

import java.util.List;

@Service
public class KafkaEmployeeEventConsumer {

  private final Logger logger = LoggerFactory.getLogger(KafkaEmployeeEventConsumer.class);


    //__listener.  #{logEventConsumer.appConf.getTopicName()}
    @KafkaListener(topics = AppConstants.EMPLOYEE_TOPIC_NAME, groupId = AppConstants.GROUP_ID,
            containerFactory = "employeeEventKafkaListenerContainerFactory")
    public void consume(String empEvent) {

            logger.info("Employee Event received -> {}", empEvent);

  }
}

package com.emre.employeeAPI.producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.emre.employeeAPI.configuration.KafkaProducerConfig;
import com.emre.employeeAPI.constants.AppConstants;


import java.util.List;

import static com.emre.employeeAPI.constants.AppConstants.EMPLOYEE_TOPIC_NAME;

@Service
public class KafkaEmployeeEventProducerService {
  private static final Logger logger = LoggerFactory.getLogger(KafkaEmployeeEventProducerService.class);


  @Autowired private KafkaProducerConfig kafkaProducerConfig;


  public void sendTransactionListMessage(String eventForEmployee){
    logger.info("Message sent for employee event -> {}", eventForEmployee);
    kafkaProducerConfig.transacionKafkaTemplate().send(EMPLOYEE_TOPIC_NAME, eventForEmployee);
  }


}

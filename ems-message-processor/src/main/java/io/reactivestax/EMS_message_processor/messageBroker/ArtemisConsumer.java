package io.reactivestax.EMS_message_processor.messageBroker;

import io.reactivestax.EMS_message_processor.twilio.TwilioRestService;
import io.reactivestax.EMS_message_processor.twilio.TwilioService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class ArtemisConsumer {

    @Autowired
    private TwilioService twilioService;

    @Autowired
    private TwilioRestService twilioRestService;

    @JmsListener(destination = "ems-queue")
    public void consumeMessage(String message) {

        log.info("Message received from queue: {}", message);
        String[] splittedMessage = message.split(",");
        String messageType = splittedMessage[0];
        String receivedMessage = splittedMessage[1];
        twilioService.callTwilioBasedOnMessageType(receivedMessage, messageType);
//        twilioRestService.callTwilio(receivedMessage);
    }

}

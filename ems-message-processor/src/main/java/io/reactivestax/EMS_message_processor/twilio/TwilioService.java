package io.reactivestax.EMS_message_processor.twilio;

import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.reactivestax.EMS_message_processor.domain.Otp;
import io.reactivestax.EMS_message_processor.domain.Phone;
import io.reactivestax.EMS_message_processor.domain.Sms;
import io.reactivestax.EMS_message_processor.repository.EmailRepository;
import io.reactivestax.EMS_message_processor.repository.OTPRepository;
import io.reactivestax.EMS_message_processor.repository.PhoneRepository;
import io.reactivestax.EMS_message_processor.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class TwilioService {

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private OTPRepository otpRepository;


    public String sendTwilioSms(String toPhoneNumber, String messageBody) {
        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber), // To phone number
                new PhoneNumber(twilioPhoneNumber), // From Twilio number
                messageBody // Message body
        ).create();
        return message.getSid(); // Return message SID
    }


    public void sendTwilioCall(String receiverContact, String message) {
        Call.creator(new PhoneNumber(receiverContact),
                        new PhoneNumber(twilioPhoneNumber),
                        new com.twilio.type.Twiml("<Response><Say>" + message + "</Say></Response>")
                )
                .create();
    }

    public void callTwilioBasedOnMessageType(String message, String messageType) {
        if(messageType.equals("phone")){
            Phone receiverPhone = phoneRepository.findById(Long.parseLong(message)).orElseThrow(() -> new RuntimeException("phone not found"));
            sendTwilioCall(receiverPhone.getOutgoingPhoneNumber(), "call to" + receiverPhone.getClientId());
            return;
        }
        if(messageType.equals("sms")){
            Sms sms = smsRepository.findById(Long.parseLong(message)).orElseThrow(() -> new RuntimeException("phone not found"));
            sendTwilioSms(sms.getPhone(), sms.getMessage());
            return;
        }
        if(messageType.equals("otp-sms")) {
            Otp otp = otpRepository.findById(Long.parseLong(message)).orElseThrow(() -> new RuntimeException("Phone to send the otp is not found"));
            String otpDelivery = "Your valid otp is: " +  otp.getValidOtp();
            sendTwilioSms(otp.getPhone(), otpDelivery);
            return;
        }

        if(messageType.equals("otp-phone")) {
            Otp otp = otpRepository.findById(Long.parseLong(message)).orElseThrow(() -> new RuntimeException("Phone to send the otp is not found"));
            sendTwilioCall(otp.getPhone(), otp.getValidOtp());
        }


    }


//    public void twilioEmail(String toEmail,String message) throws IOException {
//        Email email = new Email(fromEmail);
//        String subject = "Email from Twilio";
//        Email recipient = new Email(toEmail);
//        Content content = new Content("text/plain", message);
//        Mail mail = new Mail(email, subject, recipient, content);
//
//        SendGrid sendGrid = new SendGrid(sendGridApiKey);
//        Request request = new Request();
//
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            Response response = sendGrid.api(request);
//        } catch (IOException ex) {
//            throw new IOException("Error sending email: " + ex.getMessage(), ex);
//        }
//    }
}

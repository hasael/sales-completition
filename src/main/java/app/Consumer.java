package app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Slf4j
@Component
public class Consumer implements MessageListener {

    @Autowired
    SalesNotifier salesNotifier;

    @Override
    @JmsListener(destination = "sales")
    public void onMessage(Message message) {
        try {
            log.info("Received message: " + message);

            var text = ((TextMessage) message).getText();
            Sale sale = fromJson(text);
            salesNotifier.notifySale(sale);

        } catch (Exception ex) {
            log.error("Error consuming message: " + ex.getLocalizedMessage());
        }
    }

    private Sale fromJson(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, Sale.class);
    }
}

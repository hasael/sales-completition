package app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaSalesNotifier implements SalesNotifier {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void notifySale(Sale sale) {

        log.debug("Notifying sale to kafka: " + sale.getCostumeId());
        kafkaTemplate.send("sales", toJson(sale));
    }

    private String toJson(Sale sale) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(sale);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}

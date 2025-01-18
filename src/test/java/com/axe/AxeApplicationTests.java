package com.axe;

import com.axe.delivery_notes.DeliveryNoteRepository;
import com.axe.delivery_notes.DeliveryNoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {AxeApplication.class, MockDataSourceConfig.class})
class AxeApplicationTests {

    @Test
    void contextLoads() {
        // Your test logic here
    }

}

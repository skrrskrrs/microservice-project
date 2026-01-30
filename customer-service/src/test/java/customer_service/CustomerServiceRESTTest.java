package customer_service;

import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CustomerServiceRESTTest {
}

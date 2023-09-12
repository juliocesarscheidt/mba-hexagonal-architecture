package br.com.fullcycle.hexagonal.infrastructure;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.fullcycle.hexagonal.infrastructure.Main;

//integration test
@ActiveProfiles("test")
@SpringBootTest(classes = Main.class)
public abstract class IntegrationTest {
}

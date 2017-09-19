package demo

import demo.domain.Customer
import demo.repository.CustomerRepository
import groovy.util.logging.Slf4j
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@Slf4j
@SpringBootApplication
class Application {

    static void main(String[] args) {
        SpringApplication.run Application, args
    }

    @Bean
    CommandLineRunner loadData(CustomerRepository repository) {
        return {
            repository.with {
                save(new Customer(firstName: 'Jack', lastName: 'Bauer'))
                save(new Customer(firstName: 'Chloe', lastName: "O'Brian"))
                save(new Customer(firstName: 'Kim', lastName: 'Bauer'))
                save(new Customer(firstName: 'David', lastName: 'Palmer'))
                save(new Customer(firstName: 'Michelle', lastName: 'Dessler'))
            }
        }
    }
}

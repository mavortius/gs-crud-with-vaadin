package demo.repository

import demo.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByLastNameStartsWithIgnoreCase(String lastName)
}

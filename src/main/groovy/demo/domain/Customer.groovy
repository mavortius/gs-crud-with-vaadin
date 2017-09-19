package demo.domain

import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@ToString(includePackage = false, includes = ['fistName', 'lastName'])
@CompileStatic
@Entity
class Customer {

    @Id
    @GeneratedValue
    Long id

    String firstName

    String lastName
}

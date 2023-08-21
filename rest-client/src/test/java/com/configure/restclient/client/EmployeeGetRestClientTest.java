package com.configure.restclient.client;

import com.configure.restclient.client.resttemplate.DeleteEmployeeRestClient;
import com.configure.restclient.client.resttemplate.GetEmployeeRestClient;
import com.configure.restclient.client.resttemplate.PostEmployeeRestClient;
import com.configure.restclient.client.resttemplate.PutEmployeeRestClient;
import com.configure.restclient.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeGetRestClientTest {

    @Autowired
    private GetEmployeeRestClient employeeGetRestClient;

    @Autowired
    private PostEmployeeRestClient employeePostRestClient;

    @Autowired
    private PutEmployeeRestClient employeePutRestClient;

    @Autowired
    private DeleteEmployeeRestClient employeeDeleteRestClient;

    @Test
    void getForEntityTest() {
        Employee employee = employeeGetRestClient.getEmployeeById(123L);
        assertNotNull(employee);
        assertNotNull(employee);
        assertEquals("Sashank", employee.getFirstName());
    }

    @Test
    void getForEntityNotFoundTest() {
        Employee nonExistingEmployee = employeeGetRestClient.getEmployeeById(-123L);

        assertNotNull(nonExistingEmployee);
        assertNull(nonExistingEmployee.getId());
    }

    @Test
    void getAllTest() {
        List<Employee> employees = employeeGetRestClient.getAllEmployees(0, 3);

        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    void getForObjectTest() {
        Employee employeeObject = employeeGetRestClient.getEmployeeById(524L);

        assertNotNull(employeeObject);
        assertEquals("Rohan", employeeObject.getFirstName());
    }

    @Test
    void postForEntityTest() {
        Employee employeeToBeCreated = Employee.builder()
                .id(56789L)
                .firstName("Monalisa")
                .lastName("Samantaray")
                .yearlyIncome(5505000L)
                .build();

        Employee entity = employeePostRestClient.createNewEmployee(employeeToBeCreated);

        assertNotNull(entity);
        assertEquals("Monalisa", entity.getFirstName());
        assertEquals("Samantaray", entity.getLastName());
        assertEquals(5505000L, entity.getYearlyIncome());
    }

    @Test
    public void postForObjectTest() {
        Employee employeeToCreate = Employee.builder()
                .firstName("Daniel")
                .lastName("Thomson")
                .yearlyIncome(65000L)
                .build();

        Employee createdEmployee = employeePostRestClient.createNewEmployee(employeeToCreate);

        assertNotNull(createdEmployee);
        assertTrue(createdEmployee.getId() != 0);
        assertEquals("Daniel", createdEmployee.getFirstName());
        assertEquals("Thomson", createdEmployee.getLastName());
        assertEquals(65000, createdEmployee.getYearlyIncome());
    }

    @Test
    public void putTest() {
        Employee employeeEntity = employeeGetRestClient.getEmployeeById(4L);
        employeeEntity.setFirstName("Robb");
        employeeEntity.setLastName("Stark");
        employeeEntity.setYearlyIncome(99999L);

        employeePutRestClient.updateEmployee(employeeEntity, 4L);

        Employee updatedEmployee = employeeGetRestClient.getEmployeeById(4L);

        assertNotNull(updatedEmployee);
        assertEquals(4, updatedEmployee.getId());
        assertEquals("Rohan", updatedEmployee.getFirstName());
        assertEquals("Samantray", updatedEmployee.getLastName());
        assertEquals(350000, updatedEmployee.getYearlyIncome());
    }

    @Test
    public void deleteWithExchangeTest() {
        employeeDeleteRestClient.deleteEmployee(220L);
    }

}
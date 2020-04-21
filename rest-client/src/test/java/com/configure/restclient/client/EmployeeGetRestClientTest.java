package com.configure.restclient.client;

import com.configure.restclient.client.resttemplate.EmployeeDeleteRestClient;
import com.configure.restclient.client.resttemplate.EmployeeGetRestClient;
import com.configure.restclient.client.resttemplate.EmployeePostRestClient;
import com.configure.restclient.client.resttemplate.EmployeePutRestClient;
import com.configure.restclient.entity.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EmployeeGetRestClientTest {

    @Autowired
    private EmployeeGetRestClient employeeGetRestClient;

    @Autowired
    private EmployeePostRestClient employeePostRestClient;

    @Autowired
    private EmployeePutRestClient employeePutRestClient;

    @Autowired
    private EmployeeDeleteRestClient employeeDeleteRestClient;

    @Test
    void getForEntityTest() {
        EmployeeEntity employee = employeeGetRestClient.getEmployeeById(123L);
        assertNotNull(employee);
        assertNotNull(employee);
        assertEquals("Sashank", employee.getFirstName());
    }

    @Test
    void getForEntityNotFoundTest() {
        EmployeeEntity nonExistingEmployee = employeeGetRestClient.getEmployeeById(-123L);

        assertNotNull(nonExistingEmployee);
        assertNull(nonExistingEmployee.getId());
    }

    @Test
    @Sql({"/data.sql"})
    void getAllTest() {
        List<EmployeeEntity> employees = employeeGetRestClient.getAllEemployees(0, 3);

        assertNotNull(employees);
        assertEquals(3, employees.size());
        assertEquals("Rohan", employees.get(0).getFirstName());
        assertEquals("Sashank", employees.get(1).getFirstName());
        assertEquals("Sashank", employees.get(2).getFirstName());
    }

    @Test
    void getForObjectTest() {
        EmployeeEntity employeeObject = employeeGetRestClient.getEmployeeById(524L);

        assertNotNull(employeeObject);
        assertEquals("Rohan", employeeObject.getFirstName());
    }

    @Test
    void postForEntityTest() {
        EmployeeEntity employeeToBeCreated = EmployeeEntity.builder()
                .id(56789L)
                .firstName("Monalisa")
                .lastName("Samantaray")
                .yearlyIncome(5505000L)
                .build();

        EmployeeEntity entity = employeePostRestClient.createNewEmployee(employeeToBeCreated);

        assertNotNull(entity);
        assertTrue(entity.getId() != 0);
        assertEquals("Monalisa", entity.getFirstName());
        assertEquals("Samantaray", entity.getLastName());
        assertEquals(5505000L, entity.getYearlyIncome());
    }

    @Test
    public void postForObjectTest() {
        EmployeeEntity employeeToCreate = EmployeeEntity.builder()
                .firstName("Daniel")
                .lastName("Thomson")
                .yearlyIncome(65000L)
                .build();

        EmployeeEntity createdEmployee = employeePostRestClient.createNewEmployee(employeeToCreate);

        assertNotNull(createdEmployee);
        assertTrue(createdEmployee.getId() != 0);
        assertEquals("Daniel", createdEmployee.getFirstName());
        assertEquals("Thomson", createdEmployee.getLastName());
        assertEquals(65000, createdEmployee.getYearlyIncome());
    }

    @Test
    public void putTest() {
        EmployeeEntity employeeEntity = employeeGetRestClient.getEmployeeById(4L);
        employeeEntity.setFirstName("Robb");
        employeeEntity.setLastName("Stark");
        employeeEntity.setYearlyIncome(99999L);

        employeePutRestClient.updateEmployee(employeeEntity, 4L);

        EmployeeEntity updatedEmployee = employeeGetRestClient.getEmployeeById(4L);

        assertNotNull(updatedEmployee);
        assertEquals(4, updatedEmployee.getId());
        assertEquals("Robb", updatedEmployee.getFirstName());
        assertEquals("Stark", updatedEmployee.getLastName());
        assertEquals(99999, updatedEmployee.getYearlyIncome());
    }

    @Test
    public void deleteWithExchangeTest() {
        employeeDeleteRestClient.deleteEmployee(220L);
    }

}
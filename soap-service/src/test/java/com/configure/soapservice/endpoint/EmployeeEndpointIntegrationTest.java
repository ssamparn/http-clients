package com.configure.soapservice.endpoint;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.ws.test.server.MockWebServiceClient;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeEndpointIntegrationTest {

    private static final Map<String, String> NAMESPACES =
            Map.of("ns", "http://configure.com/soapservice/employees");

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void getEmployeeByIdReturnsEmployee() {
        MockWebServiceClient client = MockWebServiceClient.createClient(applicationContext);
        Source requestPayload = payload("""
                <ns:GetEmployeeByIdRequest xmlns:ns="http://configure.com/soapservice/employees">
                    <ns:id>1</ns:id>
                </ns:GetEmployeeByIdRequest>
                """);

        client.sendRequest(withPayload(requestPayload))
                .andExpect(noFault())
                .andExpect(xpath("//ns:employee/ns:firstName", NAMESPACES).evaluatesTo("Alice"))
                .andExpect(xpath("//ns:message", NAMESPACES).evaluatesTo("Employee found"));
    }

    @Test
    void createEmployeeReturnsCreatedEmployee() {
        MockWebServiceClient client = MockWebServiceClient.createClient(applicationContext);
        Source requestPayload = payload("""
                <ns:CreateEmployeeRequest xmlns:ns="http://configure.com/soapservice/employees">
                    <ns:employee>
                        <ns:firstName>John</ns:firstName>
                        <ns:lastName>Doe</ns:lastName>
                        <ns:department>QA</ns:department>
                        <ns:email>john.doe@sample.com</ns:email>
                    </ns:employee>
                </ns:CreateEmployeeRequest>
                """);

        client.sendRequest(withPayload(requestPayload))
                .andExpect(noFault())
                .andExpect(xpath("//ns:employee/ns:id", NAMESPACES).exists())
                .andExpect(xpath("//ns:employee/ns:firstName", NAMESPACES).evaluatesTo("John"))
                .andExpect(xpath("//ns:message", NAMESPACES).evaluatesTo("Employee created successfully"));
    }

    @Test
    void getAllEmployeesReturnsCollection() {
        MockWebServiceClient client = MockWebServiceClient.createClient(applicationContext);
        Source requestPayload = payload("""
                <ns:GetAllEmployeesRequest xmlns:ns="http://configure.com/soapservice/employees"/>
                """);

        client.sendRequest(withPayload(requestPayload))
                .andExpect(noFault())
                .andExpect(xpath("count(//ns:employees)", NAMESPACES).evaluatesTo("3"))
                .andExpect(xpath("//ns:count", NAMESPACES).evaluatesTo("3"))
                .andExpect(xpath("//ns:message", NAMESPACES).evaluatesTo("Employees fetched successfully"));
    }

    @Test
    void getEmployeeByIdReturnsNotFoundMessageWhenMissing() {
        MockWebServiceClient client = MockWebServiceClient.createClient(applicationContext);
        Source requestPayload = payload("""
                <ns:GetEmployeeByIdRequest xmlns:ns="http://configure.com/soapservice/employees">
                    <ns:id>999</ns:id>
                </ns:GetEmployeeByIdRequest>
                """);

        client.sendRequest(withPayload(requestPayload))
                .andExpect(noFault())
                .andExpect(xpath("count(//ns:employee)", NAMESPACES).evaluatesTo("0"))
                .andExpect(xpath("//ns:message", NAMESPACES).evaluatesTo("Employee not found for id: 999"));
    }

    private Source payload(String xml) {
        return new StreamSource(new java.io.StringReader(xml));
    }
}


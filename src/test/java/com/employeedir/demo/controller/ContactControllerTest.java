package com.employeedir.demo.controller;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.entity.Prospects;
import com.employeedir.demo.service.EmployeeService;
import com.employeedir.demo.service.ProspectService;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender mailSenderMock;

    @MockBean
    private ProspectService prospectServiceMock;

    @MockBean
    private EmployeeService employeeServiceMock;


    @Test
    void testViewContactForm() throws Exception {

        int employeeId = 1;

        Employee employee = new Employee("John", "Smith", "test123@gmail.com");

        when(employeeServiceMock.getEmployee(employeeId)).thenReturn(employee);

        Employee expectedEmployee = employeeServiceMock.getEmployee(employeeId);

        assertEquals(employee, expectedEmployee);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/contact/showContactForm")
                .param("employeeId", String.valueOf(employeeId));


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/employees/contact-form"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("employee", equalTo(employee)));

    }

    @Test
    void testSendEmployeeEmail() throws Exception {

        MockMultipartFile file = new MockMultipartFile("attachment", "test.jpg", "multipart/form-data", "test".getBytes());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/contact/sendEmail")
                .file("attachment", file.getBytes())
                .param("employeeId", "1")
                .param("firstName", "John")
                .param("lastName", "Smith")
                .param("email", "test123@gmail.com")
                .param("fullName", "John Smith")
                .param("subject", "test email")
                .param("content", "This is a test email");

        MimeMessage message = new MimeMessage((Session) null);

        when(mailSenderMock.createMimeMessage()).thenReturn(message);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/contact/showContactForm?employeeId=1"))
                .andExpect(flash().attributeExists("emailSuccess"));

        verify(mailSenderMock).send(message);

    }

    @Test
    void testShowProspectContactForm() throws Exception {

        Prospects prospect = new Prospects("John Smith", "test123@gmail.com");

        when(prospectServiceMock.getProspect(1)).thenReturn(prospect);

        Prospects actualProspect = prospectServiceMock.getProspect(1);

        assertEquals(prospect, actualProspect);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/contact/showProspectContactForm")
                .param("prospectId", "1");


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/prospects/prospect-contact-form"))
                .andExpect(model().attributeExists("prospect"));
    }

    @Test
    void testSendProspectEmail() throws Exception {

        MockMultipartFile file = new MockMultipartFile("attachment", "test.jpg", "multipart/form-data", "test".getBytes());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/contact/sendProspectEmail")
                .file("attachment", file.getBytes())
                .param("prospectId", "1")
                .param("email", "test123@gmail.com")
                .param("fullName", "John Smith")
                .param("subject", "test email")
                .param("content", "This is a test email");

        MimeMessage message = new MimeMessage((Session) null);

        when(mailSenderMock.createMimeMessage()).thenReturn(message);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/contact/showProspectContactForm?prospectId=1"))
                .andExpect(flash().attributeExists("emailSuccess"));

        verify(mailSenderMock).send(message);
    }
}
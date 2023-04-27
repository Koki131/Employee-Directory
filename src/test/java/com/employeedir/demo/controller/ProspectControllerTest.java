package com.employeedir.demo.controller;

import com.employeedir.demo.entity.Employee;
import com.employeedir.demo.entity.ProspectLinks;
import com.employeedir.demo.entity.Prospects;
import com.employeedir.demo.service.EmployeeService;
import com.employeedir.demo.service.ProspectLinkService;
import com.employeedir.demo.service.ProspectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProspectController.class)
class ProspectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProspectService prospectServiceMock;

    @MockBean
    private ProspectLinkService linksServiceMock;

    @MockBean
    private EmployeeService employeeServiceMock;



    @Test
    void testShowProspectList() throws Exception {

        int employeeId = 0;
        List<Prospects> expectedProspects = List.of(new Prospects("John Smith", "test123@gmail.com"));

        when(prospectServiceMock.findAllProspectsById(employeeId)).thenReturn(expectedProspects);


        List<Prospects> actualProspects = prospectServiceMock.findAllProspectsById(employeeId);

        assertEquals(expectedProspects, actualProspects);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/showProspectList")
                .param("employeeId", String.valueOf(employeeId));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/prospects/prospect-list"))
                .andExpect(model().attributeExists("prospects"));


    }

    @Test
    void testSearchProspectsWithNullKeyword() throws Exception {

        int employeeId = 0;
        String keyword = null;

        List<Prospects> expectedProspects = List.of(
                new Prospects("John Smith", "test123@gmail.com"),
                new Prospects("Jane Smith", "test123@gmail.com")
        );

        when(prospectServiceMock.findAllByKeyword(employeeId, keyword)).thenReturn(expectedProspects);

        List<Prospects> actualProspects = prospectServiceMock.findAllByKeyword(employeeId, keyword);

        assertEquals(expectedProspects, actualProspects);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/search")
                .param("keyword", keyword);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/showProspectList?employeeId=" + employeeId));

    }


    @Test
    void testSearchProspectsWithKeyword() throws Exception {
        int employeeId  = 0;
        String keyword = "John";

        List<Prospects> expectedProspects = List.of(
                new Prospects("John Smith", "test123@gmail.com")
        );

        when(prospectServiceMock.findAllByKeyword(employeeId, keyword)).thenReturn(expectedProspects);

        List<Prospects> actualProspects = prospectServiceMock.findAllByKeyword(employeeId, keyword);

        assertEquals(expectedProspects, actualProspects);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/search")
                .param("keyword", keyword);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/prospects/prospect-list"))
                .andExpect(model().attributeExists("prospects"))
                .andExpect(model().attributeExists("keyword"))
                .andExpect(model().attribute("prospects", equalTo(expectedProspects)))
                .andExpect(model().attribute("keyword", equalTo(keyword)));

    }

    @Test
    void showProspectForm() throws Exception {

        int employeeId = 0;
        List<Prospects> expectedProspects = List.of(new Prospects("John Smith", "test123@gmail.com"));


        when(prospectServiceMock.findAllProspectsById(employeeId)).thenReturn(expectedProspects);

        List<Prospects> actualProspects = prospectServiceMock.findAllProspectsById(employeeId);

        assertEquals(expectedProspects, actualProspects);

        mockMvc.perform(get("/prospectAddForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("/prospects/prospect-form"))
                .andExpect(model().attributeExists("prospect"))
                .andExpect(model().attributeExists("emails"));

    }

    @Test
    void testAddProspect() throws Exception {


        MockHttpServletRequestBuilder requestBuilder = post("/addProspect")
                .param("fullName", "John Smith")
                .param("email", "test123@gmail.com");

        Employee expectedEmployee = new Employee("John", "Smith", "test123@gmail.com");
        Prospects prospect = new Prospects("John Smith", "test123@gmail.com");
        prospect.setEmployee(expectedEmployee);

        when(employeeServiceMock.getEmployee(0)).thenReturn(expectedEmployee);

        Employee actualEmployee = employeeServiceMock.getEmployee(0);

        assertEquals(expectedEmployee, actualEmployee);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/showProspectList?employeeId=" + 0));


    }

    @Test
    void testDeleteProspect() throws Exception {


        int employeeId = 0;
        int prospectId = 1;

        prospectServiceMock.deleteProspect(prospectId);

        verify(prospectServiceMock).deleteProspect(prospectId);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/deleteProspect")
                .param("prospectId", String.valueOf(prospectId));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/showProspectList?employeeId=" + employeeId));

    }

    @Test
    void testShowProspectUpdateForm() throws Exception {



        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/showProspectUpdateForm")
                .param("prospectId", "1");

        Prospects expectedProspect = new Prospects("John Smith", "test123@gmail.com");

        when(prospectServiceMock.getProspect(1)).thenReturn(expectedProspect);

        Prospects actualProspect = prospectServiceMock.getProspect(1);

        assertEquals(expectedProspect, actualProspect);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/prospects/prospect-form"))
                .andExpect(model().attributeExists("prospect"));

    }

    @Test
    void testShowProspectLinks() throws Exception {

        int prospectId = 1;
        int linkId = 1;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/showProspectLinks")
                .param("prospectId", "1");

        Prospects expectedProspect = new Prospects("John Smith", "test123@gmail.com");
        expectedProspect.setId(prospectId);

        ProspectLinks links = new ProspectLinks("instagram.com", "facebook.com", "linkedin.com");
        links.setId(linkId);

        expectedProspect.setProspectLinks(links);


        when(prospectServiceMock.getProspect(1)).thenReturn(expectedProspect);
        when(linksServiceMock.getProspectLinks(expectedProspect.getProspectLinks().getId())).thenReturn(links);

        Prospects actualProspect = prospectServiceMock.getProspect(1);
        ProspectLinks actualLinks = linksServiceMock.getProspectLinks(actualProspect.getProspectLinks().getId());

        assertEquals(expectedProspect, actualProspect);
        assertEquals(links, actualLinks);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("/prospects/prospect-links"))
                .andExpect(model().attributeExists("prospectLinks"))
                .andExpect(model().attributeExists("facebook"))
                .andExpect(model().attributeExists("instagram"))
                .andExpect(model().attributeExists("linkedin"))
                .andExpect(model().attribute("prospectLinks", equalTo(links)))
                .andExpect(model().attribute("facebook", equalTo(links.getFacebook())))
                .andExpect(model().attribute("instagram", equalTo(links.getInstagram())))
                .andExpect(model().attribute("linkedin", equalTo(links.getLinkedIn())));
    }

    @Test
    void saveProspectLinks() throws Exception {

        int prospectId = 1;
        int linkId = 1;
        ProspectLinks links = new ProspectLinks("instagram.com", "facebook.com", "linkedin.com");
        Prospects prospect = new Prospects("John Smith", "test123@gmail.com");

        prospect.setId(prospectId);
        links.setId(linkId);
        prospect.setProspectLinks(links);
        links.setProspects(prospect);

        linksServiceMock.saveProspectLinks(links);

        when(prospectServiceMock.getProspect(prospectId)).thenReturn(prospect);
        when(linksServiceMock.getProspectLinks(prospect.getProspectLinks().getId())).thenReturn(links);


        mockMvc.perform(post("/saveProspectLinks"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/showProspectList?employeeId=0"));


        verify(linksServiceMock).saveProspectLinks(links);

    }
}



























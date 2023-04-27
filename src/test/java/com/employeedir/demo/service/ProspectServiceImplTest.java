package com.employeedir.demo.service;

import com.employeedir.demo.entity.ProspectLinks;
import com.employeedir.demo.entity.Prospects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class ProspectServiceImplTest {


    @Mock
    @Autowired
    private ProspectService prospectServiceMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllProspectsByIdTest() {

        int employeeId = 1;
        List<Prospects> expectedProspects = List.of(new Prospects("John Smith", "test123@gmail.com"));

        when(prospectServiceMock.findAllProspectsById(employeeId)).thenReturn(expectedProspects);

        List<Prospects> actualProspects = prospectServiceMock.findAllProspectsById(employeeId);

        verify(prospectServiceMock).findAllProspectsById(employeeId);
        verifyNoMoreInteractions(prospectServiceMock);
        assertEquals(expectedProspects, actualProspects);

    }

    @Test
    void saveProspectTest() {

        Prospects prospectToSave = new Prospects("John Smith", "test123@gmail.com");

        prospectServiceMock.saveProspect(prospectToSave);

        verify(prospectServiceMock).saveProspect(prospectToSave);
        verifyNoMoreInteractions(prospectServiceMock);

    }

    @Test
    void getProspectTest() {

        int prospectId = 1;
        Prospects expectedProspect = new Prospects("John Smith", "test123@gmail.com");

        when(prospectServiceMock.getProspect(prospectId)).thenReturn(expectedProspect);

        Prospects actualProspect = prospectServiceMock.getProspect(prospectId);

        verify(prospectServiceMock).getProspect(prospectId);
        verifyNoMoreInteractions(prospectServiceMock);
        assertEquals(expectedProspect, actualProspect);

    }

    @Test
    void deleteProspectTest() {

        int prospectId = 1;

        prospectServiceMock.deleteProspect(prospectId);

        verify(prospectServiceMock).deleteProspect(prospectId);
        verifyNoMoreInteractions(prospectServiceMock);

    }

    @Test
    void findProspectsWithKeywordTest() {

        int employeeId = 1;
        String keyword = "Smith";

        List<Prospects> expectedProspects = List.of(
                new Prospects("John Smith", "test123@gmail.com"),
                new Prospects("Jane Smith", "test123@gmail.com")
        );

        when(prospectServiceMock.findAllByKeyword(employeeId, keyword)).thenReturn(expectedProspects);

        List<Prospects> actualProspects = prospectServiceMock.findAllByKeyword(employeeId, keyword);

        verify(prospectServiceMock).findAllByKeyword(employeeId, keyword);
        verifyNoMoreInteractions(prospectServiceMock);
        assertEquals(expectedProspects, actualProspects);

    }

    @Test
    void findProspectsWithoutKeyword() {

        int employeeId = 1;
        String keyword = null;

        List<Prospects> expectedProspects = List.of(
                new Prospects("John Smith", "test123@gmail.com"),
                new Prospects("Jane Smith", "test123@gmail.com"),
                new Prospects("Jason Bourne", "test123@gmail.com")
        );

        when(prospectServiceMock.findAllByKeyword(employeeId, keyword)).thenReturn(expectedProspects);

        List<Prospects> actualProspects = prospectServiceMock.findAllByKeyword(employeeId, keyword);

        verify(prospectServiceMock).findAllByKeyword(employeeId, keyword);
        verifyNoMoreInteractions(prospectServiceMock);
        assertEquals(expectedProspects, actualProspects);

    }


}
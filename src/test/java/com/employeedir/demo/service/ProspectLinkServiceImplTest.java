package com.employeedir.demo.service;

import com.employeedir.demo.entity.ProspectLinks;
import com.employeedir.demo.entity.Prospects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class ProspectLinkServiceImplTest {


    @Mock
    @Autowired
    private ProspectLinkService prospectLinkServiceMock;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveProspectLinks() {

        String instagram = "instagram.com";
        String linkedin = "linkedin.com";
        String facebook = "facebook.com";

        ProspectLinks prospectLinks = new ProspectLinks(linkedin, instagram, facebook);

        prospectLinkServiceMock.saveProspectLinks(prospectLinks);

        verify(prospectLinkServiceMock).saveProspectLinks(prospectLinks);
        verifyNoMoreInteractions(prospectLinkServiceMock);

    }

    @Test
    void getProspectLinks() {

        String instagram = "instagram.com";
        String linkedin = "linkedin.com";
        String facebook = "facebook.com";


        int linkId = 1;
        ProspectLinks expectedLinks = new ProspectLinks(linkedin, instagram, facebook);

        when(prospectLinkServiceMock.getProspectLinks(linkId)).thenReturn(expectedLinks);

        ProspectLinks actualLinks = prospectLinkServiceMock.getProspectLinks(linkId);

        verify(prospectLinkServiceMock).getProspectLinks(linkId);
        verifyNoMoreInteractions(prospectLinkServiceMock);
        assertEquals(expectedLinks, actualLinks);

    }

    @Test
    void deleteProspectLinks() {

        int linkId = 1;

        prospectLinkServiceMock.deleteProspectLinks(linkId);

        verify(prospectLinkServiceMock).deleteProspectLinks(linkId);
        verifyNoMoreInteractions(prospectLinkServiceMock);

    }
}
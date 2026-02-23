package rva.integrationTesting;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import rva.models.Sud;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SudControllerIntegrationTest {

    @Autowired
    TestRestTemplate template;

    int getHighestId() {
        int highestId = 0;
        ResponseEntity<List<Sud>> response =
                template.exchange("/sudovi", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Sud>>() {});
        if (response.getBody().isEmpty()) return highestId;
        for (Sud s : response.getBody()) {
            if (highestId < s.getId()) highestId = s.getId();
        }
        return highestId;
    }

    @Test
    @Order(1)
    void testGetAllSudovi() {
        ResponseEntity<List<Sud>> response =
                template.exchange("/sudovi", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Sud>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
    }

    @Test
    @Order(2)
    void testGetSudByNaziv() {
        String naziv = "sud";
        ResponseEntity<List<Sud>> response =
                template.exchange("/sud/naziv/" + naziv, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Sud>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
    }

    @Test
    @Order(3)
    void testGetSudById() {
        int id = 1;
        ResponseEntity<Sud> response =
                template.exchange("/sud/id/" + id, HttpMethod.GET, null, Sud.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    @Order(4)
    void testGetSudById_notFound() {
        ResponseEntity<String> response =
                template.exchange("/sud/id/99999", HttpMethod.GET, null, String.class);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Resource with id: 99999 does not exist", response.getBody());
    }

    @Test
    @Order(5)
    void testCreateSud() {
        Sud sud = new Sud();
        sud.setNaziv("Novi sud");
        sud.setAdresa("Ulica 1");

        HttpEntity<Sud> entity = new HttpEntity<>(sud);
        ResponseEntity<Sud> response =
                template.exchange("/sud", HttpMethod.POST, entity, Sud.class);
        int highestId = getHighestId();

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/sud/id/" + highestId, response.getHeaders().getLocation().toString());
        assertEquals(sud.getNaziv(), response.getBody().getNaziv());
        assertEquals(sud.getAdresa(), response.getBody().getAdresa());
    }

    @Test
    @Order(6)
    void testUpdateSud() {
        Sud sud = new Sud();
        sud.setNaziv("Izmenjen sud");
        sud.setAdresa("Izmenjena adresa");

        int highestId = getHighestId();
        HttpEntity<Sud> entity = new HttpEntity<>(sud);
        ResponseEntity<Sud> response =
                template.exchange("/sud/" + highestId, HttpMethod.PUT, entity, Sud.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(sud.getNaziv(), response.getBody().getNaziv());
        assertEquals(sud.getAdresa(), response.getBody().getAdresa());
    }

    @Test
    @Order(7)
    void testDeleteSud() {
        int highestId = getHighestId();
        ResponseEntity<String> response =
                template.exchange("/sud/" + highestId, HttpMethod.DELETE, null, String.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(String.format("Entity with id: %s has been deleted", highestId), response.getBody());

        ResponseEntity<String> responseGet =
                template.exchange("/sud/id/" + highestId, HttpMethod.GET, null, String.class);

        assertEquals(404, responseGet.getStatusCode().value());
        assertEquals(String.format("Resource with id: %s does not exist", highestId), responseGet.getBody());
    }
}
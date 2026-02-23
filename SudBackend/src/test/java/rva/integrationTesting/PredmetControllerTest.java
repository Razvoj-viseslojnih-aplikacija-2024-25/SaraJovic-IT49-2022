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

import rva.models.Predmet;
import rva.models.Sud;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PredmetControllerIntegrationTest {

    @Autowired
    TestRestTemplate template;

    int getHighestId() {
        int highestId = 0;
        ResponseEntity<List<Predmet>> response =
                template.exchange("/predmeti", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Predmet>>() {});
        if (response.getBody().isEmpty()) return highestId;
        for (Predmet p : response.getBody()) {
            if (highestId < p.getId()) highestId = p.getId();
        }
        return highestId;
    }

    @Test
    @Order(1)
    void testGetAllPredmeti() {
        ResponseEntity<List<Predmet>> response =
                template.exchange("/predmeti", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Predmet>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
    }

    @Test
    @Order(2)
    void testGetPredmetById() {
        int id = 1;
        ResponseEntity<Predmet> response =
                template.exchange("/predmet/id/" + id, HttpMethod.GET, null, Predmet.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    @Order(3)
    void testGetPredmetById_notFound() {
        ResponseEntity<String> response =
                template.exchange("/predmet/id/99999", HttpMethod.GET, null, String.class);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Resource with id: 99999 does not exist", response.getBody());
    }

    @Test
    @Order(4)
    void testGetPredmetiByBrojPredmeta() {
        ResponseEntity<List<Predmet>> response =
                template.exchange("/predmet/brojPredmeta/P", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Predmet>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
    }

    @Test
    @Order(5)
    void testGetPredmetiByAktivan() {
        ResponseEntity<List<Predmet>> response =
                template.exchange("/predmet/aktivan/true", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Predmet>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
        for (Predmet p : response.getBody()) {
            assertTrue(p.getAktivan());
        }
    }

    @Test
    @Order(6)
    void testGetPredmetiBySud() {
        int sudId = 1;
        ResponseEntity<List<Predmet>> response =
                template.exchange("/predmet/sud/" + sudId, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Predmet>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
        for (Predmet p : response.getBody()) {
            assertEquals(sudId, p.getSud().getId());
        }
    }

    @Test
    @Order(7)
    void testCreatePredmet() {
        Sud sud = new Sud();
        sud.setId(1);

        Predmet predmet = new Predmet();
        predmet.setBrojPredmeta("P-TEST-2025");
        predmet.setOpis("Test opis");
        predmet.setAktivan(true);
        predmet.setSud(sud);

        HttpEntity<Predmet> entity = new HttpEntity<>(predmet);
        ResponseEntity<Predmet> response =
                template.exchange("/predmet", HttpMethod.POST, entity, Predmet.class);
        int highestId = getHighestId();

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/predmet/id/" + highestId, response.getHeaders().getLocation().toString());
        assertEquals(predmet.getBrojPredmeta(), response.getBody().getBrojPredmeta());
        assertEquals(predmet.getAktivan(), response.getBody().getAktivan());
    }

    @Test
    @Order(8)
    void testUpdatePredmet() {
        Predmet predmet = new Predmet();
        predmet.setBrojPredmeta("P-UPDATED");
        predmet.setOpis("Izmenjen opis");
        predmet.setAktivan(false);

        int highestId = getHighestId();
        HttpEntity<Predmet> entity = new HttpEntity<>(predmet);
        ResponseEntity<Predmet> response =
                template.exchange("/predmet/" + highestId, HttpMethod.PUT, entity, Predmet.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(predmet.getBrojPredmeta(), response.getBody().getBrojPredmeta());
        assertEquals(predmet.getAktivan(), response.getBody().getAktivan());
    }

    @Test
    @Order(9)
    void testDeletePredmet() {
        int highestId = getHighestId();
        ResponseEntity<String> response =
                template.exchange("/predmet/" + highestId, HttpMethod.DELETE, null, String.class);

        assertEquals(200, response.getStatusCode().value());

        ResponseEntity<String> responseGet =
                template.exchange("/predmet/id/" + highestId, HttpMethod.GET, null, String.class);

        assertEquals(404, responseGet.getStatusCode().value());
        assertEquals(String.format("Resource with id: %s does not exist", highestId), responseGet.getBody());
    }
}
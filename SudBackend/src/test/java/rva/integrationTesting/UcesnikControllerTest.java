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

import rva.models.Ucesnik;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UcesnikControllerIntegrationTest {

    @Autowired
    TestRestTemplate template;

    int getHighestId() {
        int highestId = 0;
        ResponseEntity<List<Ucesnik>> response =
                template.exchange("/ucesnici", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Ucesnik>>() {});
        if (response.getBody().isEmpty()) return highestId;
        for (Ucesnik u : response.getBody()) {
            if (highestId < u.getId()) highestId = u.getId();
        }
        return highestId;
    }

    @Test
    @Order(1)
    void testGetAllUcesnici() {
        ResponseEntity<List<Ucesnik>> response =
                template.exchange("/ucesnici", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Ucesnik>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
    }

    @Test
    @Order(2)
    void testGetUcesniciByStatus() {
        String status = "Tužilac";
        ResponseEntity<List<Ucesnik>> response =
                template.exchange("/ucesnik/status/" + status, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Ucesnik>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
        for (Ucesnik u : response.getBody()) {
            assertEquals(status, u.getStatus());
        }
    }

    @Test
    @Order(3)
    void testGetUcesnikById() {
        int id = 1;
        ResponseEntity<Ucesnik> response =
                template.exchange("/ucesnik/id/" + id, HttpMethod.GET, null, Ucesnik.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    @Order(4)
    void testGetUcesnikById_notFound() {
        ResponseEntity<String> response =
                template.exchange("/ucesnik/id/99999", HttpMethod.GET, null, String.class);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Resource with id: 99999 does not exist", response.getBody());
    }

    @Test
    @Order(5)
    void testGetUcesniciByMbr() {
        String mbr = "1234567890123";
        ResponseEntity<List<Ucesnik>> response =
                template.exchange("/ucesnik/mbr/" + mbr, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Ucesnik>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
    }

    @Test
    @Order(6)
    void testCreateUcesnik() {
        Ucesnik ucesnik = new Ucesnik();
        ucesnik.setIme("Petar");
        ucesnik.setPrezime("Petrović");
        ucesnik.setMbr("9876543210987");
        ucesnik.setStatus("Svedok");

        HttpEntity<Ucesnik> entity = new HttpEntity<>(ucesnik);
        ResponseEntity<Ucesnik> response =
                template.exchange("/ucesnik", HttpMethod.POST, entity, Ucesnik.class);
        int highestId = getHighestId();

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/ucesnik/id/" + highestId, response.getHeaders().getLocation().toString());
        assertEquals(ucesnik.getIme(), response.getBody().getIme());
        assertEquals(ucesnik.getMbr(), response.getBody().getMbr());
    }

    @Test
    @Order(7)
    void testUpdateUcesnik() {
        Ucesnik ucesnik = new Ucesnik();
        ucesnik.setIme("Izmenjen");
        ucesnik.setPrezime("Prezime");
        ucesnik.setMbr("1111111111111");
        ucesnik.setStatus("Okrivljeni");

        int highestId = getHighestId();
        HttpEntity<Ucesnik> entity = new HttpEntity<>(ucesnik);
        ResponseEntity<Ucesnik> response =
                template.exchange("/ucesnik/" + highestId, HttpMethod.PUT, entity, Ucesnik.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(ucesnik.getIme(), response.getBody().getIme());
        assertEquals(ucesnik.getStatus(), response.getBody().getStatus());
    }

    @Test
    @Order(8)
    void testDeleteUcesnik() {
        int highestId = getHighestId();
        ResponseEntity<String> response =
                template.exchange("/ucesnik/" + highestId, HttpMethod.DELETE, null, String.class);

        assertEquals(200, response.getStatusCode().value());

        ResponseEntity<String> responseGet =
                template.exchange("/ucesnik/id/" + highestId, HttpMethod.GET, null, String.class);

        assertEquals(404, responseGet.getStatusCode().value());
        assertEquals(String.format("Resource with id: %s does not exist", highestId), responseGet.getBody());
    }
}
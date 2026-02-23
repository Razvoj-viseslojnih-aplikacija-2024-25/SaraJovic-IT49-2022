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
import rva.models.Rociste;
import rva.models.Ucesnik;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RocisteControllerIntegrationTest {

    @Autowired
    TestRestTemplate template;

    int getHighestId() {
        int highestId = 0;
        ResponseEntity<List<Rociste>> response =
                template.exchange("/rocista", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Rociste>>() {});
        if (response.getBody().isEmpty()) return highestId;
        for (Rociste r : response.getBody()) {
            if (highestId < r.getId()) highestId = r.getId();
        }
        return highestId;
    }

    @Test
    @Order(1)
    void testGetAllRocista() {
        ResponseEntity<List<Rociste>> response =
                template.exchange("/rocista", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Rociste>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
    }

    @Test
    @Order(2)
    void testGetRocisteById() {
        int id = 1;
        ResponseEntity<Rociste> response =
                template.exchange("/rociste/id/" + id, HttpMethod.GET, null, Rociste.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    @Order(3)
    void testGetRocisteById_notFound() {
        ResponseEntity<String> response =
                template.exchange("/rociste/id/99999", HttpMethod.GET, null, String.class);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Resource with id: 99999 does not exist", response.getBody());
    }

    @Test
    @Order(4)
    void testGetRocistaBySudnica() {
        String sudnica = "1";
        ResponseEntity<List<Rociste>> response =
                template.exchange("/rociste/sudnica/" + sudnica, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Rociste>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
    }

    @Test
    @Order(5)
    void testGetRocistaByPredmet() {
        int predmetId = 1;
        ResponseEntity<List<Rociste>> response =
                template.exchange("/rociste/predmet/" + predmetId, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Rociste>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
        for (Rociste r : response.getBody()) {
            assertEquals(predmetId, r.getPredmet().getId());
        }
    }

    @Test
    @Order(6)
    void testGetRocistaByUcesnik() {
        int ucesnikId = 1;
        ResponseEntity<List<Rociste>> response =
                template.exchange("/rociste/ucesnik/" + ucesnikId, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Rociste>>() {});

        assertEquals(200, response.getStatusCode().value());
        assertTrue(!response.getBody().isEmpty());
        for (Rociste r : response.getBody()) {
            assertEquals(ucesnikId, r.getUcesnik().getId());
        }
    }

    @Test
    @Order(7)
    void testCreateRociste() {
        Predmet predmet = new Predmet();
        predmet.setId(1);

        Ucesnik ucesnik = new Ucesnik();
        ucesnik.setId(1);

        Rociste rociste = new Rociste();
        rociste.setSudnica("Sudnica 5");
        rociste.setPredmet(predmet);
        rociste.setUcesnik(ucesnik);

        HttpEntity<Rociste> entity = new HttpEntity<>(rociste);
        ResponseEntity<Rociste> response =
                template.exchange("/rociste", HttpMethod.POST, entity, Rociste.class);
        int highestId = getHighestId();

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/rociste/id/" + highestId, response.getHeaders().getLocation().toString());
        assertEquals(rociste.getSudnica(), response.getBody().getSudnica());
    }

    @Test
    @Order(8)
    void testUpdateRociste() {
        Rociste rociste = new Rociste();
        rociste.setSudnica("Izmenjena sudnica");

        int highestId = getHighestId();
        HttpEntity<Rociste> entity = new HttpEntity<>(rociste);
        ResponseEntity<Rociste> response =
                template.exchange("/rociste/" + highestId, HttpMethod.PUT, entity, Rociste.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(rociste.getSudnica(), response.getBody().getSudnica());
    }

    @Test
    @Order(9)
    void testDeleteRociste() {
        int highestId = getHighestId();
        ResponseEntity<String> response =
                template.exchange("/rociste/" + highestId, HttpMethod.DELETE, null, String.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(String.format("Entity with id: %s has been deleted", highestId), response.getBody());

        ResponseEntity<String> responseGet =
                template.exchange("/rociste/id/" + highestId, HttpMethod.GET, null, String.class);

        assertEquals(404, responseGet.getStatusCode().value());
        assertEquals(String.format("Resource with id: %s does not exist", highestId), responseGet.getBody());
    }
}
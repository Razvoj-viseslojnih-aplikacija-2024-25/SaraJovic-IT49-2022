package rva.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rva.implementation.UcesnikServiceImp;
import rva.models.Predmet;
import rva.models.Ucesnik;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class UcesnikController {

    @Autowired
    private UcesnikServiceImp service;

    @GetMapping("/ucesnici")
    public ResponseEntity<?> getAllUcesnici(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/ucesnik/mbr/{mbr}")
    public ResponseEntity<?> getUcesniciByMbr(@PathVariable String mbr){
        List<Ucesnik> ucesnici = service.getByMbr(mbr);
        if(ucesnici.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No participants found with MBR: %s", mbr),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(ucesnici);
    }

    @GetMapping("/ucesnik/status/{status}")
    public ResponseEntity<?> getUcesniciByStatus(@PathVariable String status){
        List<Ucesnik> ucesnici = service.getByStatus(status);
        if(ucesnici.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No participants found with status: %s", status),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(ucesnici);
    }

    @GetMapping("/ucesnik/id/{id}")
    public ResponseEntity<?> getUcesnikById(@PathVariable int id){
        Optional<Ucesnik> ucesnik = service.findById(id);
        if(ucesnik.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Resource with id: %s does not exist", id),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(ucesnik.get());
    }
    @PostMapping("/ucesnik")
    public ResponseEntity<?> createUcesnik(@RequestBody Ucesnik ucesnik){
        try{
            Ucesnik createdUcesnik = service.create(ucesnik);
            URI uri = URI.create("/ucesnik/id/" + createdUcesnik.getId());
            return ResponseEntity.created(uri).body(createdUcesnik);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating participiant: " + e.getMessage());
        }

    }

    @PutMapping("/ucesnik/{id}")
    public ResponseEntity<?> updateUcesnik(@PathVariable int id, @RequestBody Ucesnik ucesnik){
        Optional<Ucesnik> updatedUcesnik = service.update(ucesnik, id);
        if(updatedUcesnik.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Entity with id: %s doesnt exist", id),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(updatedUcesnik.get());
    }

    @DeleteMapping("/ucesnik/{id}")
    public ResponseEntity<?> deleteUcesnik(@PathVariable int id){
        try {
            if (!service.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Ucesnik sa ID: " + id + " ne postoji");
            }

            // Proveri da li predmet ima ročišta
            Optional<Ucesnik> ucesnikOpt = service.findById(id);
            if (ucesnikOpt.isPresent()) {
                Ucesnik ucesnik = ucesnikOpt.get();
                if (ucesnik.getRocista() != null && !ucesnik.getRocista().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(String.format(
                                    "Cannot delete participiant'%s' due to %d linked hearings. " +
                                            "First delete all hearings for this participant.",
                                    ucesnik.getIme()+ucesnik.getPrezime(),
                                    ucesnik.getRocista().size()
                            ));
                }
            }
            service.delete(id);
            return ResponseEntity.ok("Participiant with ID: " + id + "deleted.");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete this paticipiant due to linked hearings.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting participant: " + e.getMessage());
        }
    }
}

package rva.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rva.implementation.SudServiceImp;
import rva.models.Predmet;
import rva.models.Sud;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class SudController {

    @Autowired
    private SudServiceImp sudService;

    @GetMapping("/sudovi")
    public ResponseEntity<?> getAllSudovi(){
        return ResponseEntity.ok(sudService.getAll());
    }

    @GetMapping("/sud/naziv/{naziv}")
    public ResponseEntity<?> getSudoviByNaziv(@PathVariable String naziv){
        List<Sud> sudovi = sudService.getByNaziv(naziv);
        if(sudovi.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No courts found with name containing: %s", naziv),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(sudovi);
    }

    @GetMapping("/sud/id/{id}")
    public ResponseEntity<?> getSudById(@PathVariable int id){
        Optional<Sud> sud = sudService.findById(id);
        if(sud.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Resource with id: %s does not exist", id),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(sud.get());
    }

    @PostMapping("/sud")
    public ResponseEntity<?> createSud(@RequestBody Sud sud){
        try
        {
            Sud createdSud = sudService.create(sud);
            URI uri = URI.create("/sud/id/" + createdSud.getId());
            return ResponseEntity.created(uri).body(createdSud);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating court: " + e.getMessage());
        }

    }

    @PutMapping("/sud/{id}")
    public ResponseEntity<?> updateSud(@PathVariable int id, @RequestBody Sud sud){
        Optional<Sud> updatedSud = sudService.update(sud, id);
        if(updatedSud.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Entity with id: %s doesnt exist", id),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(updatedSud.get());
    }

    @DeleteMapping("/sud/{id}")
    public ResponseEntity<?> deleteSud(@PathVariable int id){
        try {
            if (!sudService.existsById(id))
                return new ResponseEntity<String>(
                        String.format("Entity with id: %s doesnt exist", id),
                        HttpStatus.NOT_FOUND
                );
            Optional<Sud> sudOpt = sudService.findById(id);
            if (sudOpt.isPresent()) {
                Sud sud = sudOpt.get();
                if (sud.getPredmeti() != null && !sud.getPredmeti().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(String.format(
                                    "Cannot delete court '%s' due to %d linked cases. " +
                                            "First delete all cases for this court.",
                                    sud.getNaziv(),
                                    sud.getPredmeti().size()
                            ));
                }
            }
            sudService.delete(id);
            return ResponseEntity.ok(String.format("Entity with id: %s has been deleted", id));
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete this court due to linked cases.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }

    }
}

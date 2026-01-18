package rva.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rva.implementation.PredmetServiceImp;
import rva.implementation.SudServiceImp;
import rva.models.Predmet;
import rva.models.Sud;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class PredmetController {

    @Autowired
    private PredmetServiceImp service;

    @Autowired
    private SudServiceImp sudService;

    @GetMapping("/predmeti")
    public ResponseEntity<?> getAllPredmeti(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/predmet/brojPredmeta/{brojPredmeta}")
    public ResponseEntity<?> getPredmetiByBrojPredmeta(@PathVariable String brojPredmeta){
        List<Predmet> predmeti = service.getByBrojPredmeta(brojPredmeta);
        if(predmeti.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No cases found with case number containing: %s", brojPredmeta),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(predmeti);
    }

    @GetMapping("/predmet/datum/{datum}")
    public ResponseEntity<?> getPredmetiByDatum(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate datum){
        List<Predmet> predmeti = service.getByDatumPocetka(datum);
        if(predmeti.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No cases found with start date: %s", datum),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(predmeti);
    }

    @GetMapping("/predmet/aktivan/{aktivan}")
    public ResponseEntity<?> getPredmetiByAktivan(@PathVariable Boolean aktivan){
        List<Predmet> predmeti = service.getByAktivan(aktivan);
        if(predmeti.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No %s cases found", aktivan ? "active" : "inactive"),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(predmeti);
    }
    @GetMapping("/predmet/id/{id}")
    public ResponseEntity<?> getPredmetById(@PathVariable int id){
        Optional<Predmet> predmet = service.findById(id);
        if(predmet.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Resource with id: %s does not exist", id),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(predmet.get());
    }

    @PostMapping("/predmet")
    public ResponseEntity<?> createPredmet(@RequestBody Predmet predmet){
        try {
            Predmet createdPredmet = service.create(predmet);
            URI uri = URI.create("/predmet/id/" + createdPredmet.getId());
            return ResponseEntity.created(uri).body(createdPredmet);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating case: " + e.getMessage());
        }


    }

    @PutMapping("/predmet/{id}")
    public ResponseEntity<?> updatePredmet(@PathVariable int id, @RequestBody Predmet predmet){
        Optional<Predmet> updatedPredmet = service.update(predmet, id);
        if(updatedPredmet.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Entity with id: %s doesnt exist", id),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(updatedPredmet.get());
    }

    @DeleteMapping("/predmet/{id}")
    public ResponseEntity<?> deletePredmet(@PathVariable int id){
        try {
            if (!service.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Case with ID " + id + "does not exist");
            }
            Optional<Predmet> predmetOpt = service.findById(id);
            if (predmetOpt.isPresent()) {
                Predmet predmet = predmetOpt.get();
                if (predmet.getRocista() != null && !predmet.getRocista().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(String.format(
                                    "Cannot delete case with case number: '%s' due to '%d' linked hearings.",
                                            "First delete all hearings for this case.",
                                    predmet.getBrojPredmeta(),
                                    predmet.getRocista().size()
                            ));
                }
            }
            service.delete(id);
            return ResponseEntity.ok("Case with ID: " + id + "deleted.");
    }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete this case due to linked hearings.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error:  " + e.getMessage());
        }
    }

    @GetMapping("/predmet/sud/{foreignKey}")
    public ResponseEntity<?> getPredmetiBySud(@PathVariable int foreignKey){
        Optional<Sud> sud = sudService.findById(foreignKey);
        if(sud.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Courtroom with id: %s doesnt exist", foreignKey),
                    HttpStatus.NOT_FOUND
            );
        List<Predmet> predmeti = service.getBySud(sud.get());
        if(predmeti.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No cases found for court with id: %s", foreignKey),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(predmeti);
    }
}

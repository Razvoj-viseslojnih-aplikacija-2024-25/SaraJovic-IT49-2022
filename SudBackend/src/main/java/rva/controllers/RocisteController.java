package rva.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rva.implementation.PredmetServiceImp;
import rva.implementation.RocisteServiceImp;
import rva.implementation.UcesnikServiceImp;
import rva.models.Predmet;
import rva.models.Rociste;
import rva.models.Ucesnik;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class RocisteController {

    @Autowired
    private RocisteServiceImp service;

    @Autowired
    private PredmetServiceImp predmetService;

    @Autowired
    private UcesnikServiceImp ucesnikService;

    @GetMapping("/rocista")
    public ResponseEntity<?> getAllRocista(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/rociste/sudnica/{sudnica}")
    public ResponseEntity<?> getRocistaoBySudnica(@PathVariable String sudnica){
        List<Rociste> rocista = service.getBySudnica(sudnica);
        if(rocista.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No hearings found in courtroom: %s", sudnica),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(rocista);
    }

    @GetMapping("/rociste/id/{id}")
    public ResponseEntity<?> getRocisteById(@PathVariable int id){
        Optional<Rociste> rociste = service.findById(id);
        if(rociste.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Resource with id: %s does not exist", id),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(rociste.get());
    }

    @PostMapping("/rociste")
    public ResponseEntity<?> createRociste(@RequestBody Rociste rociste){
        try{
            Rociste createdRociste = service.create(rociste);
            URI uri = URI.create("/rociste/id/" + createdRociste.getId());
            return ResponseEntity.created(uri).body(createdRociste);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating hearing: " + e.getMessage());
        }

    }

    @PutMapping("/rociste/{id}")
    public ResponseEntity<?> updateRociste(@PathVariable int id, @RequestBody Rociste rociste){
        Optional<Rociste> updatedRociste = service.update(rociste, id);
        if(updatedRociste.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Entity with id: %s doesnt exist", id),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(updatedRociste.get());
    }

    @DeleteMapping("/rociste/{id}")
    public ResponseEntity<?> deleteRociste(@PathVariable int id){
        if(!service.existsById(id))
            return new ResponseEntity<String>(
                    String.format("Entity with id: %s doesnt exist", id),
                    HttpStatus.NOT_FOUND
            );
        service.delete(id);
        return ResponseEntity.ok(String.format("Entity with id: %s has been deleted", id));
    }

    @GetMapping("/rociste/predmet/{foreignKey}")
    public ResponseEntity<?> getRocistaByPredmet(@PathVariable int foreignKey){
        Optional<Predmet> predmet = predmetService.findById(foreignKey);
        if(predmet.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Case with id: %s doesnt exist", foreignKey),
                    HttpStatus.NOT_FOUND
            );
        List<Rociste> rocista = service.getByPredmet(predmet.get());
        if(rocista.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No hearings found for case with id: %s", foreignKey),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(rocista);
    }

    @GetMapping("/rociste/ucesnik/{foreignKey}")
    public ResponseEntity<?> getRocistaByUcesnik(@PathVariable int foreignKey){
        Optional<Ucesnik> ucesnik = ucesnikService.findById(foreignKey);
        if(ucesnik.isEmpty())
            return new ResponseEntity<String>(
                    String.format("Participant with id: %s doesnt exist", foreignKey),
                    HttpStatus.NOT_FOUND
            );
        List<Rociste> rocista = service.getByUcesnik(ucesnik.get());
        if(rocista.isEmpty())
            return new ResponseEntity<String>(
                    String.format("No hearings found for participant with id: %s", foreignKey),
                    HttpStatus.NOT_FOUND
            );
        return ResponseEntity.ok(rocista);
    }
}

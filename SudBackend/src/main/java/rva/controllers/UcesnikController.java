package rva.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rva.implementation.UcesnikServiceImp;
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
        if(service.existsById(ucesnik.getId()))
            return new ResponseEntity<String>(
                    String.format("Entity with id: %s already exists", ucesnik.getId()),
                    HttpStatus.CONFLICT
            );
        Ucesnik createdUcesnik = service.create(ucesnik);
        URI uri = URI.create("/ucesnik/id/" + createdUcesnik.getId());
        return ResponseEntity.created(uri).body(createdUcesnik);
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
        if(!service.existsById(id))
            return new ResponseEntity<String>(
                    String.format("Entity with id: %s doesnt exist", id),
                    HttpStatus.NOT_FOUND
            );
        service.delete(id);
        return ResponseEntity.ok(String.format("Entity with id: %s has been deleted", id));
    }
}

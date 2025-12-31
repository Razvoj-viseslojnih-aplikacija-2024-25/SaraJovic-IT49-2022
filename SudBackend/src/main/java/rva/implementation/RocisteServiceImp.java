package rva.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rva.models.Predmet;
import rva.models.Rociste;
import rva.models.Ucesnik;
import rva.repository.RocisteRepository;
import rva.services.PredmetService;
import rva.services.RocisteService;

import java.util.List;
import java.util.Optional;

@Component
public class RocisteServiceImp implements RocisteService {

    @Autowired
    private RocisteRepository repo;

    @Override
    public List<Rociste> getBySudnica(String sudnica) {
        return repo.findBySudnicaContainingIgnoreCase(sudnica);
    }

    @Override
    public List<Rociste> getByPredmet(Predmet predmet) {
        return repo.findByPredmet(predmet);
    }

    @Override
    public List<Rociste> getByUcesnik(Ucesnik ucesnik) {
        return repo.findByUcesnik(ucesnik)  ;
    }

    @Override
    public List<Rociste> getAll() {
        return repo.findAll();
    }

    @Override
    public boolean existsById(int id) {
        return repo.existsById(id);
    }

    @Override
    public Optional<Rociste> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Rociste create(Rociste rociste) {
        return repo.save(rociste);
    }

    @Override
    public Optional<Rociste> update(Rociste rociste, int id) {
        if(existsById(id)){
            rociste.setId(id);
            return Optional.of(repo.save(rociste));
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }
}

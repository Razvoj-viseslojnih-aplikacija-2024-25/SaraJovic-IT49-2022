package rva.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rva.models.Predmet;
import rva.repository.PredmetRepository;
import rva.services.PredmetService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class PredmetServiceImp implements PredmetService {

    @Autowired
    private PredmetRepository repo;


    @Override
    public List<Predmet> getByBrojPr(Integer brojPr) {
        return repo.getByBrojPr(brojPr);
    }

    @Override
    public List<Predmet> getByDatum(Date datum) {
        return repo.getByDatum(datum);
    }

    @Override
    public List<Predmet> getAll() {
        return repo.findAll();
    }

    @Override
    public boolean existsById(int id) {
        return repo.existsById(id);
    }

    @Override
    public Optional<Predmet> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Predmet create(Predmet predmet) {
        return repo.save(predmet);
    }

    @Override
    public Optional<Predmet> update(Predmet predmet, int id) {
        if(existsById(id)){
            predmet.setId(id);
            return Optional.of(repo.save(predmet));
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
    repo.deleteById(id);
    }
}

package rva.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rva.models.Sud;
import rva.repository.SudRepository;
import rva.services.SudService;

import java.util.List;
import java.util.Optional;

@Component
public class SudServiceImp implements SudService {

    @Autowired
    private SudRepository repo;

    @Override
    public List<Sud> getByNaziv(String naziv) {
        return repo.findByNazivContainingIgnoreCase(naziv);
    }

    @Override
    public List<Sud> getAll() {
        return repo.findAll();
    }

    @Override
    public boolean existsById(int id) {
        return repo.existsById(id);
    }

    @Override
    public Optional<Sud> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Sud create(Sud sud) {
        return repo.save(sud);
    }

    @Override
    public Optional<Sud> update(Sud sud, int id) {
        if(existsById(id)){
            sud.setId(id);
            return Optional.of(repo.save(sud));
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }
}

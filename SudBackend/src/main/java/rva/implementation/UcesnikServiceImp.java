package rva.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rva.models.Ucesnik;
import rva.repository.UcesnikRepository;
import rva.services.UcesnikService;

import java.util.List;
import java.util.Optional;

@Component
public class UcesnikServiceImp implements UcesnikService {

    @Autowired
    private UcesnikRepository repo;

    @Override
    public List<Ucesnik> getByMbr(String mbr) {
        return repo.findByMbr(mbr);
    }

    @Override
    public List<Ucesnik> getByStatus(String status) {
        return repo.findByStatus(status);
    }

    @Override
    public List<Ucesnik> getAll() {
        return repo.findAll();
    }

    @Override
    public boolean existsById(int id) {
        return repo.existsById(id);
    }

    @Override
    public Optional<Ucesnik> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public Ucesnik create(Ucesnik ucesnik) {
        return repo.save(ucesnik);
    }

    @Override
    public Optional<Ucesnik> update(Ucesnik ucesnik, int id) {
        if(existsById(id)){
            ucesnik.setId(id);
            return Optional.of(repo.save(ucesnik));
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }
}

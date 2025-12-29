package rva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rva.models.Predmet;
import rva.models.Rociste;
import rva.models.Ucesnik;

import java.util.List;

public interface RocisteRepository extends JpaRepository<Rociste, Integer> {

    List<Rociste> findBySudnicaContainingIgnoreCase(String sudnica);
    List<Rociste> findByPredmet(Predmet predmet);
    List<Rociste> findByUcesnik(Ucesnik ucesnik);
}

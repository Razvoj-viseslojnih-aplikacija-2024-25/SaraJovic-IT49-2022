package rva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rva.models.Predmet;
import rva.models.Sud;

import java.time.LocalDate;
import java.util.List;

public interface PredmetRepository extends JpaRepository<Predmet, Integer> {

    // Pretraga po broju predmeta (contains, ignore case)
    List<Predmet> findByBrojPredmetaContainingIgnoreCase(String brojPredmeta);

    // Pretraga po datumu početka
    List<Predmet> findByDatumPocetka(LocalDate datumPocetka);

    // Pretraga po sudu
    List<Predmet> findBySud(Sud sud);

    // Pretraga po statusu aktivnosti
    List<Predmet> findByAktivan(Boolean aktivan);
}
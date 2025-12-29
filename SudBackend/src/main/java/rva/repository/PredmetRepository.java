package rva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rva.models.Predmet;

import java.util.Date;
import java.util.List;

public interface PredmetRepository extends JpaRepository<Predmet,Integer> {

    List<Predmet> getByBrojPr(int brojPr);
    List<Predmet> getByDatum(Date datum);
}

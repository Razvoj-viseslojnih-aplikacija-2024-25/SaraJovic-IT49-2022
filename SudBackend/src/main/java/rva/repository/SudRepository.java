package rva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rva.models.Sud;

import java.util.List;

public interface SudRepository extends JpaRepository<Sud, Integer>  {
    List<Sud> findByNazivContainingIgnoreCase(String naziv);
}

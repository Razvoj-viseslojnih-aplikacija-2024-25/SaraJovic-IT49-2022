package rva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rva.models.Ucesnik;

import java.util.List;

public interface UcesnikRepository extends JpaRepository<Ucesnik,Integer> {
    List<Ucesnik> findByMbr(String mbr);
    List<Ucesnik> findByStatus(String status);
}

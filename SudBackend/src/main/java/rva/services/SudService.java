package rva.services;

import rva.models.Sud;

import java.util.List;

public interface SudService extends  CrudService <Sud>{

    List<Sud> getByNaziv(String naziv);
}

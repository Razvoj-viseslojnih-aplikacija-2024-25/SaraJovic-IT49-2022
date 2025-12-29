package rva.services;

import rva.models.Ucesnik;

import java.util.List;

public interface UcesnikService extends CrudService<Ucesnik>{

    List<Ucesnik> getByMbr(String mbr);
    List<Ucesnik> getByStatus(String status);
}

package rva.services;

import rva.models.Predmet;
import rva.models.Rociste;
import rva.models.Ucesnik;

import java.util.Date;
import java.util.List;

public interface RocisteService extends CrudService<Rociste>{

    List<Rociste> getBySudnica(String sudnica);
    List<Rociste> getByPredmet(Predmet predmet);
    List<Rociste> getByUcesnik(Ucesnik ucesnik);
}

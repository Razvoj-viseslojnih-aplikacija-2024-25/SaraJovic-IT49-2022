package rva.services;

import rva.models.Predmet;
import rva.models.Sud;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface PredmetService extends CrudService<Predmet>{
    List<Predmet> getByBrojPredmeta(String brojPredmeta);
    List<Predmet> getByDatumPocetka(LocalDate datumPocetka);
    List<Predmet> getBySud(Sud sud);
    List<Predmet> getByAktivan(Boolean aktivan);
}

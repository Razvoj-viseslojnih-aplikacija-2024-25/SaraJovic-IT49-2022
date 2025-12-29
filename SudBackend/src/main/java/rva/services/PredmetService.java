package rva.services;

import rva.models.Predmet;

import java.util.Date;
import java.util.List;


public interface PredmetService extends CrudService<Predmet>{
    List<Predmet> getByBrojPr(Integer brojPr);
    List<Predmet> getByDatum (Date datum);
}

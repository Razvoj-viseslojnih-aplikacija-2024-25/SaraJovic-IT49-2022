package rva.models;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public  class Sud implements  Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SUD_ID_GEN", sequenceName = "SUD_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUD_ID_GEN")
    private Integer id;

    private String naziv;
    private String adresa;

    @OneToMany(mappedBy = "sud")
    private List<Predmet> predmeti;

    public Sud() {
        super();
    }

    public Sud(Integer id, String naziv, String adresa) {
        super();
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public List<Predmet> getPredmeti() {
        return predmeti;
    }

    public void setPredmeti(List<Predmet> predmeti) {
        this.predmeti = predmeti;
    }

}
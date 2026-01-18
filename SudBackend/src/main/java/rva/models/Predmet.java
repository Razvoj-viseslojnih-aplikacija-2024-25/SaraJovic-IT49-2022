package rva.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Predmet implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PREDMET_ID_GEN", sequenceName = "PREDMET_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREDMET_ID_GEN")
    private Integer id;

    @Column(name = "broj_predmeta")
    private String brojPredmeta;

    private String opis;

    @Column(name = "datum_pocetka")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate datumPocetka;

    private Boolean aktivan;

    @ManyToOne
    @JoinColumn(name = "sud_id")
    private Sud sud;

    @OneToMany(mappedBy = "predmet")
    @JsonIgnore
    private List<Rociste> rocista;

    public Predmet() {
        super();
    }

    public Predmet(Integer id, String brojPredmeta, String opis, LocalDate datumPocetka, Boolean aktivan) {
        super();
        this.id = id;
        this.brojPredmeta = brojPredmeta;
        this.opis = opis;
        this.datumPocetka = datumPocetka;
        this.aktivan = aktivan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrojPredmeta() {
        return brojPredmeta;
    }

    public void setBrojPredmeta(String brojPredmeta) {
        this.brojPredmeta = brojPredmeta;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDate getDatumPocetka() {
        return datumPocetka;
    }

    public void setDatumPocetka(LocalDate datumPocetka) {
        this.datumPocetka = datumPocetka;
    }

    public Boolean getAktivan() {
        return aktivan;
    }

    public void setAktivan(Boolean aktivan) {
        this.aktivan = aktivan;
    }

    public Sud getSud() {
        return sud;
    }

    public void setSud(Sud sud) {
        this.sud = sud;
    }

    public List<Rociste> getRocista() {
        return rocista;
    }

    public void setRocista(List<Rociste> rocista) {
        this.rocista = rocista;
    }
}

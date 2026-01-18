package rva.models;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
public class Rociste implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ROCISTE_ID_GEN", sequenceName = "ROCISTE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROCISTE_ID_GEN")
    private Integer id;

    @Column(name = "datum_rocista")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate datumRocista;

    private String sudnica;

    @ManyToOne
    @JoinColumn(name = "predmet_id")
    private Predmet predmet;

    @ManyToOne
    @JoinColumn(name = "ucesnik_id")
    private Ucesnik ucesnik;

    public Rociste() {
        super();
    }

    public Rociste(Integer id, LocalDate datumRocista, String sudnica) {
        super();
        this.id = id;
        this.datumRocista = datumRocista;
        this.sudnica = sudnica;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDatumRocista() {
        return datumRocista;
    }

    public void setDatumRocista(LocalDate datumRocista) {
        this.datumRocista = datumRocista;
    }

    public String getSudnica() {
        return sudnica;
    }

    public void setSudnica(String sudnica) {
        this.sudnica = sudnica;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }

    public Ucesnik getUcesnik() {
        return ucesnik;
    }

    public void setUcesnik(Ucesnik ucesnik) {
        this.ucesnik = ucesnik;
    }
}

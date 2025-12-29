package rva.models;
import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Ucesnik implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "UCESNIK_ID_GEN", sequenceName = "UCESNIK_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UCESNIK_ID_GEN")
    private Integer id;

    private String ime;
    private String prezime;
    private String mbr;
    private String status;

    @OneToMany(mappedBy = "ucesnik")
    private List<Rociste> rocista;


    public Ucesnik() {
        super();
    }

    public Ucesnik(Integer id, String ime, String prezime, String mbr, String status) {
        super();
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.mbr = mbr;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getMbr() {
        return mbr;
    }

    public void setMbr(String mbr) {
        this.mbr = mbr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

INSERT INTO sud (id, naziv, adresa)
VALUES
    (nextval('sud_seq'), 'Osnovni sud Beograd', 'Nemanjina 22'),
    (nextval('sud_seq'), 'Viši sud Novi Sad', 'Sutjeska 3'),
    (nextval('sud_seq'), 'Privredni sud Niš', 'Bulevar pravde 15');

INSERT INTO ucesnik (id, ime, prezime, mbr, status)
VALUES
    (nextval('ucesnik_seq'), 'Marko', 'Marković', '1234567890123', 'Tužilac'),
    (nextval('ucesnik_seq'), 'Ana', 'Anić', '9876543210987', 'Okrivljeni'),
    (nextval('ucesnik_seq'), 'Petar', 'Petrović', '4567891234567', 'Svedok');

INSERT INTO predmet (id, broj_predmeta, opis, datum_pocetka, aktivan, sud_id)
VALUES
    (nextval('predmet_seq'), 'P-101-2024', 'Krivični postupak', DATE '2024-01-15', TRUE, 1),
    (nextval('predmet_seq'), 'P-202-2024', 'Parnični postupak', DATE '2024-02-10', TRUE, 1),
    (nextval('predmet_seq'), 'P-303-2023', 'Privredni spor', DATE '2023-11-05', FALSE, 2);

INSERT INTO rociste (id, datum_rocista, sudnica, predmet_id, ucesnik_id)
VALUES
    (nextval('rociste_seq'), DATE '2024-03-01', 'Sudnica 1', 1, 1),
    (nextval('rociste_seq'), DATE '2024-03-15', 'Sudnica 2', 1, 2),
    (nextval('rociste_seq'), DATE '2024-04-10', 'Sudnica 3', 2, 3),
    (nextval('rociste_seq'), DATE '2024-05-20', 'Sudnica 1', 3, 1);
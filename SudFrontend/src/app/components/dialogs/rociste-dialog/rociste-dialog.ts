import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RocisteService } from '../../../services/rociste-service';
import { PredmetService } from '../../../services/predmet-service';
import { UcesnikService } from '../../../services/ucesnik-service';
import { Rociste } from '../../../models/rociste';
import { Predmet } from '../../../models/predmet';
import { Ucesnik } from '../../../models/ucesnik';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-rociste-dialog',
  templateUrl: './rociste-dialog.html',
  styleUrls: ['./rociste-dialog.css'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDialogModule
  ]
})
export class RocisteDialog implements OnInit {
  flag!: number;
  predmeti!: Predmet[];
  ucesnici!: Ucesnik[];

  constructor(
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<RocisteDialog>,
    private rocisteService: RocisteService,
    private predmetService: PredmetService,
    private ucesnikService: UcesnikService,
    @Inject(MAT_DIALOG_DATA) public data: Rociste
  ) { }

  ngOnInit(): void {
    this.predmetService.getAllPredmeti().subscribe(p => this.predmeti = p);
    this.ucesnikService.getAllUcesnici().subscribe(u => this.ucesnici = u);
  }

  add(): void {
    // Format date specifically for backend (yyyy-MM-dd)
    const formattedData = { ...this.data };
    if (this.data.datumRocista) {
      const date = new Date(this.data.datumRocista);
      const offset = date.getTimezoneOffset();
      const localDate = new Date(date.getTime() - (offset * 60 * 1000));
      (formattedData as any).datumRocista = localDate.toISOString().split('T')[0];
    }

    this.rocisteService.createRociste(formattedData).subscribe({
      next: (res) => {
        this.dialogRef.close(1);
        this.snackBar.open('Ročište je uspešno kreirano!', 'U redu', { duration: 2500 });
      },
      error: (err) => {
        this.snackBar.open('Greška prilikom kreiranja ročišta', 'U redu', { duration: 2500 });
        console.log(err);
      }
    });
  }

  update(): void {
    // Format date specifically for backend (yyyy-MM-dd)
    const formattedData = { ...this.data };
    if (this.data.datumRocista) {
      const date = new Date(this.data.datumRocista);
      const offset = date.getTimezoneOffset();
      const localDate = new Date(date.getTime() - (offset * 60 * 1000));
      (formattedData as any).datumRocista = localDate.toISOString().split('T')[0];
    }

    this.rocisteService.updateRociste(this.data.id, formattedData).subscribe({
      next: (res) => {
        this.dialogRef.close(1);
        this.snackBar.open('Ročište je uspešno ažurirano!', 'U redu', { duration: 2500 });
      },
      error: (err) => {
        this.snackBar.open('Greška prilikom ažuriranja ročišta', 'U redu', { duration: 2500 });
        console.log(err);
      }
    });
  }

  delete(): void {
    this.rocisteService.deleteRociste(this.data.id).subscribe({
      next: () => {
        this.dialogRef.close(1);
        this.snackBar.open('Ročište je obrisano!', 'U redu', { duration: 2500 });
      },
      error: (err) => {
        this.snackBar.open('Greška prilikom brisanja ročišta', 'U redu', { duration: 2500 });
        console.log(err);
      }
    });
  }

  cancel(): void {
    this.dialogRef.close();
    this.snackBar.open('Odustali ste od izmena', 'U redu', { duration: 2500 });
  }

  comparePredmet(a: Predmet, b: Predmet) {
    return a && b && a.id === b.id;
  }

  compareUcesnik(a: Ucesnik, b: Ucesnik) {
    return a && b && a.id === b.id;
  }
}

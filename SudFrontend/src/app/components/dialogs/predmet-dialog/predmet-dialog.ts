import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PredmetService } from '../../../services/predmet-service';
import { Predmet } from '../../../models/predmet';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { Sud } from '../../../models/sud';
import { SudService } from '../../../services/sud-service';

@Component({
  selector: 'app-predmet-dialog',
  imports: [
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    FormsModule,
  ],
  templateUrl: './predmet-dialog.html',
  styleUrl: './predmet-dialog.css',
  standalone: true
})
export class PredmetDialog implements OnInit {
  flag!: number;
  sudovi!: Sud[];

  constructor(
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<PredmetDialog>,
    private predmetService: PredmetService,
    @Inject(MAT_DIALOG_DATA) public data: Predmet,
    private sudService: SudService
  ) { }

  ngOnInit(): void {
    this.sudService.getAllSudovi().subscribe((data) => {
      this.sudovi = data;
    });
  }

  public add(): void {
    // Format date specifically for backend (yyyy-MM-dd)
    const formattedData = { ...this.data };
    if (this.data.datumPocetka) {
      const date = new Date(this.data.datumPocetka);
      const offset = date.getTimezoneOffset();
      const localDate = new Date(date.getTime() - (offset * 60 * 1000));
      (formattedData as any).datumPocetka = localDate.toISOString().split('T')[0];
    }

    this.predmetService.createPredmet(formattedData).subscribe({
      next: (data) => {
        this.dialogRef.close(1);
        this.snackBar.open(
          `Predmet ${data.brojPredmeta} je uspešno kreiran!`,
          'U redu',
          { duration: 2500 }
        );
      },
      error: (error) => {
        this.snackBar.open(
          'Došlo je do greške prilikom kreiranja predmeta',
          'U redu',
          { duration: 2500 }
        );
        console.log(error.message);
      },
    });
  }

  public update(): void {
    // Format date specifically for backend (yyyy-MM-dd)
    const formattedData = { ...this.data };
    if (this.data.datumPocetka) {
      const date = new Date(this.data.datumPocetka);
      const offset = date.getTimezoneOffset();
      const localDate = new Date(date.getTime() - (offset * 60 * 1000));
      (formattedData as any).datumPocetka = localDate.toISOString().split('T')[0];
    }

    this.predmetService.updatePredmet(this.data.id, formattedData).subscribe({
      next: (data) => {
        this.dialogRef.close(1);
        this.snackBar.open(
          `Predmet ${data.brojPredmeta} je uspešno ažuriran!`,
          'U redu',
          { duration: 2500 }
        );
      },
      error: (error) => {
        this.snackBar.open(
          'Došlo je do greške prilikom ažuriranja predmeta',
          'U redu',
          { duration: 2500 }
        );
        console.log(error.message);
      },
    });
  }

  public delete(): void {
    this.predmetService.deletePredmet(this.data.id).subscribe({
      next: (response) => {
        this.dialogRef.close(1);
        this.snackBar.open('Predmet je uspešno obrisan!', 'U redu', { duration: 2500 });
      },
      error: (error) => {
        this.snackBar.open(
          'Došlo je do greške prilikom brisanja predmeta',
          'U redu',
          { duration: 2500 }
        );
        console.log(error.message);
      },
    });
  }

  public cancel(): void {
    this.dialogRef.close();
    this.snackBar.open('Odustali ste od izmena', 'U redu', { duration: 2500 });
  }

  public compareSud(a: Sud, b: Sud) {
    return a && b && a.id === b.id;
  }
}
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';

import { Ucesnik } from '../../../models/ucesnik';
import { UcesnikService } from '../../../services/ucesnik-service';
import { Predmet } from '../../../models/predmet';
import { PredmetService } from '../../../services/predmet-service';

@Component({
  selector: 'app-ucesnik-dialog',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    CommonModule,
    FormsModule,
    MatDialogModule,
  ],
  templateUrl: './ucesnik-dialog.html',
  styleUrls: ['./ucesnik-dialog.css'],
  standalone: true
})
export class UcesnikDialog implements OnInit {
  flag!: number;
  predmeti!: Predmet[];

  constructor(
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<UcesnikDialog>,
    @Inject(MAT_DIALOG_DATA) public data: Ucesnik,
    private ucesnikService: UcesnikService,
    private predmetService: PredmetService,
  ) {}

  ngOnInit(): void {
    this.predmetService.getAllPredmeti().subscribe((data) => {
      this.predmeti = data;
    });
  }

  public add(): void {
    this.ucesnikService.createUcesnik(this.data).subscribe({
      next: (data) => {
        this.dialogRef.close(1);
        this.snackBar.open(`Učesnik ${data.ime} ${data.prezime} je kreiran!`, 'U redu', { duration: 2500 });
      },
      error: (error) => {
        console.log(error.message);
        this.snackBar.open('Greška prilikom kreiranja učesnika', 'U redu', { duration: 2500 });
      },
    });
  }

  public update(): void {
    this.ucesnikService.updateUcesnik(this.data.id, this.data).subscribe({
      next: (data) => {
        this.dialogRef.close(1);
        this.snackBar.open(`Učesnik ${data.ime} ${data.prezime} je ažuriran!`, 'U redu', { duration: 2500 });
      },
      error: (error) => {
        console.log(error.message);
        this.snackBar.open('Greška prilikom ažuriranja učesnika', 'U redu', { duration: 2500 });
      },
    });
  }

  public delete(): void {
    this.ucesnikService.deleteUcesnik(this.data.id).subscribe({
      next: () => {
        this.dialogRef.close(1);
        this.snackBar.open('Učesnik je obrisan!', 'U redu', { duration: 2500 });
      },
      error: (error) => {
        console.log(error.message);
        this.snackBar.open('Greška prilikom brisanja učesnika', 'U redu', { duration: 2500 });
      },
    });
  }

  public cancel(): void {
    this.dialogRef.close();
    this.snackBar.open('Odustali ste od izmena', 'U redu', { duration: 2500 });
  }

  public comparePredmet(a: Predmet, b: Predmet) {
    return a && b && a.id === b.id;
  }
}

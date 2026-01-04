import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SudService } from '../../../services/sud-service';
import { Sud } from '../../../models/sud';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sud-dialog',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    FormsModule,
  ],
  templateUrl: './sud-dialog.html',
  styleUrl: './sud-dialog.css',
  standalone: true
})
export class SudDialog {
  flag!: number;

  constructor(
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<SudDialog>,
    private sudService: SudService,
    @Inject(MAT_DIALOG_DATA) public data: Sud
  ) {}

  public add(): void {
    this.sudService.createSud(this.data).subscribe({
      next: (data) => {
        this.dialogRef.close(1);
        this.snackBar.open(
          `Sud sa nazivom: ${data.naziv} je uspešno kreiran!`,
          'U redu',
          { duration: 2500 }
        );
      },
      error: (error) => {
        this.snackBar.open(
          'Došlo je do greške prilikom kreiranja suda',
          'U redu',
          { duration: 2500 }
        );
        console.log(error.message);
      },
    });
  }

  public update(): void {
    this.sudService.updateSud(this.data.id, this.data).subscribe({
      next: (data) => {
        this.dialogRef.close(1);
        this.snackBar.open(
          `Sud sa nazivom: ${data.naziv} je uspešno ažuriran!`,
          'U redu',
          { duration: 2500 }
        );
      },
      error: (error) => {
        this.snackBar.open(
          'Došlo je do greške prilikom ažuriranja suda',
          'U redu',
          { duration: 2500 }
        );
        console.log(error.message);
      },
    });
  }

  public delete(): void {
    this.sudService.deleteSud(this.data.id).subscribe({
      next: (response) => {
        this.dialogRef.close(1);
        this.snackBar.open('Sud je uspešno obrisan!', 'U redu', { duration: 2500 });
      },
      error: (error) => {
        this.snackBar.open(
          'Došlo je do greške prilikom brisanja suda',
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
}
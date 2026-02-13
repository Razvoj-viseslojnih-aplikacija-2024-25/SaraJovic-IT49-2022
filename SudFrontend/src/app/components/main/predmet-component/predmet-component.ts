import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';

import { Predmet } from '../../../models/predmet';
import { PredmetService } from '../../../services/predmet-service';
import { PredmetDialog } from '../../dialogs/predmet-dialog/predmet-dialog';

@Component({
  selector: 'app-predmet-component',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    MatTableModule, 
    MatIconModule, 
    MatToolbarModule, 
    MatSortModule, 
    MatPaginatorModule,
    MatDialogModule,
    MatButtonModule,
    MatTooltipModule,
    MatChipsModule
  ],
  templateUrl: './predmet-component.html',
  styleUrl: './predmet-component.css',
})
export class PredmetComponent implements OnInit {
  displayedColumns = ['id', 'brojPredmeta', 'opis', 'datumPocetka', 'aktivan', 'actions'];
  dataSource!: MatTableDataSource<Predmet>;

  @ViewChild(MatSort, { static: false }) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

  constructor(private predmetService: PredmetService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadData();
  }

  public loadData(): void {
    this.predmetService.getAllPredmeti().subscribe({
      next: (data) => {
        console.log(data);
        this.dataSource = new MatTableDataSource<Predmet>(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (error) => {
        console.log(error.message);
      },
    });
  }

  public openDialog(
    flag: number,
    id?: number,
    brojPredmeta?: string,
    opis?: string,
    datumPocetka?: Date,
    aktivan?: boolean
  ): void {
    const ref = this.dialog.open(PredmetDialog, {
      data: { id, brojPredmeta, opis, datumPocetka, aktivan },
    });
    ref.componentInstance.flag = flag;
    ref.afterClosed().subscribe((response) => {
      if (response === 1) {
        this.loadData();
      }
    });
  }
}
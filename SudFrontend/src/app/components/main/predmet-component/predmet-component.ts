import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { CommonModule, DatePipe } from '@angular/common';

import { Predmet } from '../../../models/predmet';
import { PredmetService } from '../../../services/predmet-service';
import { PredmetDialog } from '../../dialogs/predmet-dialog/predmet-dialog';


@Component({
  selector: 'app-predmet-component',
  imports: [MatTableModule, MatIconModule, MatToolbarModule, MatSortModule, MatPaginatorModule,CommonModule,
    DatePipe],
  templateUrl: './predmet-component.html',
  styleUrl: './predmet-component.css',
  standalone: true
})
export class PredmetComponent implements OnInit {
  displayedColumns = ['id', 'brojPredmeta', 'opis', 'datumPocetka', 'aktivan', 'actions'];
  dataSource!: MatTableDataSource<Predmet>;
  parentSelectedPredmet!:Predmet;

  constructor(private predmetService: PredmetService, private dialog: MatDialog) {}

  @ViewChild(MatSort, { static: false }) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

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
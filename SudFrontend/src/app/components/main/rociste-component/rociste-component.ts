import { Component, ViewChild, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';

import { MatTableDataSource } from '@angular/material/table';
import { Rociste } from '../../../models/rociste';
import { RocisteService } from '../../../services/rociste-service';
import { RocisteDialog } from '../../dialogs/rociste-dialog/rociste-dialog';

@Component({
  selector: 'app-rociste-component',
  templateUrl: './rociste-component.html',
  styleUrls: ['./rociste-component.css'],
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    MatTooltipModule
  ]
})
export class RocisteComponent implements OnInit {
  displayedColumns: string[] = ['id', 'datumRocista', 'sudnica', 'predmet', 'ucesnik', 'actions'];
  dataSource!: MatTableDataSource<Rociste>;

  @ViewChild(MatSort, { static: false }) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

  constructor(private rocisteService: RocisteService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.rocisteService.getAllRocista().subscribe({
      next: (data) => {
        this.dataSource = new MatTableDataSource<Rociste>(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (err) => console.error(err)
    });
  }

  openDialog(flag: number, id?: number, datumRocista?: Date, sudnica?: string, predmet?: any, ucesnik?: any): void {
    const ref = this.dialog.open(RocisteDialog, {
      data: { id, datumRocista, sudnica, predmet, ucesnik }
    });
    ref.componentInstance.flag = flag;
    ref.afterClosed().subscribe(result => {
      if (result === 1) this.loadData();
    });
  }
}

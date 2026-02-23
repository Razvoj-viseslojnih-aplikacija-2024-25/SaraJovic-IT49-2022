import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';

import { Ucesnik } from '../../../models/ucesnik';
import { UcesnikService } from '../../../services/ucesnik-service';
import { UcesnikDialog } from '../../dialogs/ucesnik-dialog/ucesnik-dialog';

@Component({
  selector: 'app-ucesnik-component',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatIconModule,
    MatToolbarModule,
    MatPaginatorModule,
    MatSortModule,
    MatDialogModule,
    MatButtonModule,
    MatTooltipModule,
    MatChipsModule
  ],
  templateUrl: './ucesnik-component.html',
  styleUrls: ['./ucesnik-component.css']
})
export class UcesnikComponent implements OnInit {

  displayedColumns = ['id', 'ime', 'prezime', 'mbr', 'status', 'actions'];
  dataSource!: MatTableDataSource<Ucesnik>;

  @ViewChild(MatSort, { static: false }) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

  constructor(private ucesnikService: UcesnikService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.ucesnikService.getAllUcesnici().subscribe({
      next: (data) => {
        this.dataSource = new MatTableDataSource<Ucesnik>(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (error) => console.log(error)
    });
  }

  public searchById(id: number): void {
    this.ucesnikService.getUcesnikById(id).subscribe({
      next: (data) => {
        this.dataSource = new MatTableDataSource<Ucesnik>([data]);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (error) => {
        console.log(error.message);
      },
    });
  }

  public searchByMbr(mbr: string): void {
    this.ucesnikService.getUcesniciByMbr(mbr).subscribe({
      next: (data) => {
        this.dataSource = new MatTableDataSource<Ucesnik>(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (error) => {
        console.log(error.message);
      },
    });
  }

  public searchByStatus(status: string): void {
    this.ucesnikService.getUcesniciByStatus(status).subscribe({
      next: (data) => {
        this.dataSource = new MatTableDataSource<Ucesnik>(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (error) => {
        console.log(error.message);
      },
    });
  }

  openDialog(flag: number, id?: number, ime?: string, prezime?: string, mbr?: string, status?: string): void {
    const ref = this.dialog.open(UcesnikDialog, {
      data: { id, ime, prezime, mbr, status }
    });
    ref.componentInstance.flag = flag;
    ref.afterClosed().subscribe(result => {
      if (result === 1) {
        this.loadData();
      }
    });
  }

  getStatusIcon(status: string): string {
    switch (status?.toUpperCase()) {
      case 'TUŽILAC':
      case 'TUZILAC':
        return 'gavel';
      case 'OKRIVLJENI':
        return 'person_off';
      case 'SVEDOK':
      case 'SVEDEOK':
        return 'record_voice_over';
      default:
        return 'help';
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toLowerCase()) {
      case 'tužilac':
      case 'tuzilac':
        return 'tuzilac';
      case 'okrivljeni':
        return 'okrivljeni';
      case 'svedok':
      case 'svedeok':
        return 'svedeok';
      default:
        return '';
    }
  }
}
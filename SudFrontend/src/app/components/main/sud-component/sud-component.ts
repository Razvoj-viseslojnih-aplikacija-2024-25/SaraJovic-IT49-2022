import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';

import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Sud } from '../../../models/sud';
import { SudService } from '../../../services/sud-service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { SudDialog } from '../../dialogs/sud-dialog/sud-dialog';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';

import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-sud-component',
  imports: [MatTableModule, MatIconModule, MatToolbarModule, MatSortModule, MatPaginatorModule, MatButtonModule, MatDialogModule, MatTooltipModule, MatInputModule, MatFormFieldModule],
  templateUrl: './sud-component.html',
  styleUrl: './sud-component.css',
})
export class SudComponent implements OnInit {
  displayedColumns = ['id', 'naziv', 'adresa', 'actions'];
  dataSource!: MatTableDataSource<Sud>;

  constructor(private sudService: SudService, private dialog: MatDialog) { }

  @ViewChild(MatSort, { static: false }) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

  ngOnInit(): void {
    this.loadData();
  }

  public loadData(): void {
    this.sudService.getAllSudovi().subscribe({
      next: (data) => {
        console.log(data);
        this.dataSource = new MatTableDataSource<Sud>(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (error) => {
        console.log(error.message);
      },
    });
  }

  public applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  public openDialog(flag: number, id?: number, naziv?: string, adresa?: string): void {
    const ref = this.dialog.open(SudDialog, { data: { id, naziv, adresa } });
    ref.componentInstance.flag = flag;
    ref.afterClosed().subscribe((response) => {
      if (response === 1) {
        this.loadData();
      }
    });
  }
}
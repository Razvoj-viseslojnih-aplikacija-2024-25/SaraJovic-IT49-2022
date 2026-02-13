import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Sud } from '../models/sud';

@Injectable({
  providedIn: 'root'
})
export class SudService {
  private baseUrl = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) {}

  public getAllSudovi(): Observable<Sud[]> {
    return this.httpClient.get<Sud[]>(`${this.baseUrl}/sudovi`);
  }

  public getSudById(id: number): Observable<Sud> {
    return this.httpClient.get<Sud>(`${this.baseUrl}/sud/id/${id}`);
  }

  public createSud(sud: Sud): Observable<Sud> {
    return this.httpClient.post<Sud>(`${this.baseUrl}/sud`, sud);
  }

  public updateSud(id: number, sud: Sud): Observable<Sud> {
    return this.httpClient.put<Sud>(`${this.baseUrl}/sud/${id}`, sud);
  }

  public deleteSud(id: number): Observable<any> {
    return this.httpClient.delete(`${this.baseUrl}/sud/${id}`, { responseType: 'text' });
  }

  public getSudoviByNaziv(naziv: string): Observable<Sud[]> {
    return this.httpClient.get<Sud[]>(`${this.baseUrl}/sud/naziv/${naziv}`);
  }
}

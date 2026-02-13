import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ucesnik } from '../models/ucesnik';

@Injectable({
  providedIn: 'root'
})
export class UcesnikService {
  private baseUrl = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }

  // Vrati sve učesnike
  public getAllUcesnici(): Observable<Ucesnik[]> {
    return this.httpClient.get<Ucesnik[]>(`${this.baseUrl}/ucesnici`);
  }

  // Vrati učesnika po ID
  public getUcesnikById(id: number): Observable<Ucesnik> {
    return this.httpClient.get<Ucesnik>(`${this.baseUrl}/ucesnik/id/${id}`);
  }

  // Kreiraj novog učesnika
  public createUcesnik(ucesnik: Ucesnik): Observable<Ucesnik> {
    return this.httpClient.post<Ucesnik>(`${this.baseUrl}/ucesnik`, ucesnik);
  }

  // Ažuriraj učesnika
  public updateUcesnik(id: number, ucesnik: Ucesnik): Observable<Ucesnik> {
    return this.httpClient.put<Ucesnik>(`${this.baseUrl}/ucesnik/${id}`, ucesnik);
  }

  // Obriši učesnika
  public deleteUcesnik(id: number): Observable<any> {
    return this.httpClient.delete(`${this.baseUrl}/ucesnik/${id}`, { responseType: 'text' });
  }

  // Pretraga po MBR
  public getUcesniciByMbr(mbr: string): Observable<Ucesnik[]> {
    return this.httpClient.get<Ucesnik[]>(`${this.baseUrl}/ucesnik/mbr/${mbr}`);
  }

  // Pretraga po statusu (aktivan/neaktivan)
  public getUcesniciByStatus(status: string): Observable<Ucesnik[]> {
    return this.httpClient.get<Ucesnik[]>(`${this.baseUrl}/ucesnik/status/${status}`);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rociste } from '../models/rociste';
import { Predmet } from '../models/predmet';
import { Ucesnik } from '../models/ucesnik';

@Injectable({
  providedIn: 'root'
})
export class RocisteService {
  private baseUrl = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }

  // Dobavljanje svih ročišta
  public getAllRocista(): Observable<Rociste[]> {
    return this.httpClient.get<Rociste[]>(`${this.baseUrl}/rocista`);
  }

  // Dobavljanje ročišta po ID
  public getRocisteById(id: number): Observable<Rociste> {
    return this.httpClient.get<Rociste>(`${this.baseUrl}/rociste/id/${id}`);
  }

  // Kreiranje novog ročišta
  public createRociste(rociste: Rociste): Observable<Rociste> {
    return this.httpClient.post<Rociste>(`${this.baseUrl}/rociste`, rociste);
  }

  // Ažuriranje ročišta po ID
  public updateRociste(id: number, rociste: Rociste): Observable<Rociste> {
    return this.httpClient.put<Rociste>(`${this.baseUrl}/rociste/${id}`, rociste);
  }

  // Brisanje ročišta po ID
  public deleteRociste(id: number): Observable<any> {
    return this.httpClient.delete(`${this.baseUrl}/rociste/${id}`, { responseType: 'text' });
  }

  // Dobavljanje ročišta po predmetu
  public getRocistaByPredmet(predmetId: number): Observable<Rociste[]> {
    return this.httpClient.get<Rociste[]>(`${this.baseUrl}/rociste/predmet/${predmetId}`);
  }

  // Dobavljanje ročišta po učesniku
  public getRocistaByUcesnik(ucesnikId: number): Observable<Rociste[]> {
    return this.httpClient.get<Rociste[]>(`${this.baseUrl}/rociste/ucesnik/${ucesnikId}`);
  }

  // Dobavljanje ročišta po sudnici
  public getRocistaBySudnica(sudnica: string): Observable<Rociste[]> {
    return this.httpClient.get<Rociste[]>(`${this.baseUrl}/rociste/sudnica/${sudnica}`);
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Predmet } from '../models/predmet';


@Injectable({
  providedIn: 'root'
})
export class PredmetService {
  private baseUrl = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }

  public getAllPredmeti(): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/predmeti`);
  }

  public getPredmetById(id: number): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/predmet/id/${id}`);
  }

  public createPredmet(predmet: Predmet): Observable<any> {
    return this.httpClient.post(`${this.baseUrl}/predmet`, predmet);
  }

  public updatePredmet(id: number, predmet: Predmet): Observable<any> {
    return this.httpClient.put(`${this.baseUrl}/predmet/${id}`, predmet);
  }

  public deletePredmet(id: number): Observable<any> {
    return this.httpClient.delete(`${this.baseUrl}/predmet/${id}`, { responseType: 'text' });
  }

  public getPredmetiByBrojPredmeta(brojPredmeta: string): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/predmet/brojPredmeta/${brojPredmeta}`);
  }

  public getPredmetiByAktivan(aktivan: boolean): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/predmet/aktivan/${aktivan}`);
  }

  public getPredmetiBySud(sudId: number): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/predmet/sud/${sudId}`);
  }
}
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EnergyGridService  {
  constructor(private http: HttpClient) {
  }

  getGrid(): Observable<any> {
    return this.http.get<any>(`api/grid/main`);
  }

  updateGrid(data: any): Observable<void> {
    return this.http.post<void>(`api/grid/main`, data);
  }
}

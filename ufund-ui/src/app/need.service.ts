import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Need } from './need';
import { MessageService } from './message.service';


@Injectable({ providedIn: 'root' })
export class NeedService {

  private needsUrl = 'http://localhost:8080/needs';//'api/needs';  // URL to web api
  need: Need | undefined;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient) { }

  /** GET needs from the server */
  getAllNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl)
      .pipe(
        tap(_ => console.log('fetched needs')),
        catchError(this.handleError<Need[]>('getAllNeeds', []))
      );
  }

  fulfillNeedQuantity(id: number, quantity: number): Observable<Need>{
    const url = `${this.needsUrl}/${id}/fulfill?quantity=${quantity}`;
    return this.http.put<Need>(url, quantity).pipe(
      tap(_ => console.log(`changed quantityFulfilled of need id=${id}`)),
      catchError(this.handleError<Need>(`filfillQuantity id=${id} quantity=${quantity}`))
    );
  }

  /** GET need by id. Return `undefined` when id not found */
  getNeedNo404<Data>(id: number): Observable<Need> {
    const url = `${this.needsUrl}/?id=${id}`;
    return this.http.get<Need[]>(url)
      .pipe(
        map(needs => needs[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? 'fetched' : 'did not find';
          console.log(`${outcome} need id=${id}`);
        }),
        catchError(this.handleError<Need>(`getNeed id=${id}`))
      );
  }

  /** GET need by id. Will 404 if id not found */
  getNeed(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;
    return this.http.get<Need>(url).pipe(
      tap(_ => console.log(`fetched need id=${id}`)),
      catchError(this.handleError<Need>(`getNeed id=${id}`))
    );
  }

   /** GET needs by id array. Will 404 if id not found */
  getNeeds(ids: number[]): Observable<Need[]>{
    const url = `${this.needsUrl}/array/?ids=${ids.toString()}`
    return this.http.get<Need[]>(url)
      .pipe(
        tap(_ => console.log('fetched needs')),
        catchError(this.handleError<Need[]>('getNeeds', []))
      );
  }

  /* GET needs whose name contains search term */
  searchNeeds(term: string): Observable<Need[]> {
    if (!term.trim()) {
      // if not search term, return empty need array.
      return of([]);
    }
    return this.http.get<Need[]>(`${this.needsUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
         console.log(`found needs matching "${term}"`) :
         console.log(`no needs matching "${term}"`)),
      catchError(this.handleError<Need[]>('searchNeeds', []))
    );
  }

  //////// Save methods //////////

  /** POST: add a new need to the server */
  addNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.needsUrl, need, this.httpOptions).pipe(
      tap((newNeed: Need) => console.log(`added need w/ id=${newNeed.id}`)),
      catchError(this.handleError<Need>('addNeed'))
    );
  }

  /** DELETE: delete the need from the server */
  deleteNeed(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;

    return this.http.delete<Need>(url, this.httpOptions).pipe(
      tap(_ => console.log(`deleted need id=${id}`)),
      catchError(this.handleError<Need>('deleteNeed'))
    );
  }

  /** PUT: update the need on the server */
  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsUrl, need, this.httpOptions).pipe(
      tap(_ => console.log(`updated need id=${need.id}`)),
      catchError(this.handleError<any>('updateNeed'))
    );
  }

  getProgress(): Observable<number[]>{
    return this.http.get<number[]>(`${this.needsUrl}/progress`).pipe(
      tap(_ => console.log(`get progress`)),
      catchError(this.handleError<any>('getProgress'))
    );
  }

  getUrgency(id: number): string {
    this.getNeed(id).subscribe(need => (this.need = need));
    if(this.need){
      if(this.need?.urgency){
        return this.need.urgency;
      }
    }
    
    return '';
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
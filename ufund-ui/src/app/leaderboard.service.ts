import { Injectable } from '@angular/core';
import { Profile } from './profile';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, of } from 'rxjs';
import { Need } from './need';

@Injectable({
  providedIn: 'root'
})
export class LeaderboardService {
  private leaderboardURL = 'http://localhost:8080/leaderboard'; // URL to web api
  //need: Need | undefined;
  /* someArray: Record<number, Profile>[] = [
    {'first': 'one'},
    {'second': 'two'}
  ]; */
  //rankings: Record<number, Profile>[] = [];
  //rankings: Profile[] | undefined;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  /** GET leaderboard from the server */
  getLeaderboard(): Observable<String[][]> {
    return this.http.get<String[][]>(this.leaderboardURL)
      .pipe(
        tap(_ => console.log('fetched leaderboard')),
        catchError(this.handleError<String[][]>('getLeaderboard', []))
      );
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
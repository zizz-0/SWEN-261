import { Injectable } from '@angular/core';
import { Profile } from './profile';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private profilesUrl = 'http://localhost:8080/profiles/';//'api/profiles';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { 

  }

  /** GET profile by user name. Will 404 if id not found */
  getProfile(userName: string): Observable<Profile> {
    
    const url = `${this.profilesUrl}/${userName}`;
    return this.http.get<Profile>(url).pipe(
      tap(_ => console.log(`fetched profile userName=${userName}`)),
      catchError(this.handleError<Profile>(`getProfile userName=${userName}`))
    );
  }

  /** PUT: update the profile on the server */
  updateProfile(profile: Profile): Observable<any> {
   
    return this.http.put(this.profilesUrl, profile, this.httpOptions).pipe(
      tap(_ => console.log(`updated profile userName=${profile.userName}`)),
      catchError(this.handleError<any>('updateProfile'))
    );
  }

  /** POST: add a new profile to the server */
  createProfile(profile: Profile): Observable<Profile> {
    return this.http.post<Profile>(this.profilesUrl, profile, this.httpOptions).pipe(
      tap((newProfile: Profile) => console.log(`added profile w/ userName=${newProfile.userName}`)),
      catchError(this.handleError<Profile>('addedProfile'))
    );
  }

  /** PUT: switches privacy on the server */
  switchPrivacy(profile: Profile): Observable<any> {
    return this.http.put(`${this.profilesUrl}/privacy`, profile, this.httpOptions).pipe(
      tap(_ => console.log(`switched privacy for userName=${profile.userName}`)),
      catchError(this.handleError<any>('switchPrivacy'))
    );
  }

  addContribution(userName: string, needId: number, quantity: number): Observable<any>{
    return this.http.put(`${this.profilesUrl}/${userName}/contribute?needId=${needId}&quantity=${quantity}`,{"needId": needId, "quantity": quantity}).pipe(
      tap(_ => console.log(`added contributions for userName=${userName}`)),
      catchError(this.handleError<any>('switchPrivacy'))
    );
  }

  /** DELETE: delete the profile from the server */
  deleteProfile(userName: string): Observable<Profile> {
    const url = `${this.profilesUrl}/${userName}`;

    return this.http.delete<Profile>(url, this.httpOptions).pipe(
      tap(_ => console.log(`deleted profile=${userName}`)),
      catchError(this.handleError<Profile>('deleteProfile'))
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

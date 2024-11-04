import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of} from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { FundingBasket } from './funding-basket';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class FundingBasketService {
  private basketUrl = 'http://localhost:8080/baskets/'
  private basketId = 1; //TODO: link this to login

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor( private http: HttpClient, private messageService: MessageService) {
    
  }

  /** GET basket by id */
  getBasket(basketId: number): Observable<FundingBasket> {
    return this.http.get<FundingBasket>(`${this.basketUrl}/${basketId}`)
      .pipe(
        catchError(this.handleError<FundingBasket>(`getbasket id=${basketId}`))
      );
  }

  /** GET basket by id. Return `undefined` when id not found */
  getBasketNo404<Data>(basketId: number): Observable<FundingBasket> {
    const url = `${this.basketUrl}/?id=${basketId}`;
    return this.http.get<FundingBasket>(url)
      .pipe(
        tap(h => {
          const outcome = h ? 'fetched' : 'did not find';
        }),
        catchError(this.handleError<FundingBasket>(`getbasket id=${basketId}`))
      );
  }

  /** GET need by id. Will 404 if id not found */
  getUsername(basketId: number): Observable<string> {
    const url = `${this.basketUrl}/${basketId}/user`;
    return this.http.get<string>(url).pipe(
      catchError(this.handleError<string>(`getUsername id=${basketId}`))
    );
  }
  setUsername(basketId: number, username: String): Observable<Boolean> {
    const url = `${this.basketUrl}/${basketId}/user/${username}`;
    return this.http.put<Boolean>(url, username).pipe(
      catchError(this.handleError<Boolean>('setUsername'))
    );
  }

  /** GET all needs from the basket */
  getNeeds(basketId: number): Observable<Map<number, number>> {
    return this.http.get<Map<number, number>>(`${this.basketUrl}${basketId}/needs`)
      .pipe(
        tap(_ => console.log('fetched needs')),
        catchError(this.handleError<Map<number, number>>('getNeeds'))
      );
  }

  /** PUT: add a new need to the basket */
  addNeed(basketId: number, needId: number, quantity: number): Observable<FundingBasket> {
    return this.http.put<FundingBasket>(`${this.basketUrl}/${basketId}/${needId}?quantity=${quantity}`, needId, this.httpOptions).pipe(
      catchError(this.handleError<FundingBasket>('addNeed'))
    );
  }

  /** DELETE: delete the need from the basket */
  removeNeed(basketId: number, needId: number): Observable<FundingBasket> {
    const url = `${this.basketUrl}${basketId}/${needId}`;
    console.log(url);
    return this.http.delete<FundingBasket>(url, this.httpOptions).pipe(
      catchError(this.handleError<FundingBasket>('deleteNeed'))
    );
  }

  /** PUT: claer needs form basket */
  clearBasket(basketId: number): Observable<FundingBasket> {
    return this.http.put<FundingBasket>(`${this.basketUrl}/clear?basketId=${basketId}`, {"basketId":basketId}).pipe(
      catchError(this.handleError<FundingBasket>('clearBasket'))
    );
  }

  /** DELETE: deletes the basket from the server */
  deleteBasket(basketId: number): Observable<boolean> {
    const url = `${this.basketUrl}/?basketId=${basketId}`;

    return this.http.delete<boolean>(url, this.httpOptions).pipe(
      catchError(this.handleError<boolean>('deleteBasket'))
    );
  }

  getQuantity(basketId: number, needId: number): Observable<number>{
    const url = `${this.basketUrl}/${basketId}/${needId}`
    return this.http.get<number>(url).pipe(
      catchError(this.handleError<number>('getQuantity'))
    );
  }

  setQuantity(basketId: number, needId: number, quantity: number): Observable<FundingBasket> {
    const url = `${this.basketUrl}/${basketId}/${needId}/${quantity}`;
    return this.http.put<FundingBasket>(url, quantity).pipe(
      catchError(this.handleError<FundingBasket>('setQuantity'))
    );
  }

  
  updateQuantity(basketId: number, needId: number, quantity: number){
    this.getQuantity(basketId, needId).subscribe((q)=> this.setQuantity(basketId,needId,q+quantity).subscribe());
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

      // log to console 
      console.error(error);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

 
}

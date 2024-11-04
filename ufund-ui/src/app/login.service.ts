import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Login } from './login';
import { FundingBasket } from './funding-basket';
import { ProfileService } from './profile.service';
import { Profile } from './profile';
@Injectable({
  providedIn: 'root'
})
export class LoginService {
  currentUser: Login = {userName: "", pass: "", basketId: 0, role: ""};
  private loginUrl = 'http://localhost:8080/logins/';
  private basketUrl = 'http://localhost:8080/baskets/';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  constructor(private http: HttpClient, private profileService: ProfileService) { }

  //Returns boolean whether login was success or not; Does not check for empty strings
  login(username: string, password: string): Observable<Boolean> {
    const login = this.http.get<Boolean>(`${this.loginUrl}/login?userName=${username}&pass=${password}`)
      .pipe(
        catchError(this.handleError<Boolean>(`login userName=${username} pass=${password}`))
      );

    login.subscribe(valid => {if(valid){this.setCurrentUser(username, password)}});
    
    return login;
  }

  setCurrentUser(username: string, password: string) {
    this.currentUser.userName = username;
    this.currentUser.pass = password;
    // Determine the user's role based on the username
    if (username.toLowerCase().includes('admin')) {
      this.currentUser.role = 'admin';
    } else {
      this.currentUser.role = 'helper';
      // Handle other roles or situations as needed
      this.currentUser.role = ''; // Default if not admin or helper
    }
    this.http.get<number>(`${this.loginUrl}/basketId/?userName=${username}`)
      .pipe(
        catchError(this.handleError<number>(`basketId/?userName=${username}`))
      )
      .subscribe(basketId => this.setBasketId(basketId));
  }

  getUserRole(): string {
    if (this.currentUser && this.currentUser.role) {
      return this.currentUser.role;
    }
    return ''; // Default to an empty string if the role is not set or the user is not logged in.
  }

  setBasketId(basketId:number){
    if(basketId == undefined){
      this.currentUser.basketId = 1;
    }else{
      this.currentUser.basketId = basketId;
    }
  }

  createLogin(username:string, password:string): Observable<Login>{
    var login: Observable<Login>;
    login = this.http.post<Login>(`${this.loginUrl}`,{userName: username, pass: password, basketId: 0}).pipe(
      catchError(this.handleError<Login>(`post login userName=${username} pass=${password}`))
    );
    return login;
  }

  createBasketforLogin(username: string){
    console.log("CreateBasketforLogin");
    this.http.post<FundingBasket>(`${this.basketUrl}/?userName=${username}`, {userName: username}).pipe(
      catchError(this.handleError<FundingBasket>(`POST basket userName=${username}`))
    ).subscribe(basket => this.addBasketToLogin(username, basket.id));
  }

  createProfileforLogin(username: string){
    console.log("CreateProfileforLogin, username: " + username);
    this.profileService.createProfile({'firstName':'', 'lastName':'', 'email':'', 'country':'', 'userName':username, 'isPrivate': false, 'contributions': {}} as Profile).subscribe();
  }

  addBasketToLogin(username:string, basketId: number){
    console.log("addBasketToLogin");
    this.http.put<Login>(`${this.loginUrl}basketId?userName=${username}&basketId=${basketId}`,{userName: username, basketId: basketId}).pipe(
      catchError(this.handleError<Login>(`PUT basketId userName=${username} basketId=${basketId}`))
    ).subscribe();
    console.log("addBasketToLogin2");
  }

  updateLogin(login:Login, userName:String): Observable<Login>{
    console.log("updateLogin");
    return this.http.put<Login>(`${this.loginUrl}/${userName}`, login, this.httpOptions).pipe(
      tap(_ => console.log(`updated login userName=${login.userName}`)),
      catchError(this.handleError<any>('updateLogin'))
    );
  }

  getCurrentUsername():string{
    return this.currentUser.userName;
  }

  getCurrentBasket():number{
    return this.currentUser.basketId;
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
//todo, link to controller, create a login page, update funding basket and other things if admin or not. create user should interact with funding basket to create a new basket for the user
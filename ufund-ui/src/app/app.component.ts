// In your app.component.ts
import { Component, OnInit } from '@angular/core';
import { LoginService } from './login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'Save The Pandas!';
  loggedIn = false;
  userRole: string = '';

  constructor(private loginService: LoginService) {
  }

  onLogin(logged: boolean) {
    this.loggedIn = logged;
    this.userRole = this.loginService.getUserRole();   
  }

  logOut() {
    this.loggedIn = false;
    this.userRole = '';
  }
}

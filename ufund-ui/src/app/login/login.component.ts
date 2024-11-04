import { Component } from '@angular/core';
import { LoginService } from '../login.service';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { Observable } from 'rxjs';
import { Login } from '../login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = "";
  password = "";
  message = "";
  showPassword: boolean = false;
  @Output() loggedInEmitter = new EventEmitter < boolean > ();

  constructor(private loginService: LoginService){}
  onUsernameUpdate(inputValue: string){
    this.username = inputValue;
  }
  onPasswordUpdate(inputValue: string){
    this.password = inputValue;
  }
  login(){
    if(this.username == "" || this.password == ""){
      this.message = "username or password is empty"
    }else{
      var loggedIn = this.loginService.login(this.username, this.password);
      loggedIn.subscribe(valid => this.loginValid(valid.valueOf()));
    }
  }

  loginValid(valid: boolean){
    if(valid){
      this.loggedInEmitter.emit(true);
    }else{
      this.message = "username or password are incorrect"
    }
  }

  createAccount(){
    if(this.username == "" || this.password == ""){
      this.message = "username or password is empty"
    }else{
      this.loginService.createLogin(this.username, this.password).subscribe((login)=>{
        this.loginResult(login);
        this.loginService.createBasketforLogin(this.username);
        this.loginService.createProfileforLogin(this.username);
      });
    }
  }
  loginResult(login: Login){
    if(login){
      this.message = "Account Created, You can now login with your credentials";
    }else{
      this.message = "Account Creation failed (duplicate username likely)"
    }
  }

  hidePassword(){
    this.showPassword = !this.showPassword;
  }
}

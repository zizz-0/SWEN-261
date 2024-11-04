import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { LoginService } from '../login.service';
import { Profile } from '../profile';
import { Location } from '@angular/common';
import { ProfileService } from '../profile.service';
import { ActivatedRoute } from '@angular/router';
import { Login } from '../login';
import { ContributionsComponent } from '../contributions/contributions.component';
import { FundingBasket } from '../funding-basket';
import { FundingBasketService } from '../funding-basket.service';


@Component({
  selector: 'app-profiles',
  templateUrl: './profiles.component.html',
  styleUrls: ['./profiles.component.css']
})
export class ProfilesComponent {
  userName = "";
  originalUserName = "";
  profileErrorMessage = "";
  privacyLabel = ""
  password = ""
  message = ""
  showPassword: boolean = false;
  profile: Profile | undefined;
  newProfile: Profile | undefined;
  @Output() loggedInEmitter = new EventEmitter < boolean > ();
  

  constructor(private loginService: LoginService, private basketService: FundingBasketService, private location: Location, private profileService: ProfileService) { 
    ContributionsComponent
  }
  
  
  ngOnInit(): void {
    this.originalUserName = this.loginService.getCurrentUsername();
    this.userName = this.loginService.getCurrentUsername();
    this.getProfile();
  }

  goBack(): void {
    this.location.back();
  }

  getProfile(): void {
    this.profileService.getProfile(this.userName).subscribe(profile => {
    this.profile = profile
      if(!this.profile){
        this.profileService.createProfile({'firstName':'', 'lastName':'', 'email':'', 'country':'', 'userName':this.userName, 'isPrivate': false, 'contributions': {}} as Profile)
          .subscribe(); 
          this.privacyLabel = 'Your account is Public';
          this.getProfile()

      }else{
        if(this.profile.isPrivate == false){
          this.privacyLabel = 'Your account is Public';
        }else{
          this.privacyLabel = 'Your account is Private';
        }
      }
    });

    
  }

  save(): void {
    if(this.profile){
      this.profileService.updateProfile(this.profile).subscribe();
    }
    
  }

  saveLogin(): void {
    if(this.userName && this.password && this.profile){

      this.loginService.updateLogin({'userName': this.userName, 'pass':this.password, 'basketId':Number(this.loginService.getCurrentBasket()), 
      'role':String(this.loginService.getUserRole)}as Login, this.originalUserName).subscribe((login)=>this.loginResult(login));

      this.basketService.setUsername(this.loginService.getCurrentBasket(), this.userName).subscribe()

      this.profileService.getProfile(this.originalUserName).subscribe(newProfile => {
        this.newProfile = newProfile

        this.profileService.createProfile({'firstName':this.newProfile.firstName, 'lastName':this.newProfile.lastName, 'email':this.newProfile.email, 'country':this.newProfile.country, 'userName':this.userName, 'isPrivate': this.newProfile.isPrivate, 'contributions': this.newProfile.contributions} as Profile).subscribe();

        // this.newProfile.contributions;

      });

      this.save();
    }
    
  }

  loginResult(login: Login){
    if(login){
      this.message = "Account Updated, You can now login with your credentials";
    }else{
      this.message = "Account Creation failed (duplicate username likely)"
    }
  }

 

  switchPrivacy(): void {
    
    if(this.profile){
      this.profileService.switchPrivacy(this.profile).subscribe(profile =>{ 
        if(profile){
          this.profile = profile;
        }
      });
    }

    if (this.privacyLabel ==  'Your account is Public'){
      this.privacyLabel = 'Your account is Private'
    }else{
      this.privacyLabel = 'Your account is Public'
    }
    
  }

  hidePassword(){
    this.showPassword = !this.showPassword;
  }

}

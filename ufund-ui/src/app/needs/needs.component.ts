import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { FundingBasketService } from '../funding-basket.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrls: ['./needs.component.css']
})
export class NeedsComponent implements OnInit {
  needs: Need[] = [];
  isAdmin = false;
  isUrgent: boolean | undefined;
  needType = 'EQUIPMENT';
  needUrgency = 'LOW';
  urgencyImage = 'assets/images/blank.jpg';
  needErrorMessage = '';
  needImage = 'assets/images/EQUIPMENT.jpg'

  constructor(
    private needService: NeedService, 
    private basketService: FundingBasketService, 
    private loginService: LoginService) { }

  ngOnInit(): void {
    this.getNeeds();
    if(this.loginService.currentUser.userName == "admin"){
      this.isAdmin = true;
    }
  }

  getNeeds(): void {
    this.needService.getAllNeeds()
    .subscribe(needs => this.needs = needs);
  }

  add(name: string, price: string, type: string, quantity: string, description: string, urgency: string): void {
    name = name.trim();
    description = description.trim();
    var priceNumber:number = parseFloat(price);
    priceNumber = Math.round(priceNumber * 100) / 100
    var quantityNumber:number = parseInt(quantity);
    if (!name){
      this.needErrorMessage = "No name given for need";
      return;
    }
    if (!description){
      this.needErrorMessage = "No description given for need";
      return;
    }
    if(!priceNumber){
      this.needErrorMessage = "invalid or empty price given";
      return;
    }
    if(!quantityNumber){
      this.needErrorMessage = "Invalid or empty quanitity given";
      return;
    }
    if(quantityNumber > 999){
      this.needErrorMessage = "Quantity exceedes maximum of 999";
      return;
    }
    if(quantityNumber < 1){
      this.needErrorMessage = "Cannot set quantity to a value less than 1";
      return;
    }
    if(priceNumber < 0.01){
      this.needErrorMessage = "Cannot set price less than 1 cent";
      return;
    }
    if(!type){
      this.needErrorMessage = "No type selected";
      return;
    }
    if(!urgency){
      this.needErrorMessage = "No urgency selected";
      return;
    }

    if(this.needUrgency == 'HIGH'){
      this.urgencyImage = 'assets/images/urgent.jpg';
    }
    this.needService.addNeed({ name: name, type: this.needType, price: priceNumber, quantityNeeded: quantityNumber, quantityFulfilled: 0, urgency: this.needUrgency, urgencyImage : this.urgencyImage, description:description, image: this.needImage} as Need)
      .subscribe(need => {if(need){
        this.needs.push(need);
      }else{
        this.needErrorMessage = "error adding need (duplicate name likely)";
      }});
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(h => h !== need);
    this.needService.deleteNeed(need.id).subscribe();
  }

  needTypeSelection(event:any):void{
    this.needType =event.target.value;
  }

  needImageSelection(event:any):void{
    this.needImage = event.target.value;
  }

  needUrgencySelection(event:any):void{
    this.needUrgency =event.target.value;
  }

  addToBasket(need: Need): void{
    this.basketService.addNeed(this.loginService.currentUser.basketId, need.id, 1).subscribe(); //TODO, connect this to a user service so the 1 becomes whatever the basket id of the current logged in user is
  }

}
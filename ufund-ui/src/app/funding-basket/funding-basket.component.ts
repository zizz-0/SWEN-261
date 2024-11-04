import { Component, OnInit } from '@angular/core';
import { FundingBasketService } from '../funding-basket.service';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { ActivatedRoute } from '@angular/router';
import { LoginService } from '../login.service';
import { ProfileService } from '../profile.service';
import { FundingBasket } from '../funding-basket';

@Component({
  selector: 'app-funding-basket',
  templateUrl: './funding-basket.component.html',
  styleUrls: ['./funding-basket.component.css']
})
export class FundingBasketComponent implements OnInit{

  needMap: Map<Need, number> = new Map;
  needs: Need[] = [];
  basketId: number;
  totalPrice: number = 0;
  userName: string ="";

  constructor(private needService: NeedService, private fundingBasketService: 
    FundingBasketService, private loginService: LoginService, private route: ActivatedRoute,private profileService:ProfileService){
      this.basketId = this.loginService.getCurrentBasket();
    }
  ngOnInit(): void {
    this.getNeeds();
    this.calculateTotalPrice();
    this.fundingBasketService.getBasket(this.basketId).subscribe((fundingBasket: FundingBasket) => {
      this.userName = fundingBasket.userName;
      console.log(this.userName);
    });
  }

  calculateTotalPrice(): void {
    this.totalPrice = 0;
    this.needMap.forEach((quantity: number, need: Need)=> this.totalPrice+=(need.price * quantity));
  }

  getNeeds(): void{
    //gets needs only once need ids are returned
    this.fundingBasketService.getNeeds(this.basketId).subscribe(result => {
      if(result !== undefined){
        var map = new Map(Object.entries(result));
        console.log(map);
        console.log(Array.from(map.keys()));
        this.needService.getNeeds(Array.from(map.keys()).map(Number)).subscribe(needs => {
          this.needs = needs;
          for(const need of needs){
            this.needMap.set(need as Need, map.get(need.id.toString()) as number);
          }
          this.calculateTotalPrice(); // Calculate total price after getting needs
        });
      }
    });
    console.log(this.needMap);
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(h => h !== need);
    this.needMap.delete(need);
    this.fundingBasketService.removeNeed(this.basketId, need.id).subscribe(() => {
    this.calculateTotalPrice();
    });
  }

  checkout():void{
    this.needMap.forEach(
      (quantity: number, need: Need)=>
      {this.needService.fulfillNeedQuantity(need.id, quantity).subscribe();
      this.profileService.addContribution(this.userName, need.id, quantity).subscribe();
      });
    this.fundingBasketService.clearBasket(this.basketId).subscribe(()=>{
      this.needMap = new Map<Need, number>();
      this.totalPrice = 0;
      this.getNeeds();
    });
  }

  changeQuantity(need: Need, amount: number):void{
    var currentQ = this.needMap.get(need);
    if(currentQ !== undefined){
      if(currentQ + amount < 1){
        this.delete(need);
        return;
      }
      this.needMap.set(need, currentQ+amount);
      this.fundingBasketService.updateQuantity(this.basketId, need.id, amount);
      this.calculateTotalPrice();
    }
  }

}

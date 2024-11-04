import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { NeedService } from '../need.service';
import { FundingBasketService } from '../funding-basket.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-need-detail',
  templateUrl: './need-detail.component.html',
  styleUrls: ['./need-detail.component.css']
})
export class NeedDetailComponent implements OnInit {
  need: Need | undefined;
  userRole: string = '';
  quantityToAdd = 1;
  message = '';

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private location: Location,
    private basketService: FundingBasketService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.getNeed();
    this.setUserRole();
  }

  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needService.getNeed(id).subscribe(need => (this.need = need));
  }

  goBack(): void {
    this.location.back();
  }

  setUserRole(): void {
    this.userRole = this.loginService.getUserRole();
  }

  save(): void {
    if (this.userRole === 'admin' && this.need) {
      
      this.needService.updateNeed(this.need).subscribe(() => this.goBack());
      
    }
  }

  addToBasket(): void {
    if (this.need !== undefined && this.quantityToAdd > 0 && this.quantityToAdd <= (this.need.quantityNeeded - this.need.quantityFulfilled)) {
      this.basketService
        .addNeed(this.loginService.currentUser.basketId, this.need.id, this.quantityToAdd)
        .subscribe();
      this.message = 'Added ' + this.quantityToAdd + ' Need(s) to Basket';
    } else if (this.need !== undefined && this.need.quantityFulfilled >= this.need.quantityNeeded) {
      this.message = 'Sorry, we do not need any more contributions for this need.'
    } else {
      this.message = 'Invalid Need - Quantity must be between 1 and specified Quantity Needed';
    }
  }
}

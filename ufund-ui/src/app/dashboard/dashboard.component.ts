import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  needs: Need[] = [];
  quantityNeeded: number = 1;
  quantityFulfilled: number = 0;

  constructor(private needService: NeedService) { }

  ngOnInit(): void {
    this.getNeeds();
    this.getProgress();
  }

  getNeeds(): void {
    this.needService.getAllNeeds()
      .subscribe(needs => this.needs = needs.slice(0, 4));
  }

  getProgress(): void{
    this.needService.getProgress().subscribe((progress)=>{
                                  this.quantityNeeded = progress[0]; 
                                  this.quantityFulfilled = progress[1];});
  }
}
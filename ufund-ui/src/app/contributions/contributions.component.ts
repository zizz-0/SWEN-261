import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { ProfileService } from '../profile.service';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { Profile } from '../profile';


@Component({
  selector: 'app-contributions',
  templateUrl: './contributions.component.html',
  styleUrls: ['./contributions.component.css']
})
export class ContributionsComponent {

  needMap: Map<Need, number> = new Map<Need,number>();
  total: number = 0;

  @Input({required: true}) userName: string = "";
  
  constructor(private profileService: ProfileService, private needService: NeedService){
  }
  ngOnInit(){
    this.profileService.getProfile(this.userName).subscribe((profile: Profile)=> this.setContributions(profile));
  }
  setContributions(profile: Profile){
    var contributions = new Map(Object.entries(profile.contributions));
    this.total = 0;
    contributions.forEach((quantity, needId) => {
      this.needService.getNeed(Number(needId)).subscribe((need: Need) => {
        this.needMap.set(need, Number(quantity));
        this.total+=need.price*Number(quantity);
      });
    });
  }
}

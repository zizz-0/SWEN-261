import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { NeedsComponent } from './needs/needs.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { FundingBasketComponent } from './funding-basket/funding-basket.component';
import { ProfilesComponent } from './profiles/profiles.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'detail/:id', component: NeedDetailComponent },
  { path: 'basket', component: FundingBasketComponent},
  { path: 'needs', component: NeedsComponent },
  { path: 'needs', component: NeedsComponent },
  { path: 'profiles', component: ProfilesComponent },
  { path: 'leaderboard', component: LeaderboardComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgOptimizedImage } from '@angular/common'
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedsComponent } from './needs/needs.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { FundingBasketComponent } from './funding-basket/funding-basket.component';
import { LoginComponent } from './login/login.component';
import { ProfilesComponent } from './profiles/profiles.component';
import { ContributionsComponent } from './contributions/contributions.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    
  ],
  declarations: [
    AppComponent,
    DashboardComponent,
    NeedsComponent,
    NeedDetailComponent,
    NeedSearchComponent,
    FundingBasketComponent,
    LoginComponent,
    ProfilesComponent,
    ContributionsComponent,
    LeaderboardComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
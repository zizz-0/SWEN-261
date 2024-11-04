import { Component, OnInit } from '@angular/core';
import { LeaderboardService } from '../leaderboard.service';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit{
  ranks: String[][] = [];
  counts: Map<String, number> = new Map;
 
  constructor(private leaderboardService: LeaderboardService ){}

  ngOnInit(): void {
    this.leaderboardService.getLeaderboard().subscribe(ranks => this.ranks = ranks);
  }
}


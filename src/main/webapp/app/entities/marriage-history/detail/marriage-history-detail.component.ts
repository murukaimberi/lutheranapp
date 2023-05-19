import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMarriageHistory } from '../marriage-history.model';

@Component({
  selector: 'lutheran-marriage-history-detail',
  templateUrl: './marriage-history-detail.component.html',
})
export class MarriageHistoryDetailComponent implements OnInit {
  marriageHistory: IMarriageHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marriageHistory }) => {
      this.marriageHistory = marriageHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

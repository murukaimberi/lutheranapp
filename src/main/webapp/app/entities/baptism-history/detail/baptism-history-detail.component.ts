import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBaptismHistory } from '../baptism-history.model';

@Component({
  selector: 'lutheran-baptism-history-detail',
  templateUrl: './baptism-history-detail.component.html',
})
export class BaptismHistoryDetailComponent implements OnInit {
  baptismHistory: IBaptismHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ baptismHistory }) => {
      this.baptismHistory = baptismHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

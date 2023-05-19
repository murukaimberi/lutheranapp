import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContribution } from '../contribution.model';

@Component({
  selector: 'lutheran-contribution-detail',
  templateUrl: './contribution-detail.component.html',
})
export class ContributionDetailComponent implements OnInit {
  contribution: IContribution | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contribution }) => {
      this.contribution = contribution;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

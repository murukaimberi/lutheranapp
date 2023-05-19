import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegion } from '../region.model';

@Component({
  selector: 'lutheran-region-detail',
  templateUrl: './region-detail.component.html',
})
export class RegionDetailComponent implements OnInit {
  region: IRegion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ region }) => {
      this.region = region;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDependant } from '../dependant.model';

@Component({
  selector: 'lutheran-dependant-detail',
  templateUrl: './dependant-detail.component.html',
})
export class DependantDetailComponent implements OnInit {
  dependant: IDependant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dependant }) => {
      this.dependant = dependant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

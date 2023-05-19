import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BaptismHistoryFormService, BaptismHistoryFormGroup } from './baptism-history-form.service';
import { IBaptismHistory } from '../baptism-history.model';
import { BaptismHistoryService } from '../service/baptism-history.service';

@Component({
  selector: 'lutheran-baptism-history-update',
  templateUrl: './baptism-history-update.component.html',
})
export class BaptismHistoryUpdateComponent implements OnInit {
  isSaving = false;
  baptismHistory: IBaptismHistory | null = null;

  editForm: BaptismHistoryFormGroup = this.baptismHistoryFormService.createBaptismHistoryFormGroup();

  constructor(
    protected baptismHistoryService: BaptismHistoryService,
    protected baptismHistoryFormService: BaptismHistoryFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ baptismHistory }) => {
      this.baptismHistory = baptismHistory;
      if (baptismHistory) {
        this.updateForm(baptismHistory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const baptismHistory = this.baptismHistoryFormService.getBaptismHistory(this.editForm);
    if (baptismHistory.id !== null) {
      this.subscribeToSaveResponse(this.baptismHistoryService.update(baptismHistory));
    } else {
      this.subscribeToSaveResponse(this.baptismHistoryService.create(baptismHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBaptismHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(baptismHistory: IBaptismHistory): void {
    this.baptismHistory = baptismHistory;
    this.baptismHistoryFormService.resetForm(this.editForm, baptismHistory);
  }
}

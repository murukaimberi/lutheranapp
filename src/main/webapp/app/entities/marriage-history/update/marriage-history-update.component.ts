import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MarriageHistoryFormService, MarriageHistoryFormGroup } from './marriage-history-form.service';
import { IMarriageHistory } from '../marriage-history.model';
import { MarriageHistoryService } from '../service/marriage-history.service';

@Component({
  selector: 'lutheran-marriage-history-update',
  templateUrl: './marriage-history-update.component.html',
})
export class MarriageHistoryUpdateComponent implements OnInit {
  isSaving = false;
  marriageHistory: IMarriageHistory | null = null;

  editForm: MarriageHistoryFormGroup = this.marriageHistoryFormService.createMarriageHistoryFormGroup();

  constructor(
    protected marriageHistoryService: MarriageHistoryService,
    protected marriageHistoryFormService: MarriageHistoryFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marriageHistory }) => {
      this.marriageHistory = marriageHistory;
      if (marriageHistory) {
        this.updateForm(marriageHistory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const marriageHistory = this.marriageHistoryFormService.getMarriageHistory(this.editForm);
    if (marriageHistory.id !== null) {
      this.subscribeToSaveResponse(this.marriageHistoryService.update(marriageHistory));
    } else {
      this.subscribeToSaveResponse(this.marriageHistoryService.create(marriageHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarriageHistory>>): void {
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

  protected updateForm(marriageHistory: IMarriageHistory): void {
    this.marriageHistory = marriageHistory;
    this.marriageHistoryFormService.resetForm(this.editForm, marriageHistory);
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContributionFormService, ContributionFormGroup } from './contribution-form.service';
import { IContribution } from '../contribution.model';
import { ContributionService } from '../service/contribution.service';
import { ICongregant } from 'app/entities/congregant/congregant.model';
import { CongregantService } from 'app/entities/congregant/service/congregant.service';
import { ContributionType } from 'app/entities/enumerations/contribution-type.model';
import { Frequency } from 'app/entities/enumerations/frequency.model';

@Component({
  selector: 'lutheran-contribution-update',
  templateUrl: './contribution-update.component.html',
})
export class ContributionUpdateComponent implements OnInit {
  isSaving = false;
  contribution: IContribution | null = null;
  contributionTypeValues = Object.keys(ContributionType);
  frequencyValues = Object.keys(Frequency);

  congregantsSharedCollection: ICongregant[] = [];

  editForm: ContributionFormGroup = this.contributionFormService.createContributionFormGroup();

  constructor(
    protected contributionService: ContributionService,
    protected contributionFormService: ContributionFormService,
    protected congregantService: CongregantService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCongregant = (o1: ICongregant | null, o2: ICongregant | null): boolean => this.congregantService.compareCongregant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contribution }) => {
      this.contribution = contribution;
      if (contribution) {
        this.updateForm(contribution);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contribution = this.contributionFormService.getContribution(this.editForm);
    if (contribution.id !== null) {
      this.subscribeToSaveResponse(this.contributionService.update(contribution));
    } else {
      this.subscribeToSaveResponse(this.contributionService.create(contribution));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContribution>>): void {
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

  protected updateForm(contribution: IContribution): void {
    this.contribution = contribution;
    this.contributionFormService.resetForm(this.editForm, contribution);

    this.congregantsSharedCollection = this.congregantService.addCongregantToCollectionIfMissing<ICongregant>(
      this.congregantsSharedCollection,
      contribution.congregant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.congregantService
      .query()
      .pipe(map((res: HttpResponse<ICongregant[]>) => res.body ?? []))
      .pipe(
        map((congregants: ICongregant[]) =>
          this.congregantService.addCongregantToCollectionIfMissing<ICongregant>(congregants, this.contribution?.congregant)
        )
      )
      .subscribe((congregants: ICongregant[]) => (this.congregantsSharedCollection = congregants));
  }
}

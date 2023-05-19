import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DependantFormService, DependantFormGroup } from './dependant-form.service';
import { IDependant } from '../dependant.model';
import { DependantService } from '../service/dependant.service';
import { ICongregant } from 'app/entities/congregant/congregant.model';
import { CongregantService } from 'app/entities/congregant/service/congregant.service';
import { Gender } from 'app/entities/enumerations/gender.model';

@Component({
  selector: 'lutheran-dependant-update',
  templateUrl: './dependant-update.component.html',
})
export class DependantUpdateComponent implements OnInit {
  isSaving = false;
  dependant: IDependant | null = null;
  genderValues = Object.keys(Gender);

  congregantsSharedCollection: ICongregant[] = [];

  editForm: DependantFormGroup = this.dependantFormService.createDependantFormGroup();

  constructor(
    protected dependantService: DependantService,
    protected dependantFormService: DependantFormService,
    protected congregantService: CongregantService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCongregant = (o1: ICongregant | null, o2: ICongregant | null): boolean => this.congregantService.compareCongregant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dependant }) => {
      this.dependant = dependant;
      if (dependant) {
        this.updateForm(dependant);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dependant = this.dependantFormService.getDependant(this.editForm);
    if (dependant.id !== null) {
      this.subscribeToSaveResponse(this.dependantService.update(dependant));
    } else {
      this.subscribeToSaveResponse(this.dependantService.create(dependant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDependant>>): void {
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

  protected updateForm(dependant: IDependant): void {
    this.dependant = dependant;
    this.dependantFormService.resetForm(this.editForm, dependant);

    this.congregantsSharedCollection = this.congregantService.addCongregantToCollectionIfMissing<ICongregant>(
      this.congregantsSharedCollection,
      dependant.congregant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.congregantService
      .query()
      .pipe(map((res: HttpResponse<ICongregant[]>) => res.body ?? []))
      .pipe(
        map((congregants: ICongregant[]) =>
          this.congregantService.addCongregantToCollectionIfMissing<ICongregant>(congregants, this.dependant?.congregant)
        )
      )
      .subscribe((congregants: ICongregant[]) => (this.congregantsSharedCollection = congregants));
  }
}

import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CongregantFormService, CongregantFormGroup } from './congregant-form.service';
import { ICongregant } from '../congregant.model';
import { CongregantService } from '../service/congregant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMarriageHistory } from 'app/entities/marriage-history/marriage-history.model';
import { MarriageHistoryService } from 'app/entities/marriage-history/service/marriage-history.service';
import { IBaptismHistory } from 'app/entities/baptism-history/baptism-history.model';
import { BaptismHistoryService } from 'app/entities/baptism-history/service/baptism-history.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';

@Component({
  selector: 'lutheran-congregant-update',
  templateUrl: './congregant-update.component.html',
})
export class CongregantUpdateComponent implements OnInit {
  isSaving = false;
  congregant: ICongregant | null = null;
  genderValues = Object.keys(Gender);
  maritalStatusValues = Object.keys(MaritalStatus);

  marriageHistoriesCollection: IMarriageHistory[] = [];
  baptismHistoriesCollection: IBaptismHistory[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: CongregantFormGroup = this.congregantFormService.createCongregantFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected congregantService: CongregantService,
    protected congregantFormService: CongregantFormService,
    protected marriageHistoryService: MarriageHistoryService,
    protected baptismHistoryService: BaptismHistoryService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMarriageHistory = (o1: IMarriageHistory | null, o2: IMarriageHistory | null): boolean =>
    this.marriageHistoryService.compareMarriageHistory(o1, o2);

  compareBaptismHistory = (o1: IBaptismHistory | null, o2: IBaptismHistory | null): boolean =>
    this.baptismHistoryService.compareBaptismHistory(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ congregant }) => {
      this.congregant = congregant;
      if (congregant) {
        this.updateForm(congregant);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('lutheranappApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const congregant = this.congregantFormService.getCongregant(this.editForm);
    if (congregant.id !== null) {
      this.subscribeToSaveResponse(this.congregantService.update(congregant));
    } else {
      this.subscribeToSaveResponse(this.congregantService.create(congregant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICongregant>>): void {
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

  protected updateForm(congregant: ICongregant): void {
    this.congregant = congregant;
    this.congregantFormService.resetForm(this.editForm, congregant);

    this.marriageHistoriesCollection = this.marriageHistoryService.addMarriageHistoryToCollectionIfMissing<IMarriageHistory>(
      this.marriageHistoriesCollection,
      congregant.marriageHistory
    );
    this.baptismHistoriesCollection = this.baptismHistoryService.addBaptismHistoryToCollectionIfMissing<IBaptismHistory>(
      this.baptismHistoriesCollection,
      congregant.baptismHistory
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, congregant.user);
  }

  protected loadRelationshipsOptions(): void {
    this.marriageHistoryService
      .query({ filter: 'congregant-is-null' })
      .pipe(map((res: HttpResponse<IMarriageHistory[]>) => res.body ?? []))
      .pipe(
        map((marriageHistories: IMarriageHistory[]) =>
          this.marriageHistoryService.addMarriageHistoryToCollectionIfMissing<IMarriageHistory>(
            marriageHistories,
            this.congregant?.marriageHistory
          )
        )
      )
      .subscribe((marriageHistories: IMarriageHistory[]) => (this.marriageHistoriesCollection = marriageHistories));

    this.baptismHistoryService
      .query({ filter: 'congragant-is-null' })
      .pipe(map((res: HttpResponse<IBaptismHistory[]>) => res.body ?? []))
      .pipe(
        map((baptismHistories: IBaptismHistory[]) =>
          this.baptismHistoryService.addBaptismHistoryToCollectionIfMissing<IBaptismHistory>(
            baptismHistories,
            this.congregant?.baptismHistory
          )
        )
      )
      .subscribe((baptismHistories: IBaptismHistory[]) => (this.baptismHistoriesCollection = baptismHistories));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.congregant?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}

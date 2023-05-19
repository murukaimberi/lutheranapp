import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LeagueFormService, LeagueFormGroup } from './league-form.service';
import { ILeague } from '../league.model';
import { LeagueService } from '../service/league.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICongregant } from 'app/entities/congregant/congregant.model';
import { CongregantService } from 'app/entities/congregant/service/congregant.service';

@Component({
  selector: 'lutheran-league-update',
  templateUrl: './league-update.component.html',
})
export class LeagueUpdateComponent implements OnInit {
  isSaving = false;
  league: ILeague | null = null;

  congregantsSharedCollection: ICongregant[] = [];

  editForm: LeagueFormGroup = this.leagueFormService.createLeagueFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected leagueService: LeagueService,
    protected leagueFormService: LeagueFormService,
    protected congregantService: CongregantService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCongregant = (o1: ICongregant | null, o2: ICongregant | null): boolean => this.congregantService.compareCongregant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ league }) => {
      this.league = league;
      if (league) {
        this.updateForm(league);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const league = this.leagueFormService.getLeague(this.editForm);
    if (league.id !== null) {
      this.subscribeToSaveResponse(this.leagueService.update(league));
    } else {
      this.subscribeToSaveResponse(this.leagueService.create(league));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeague>>): void {
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

  protected updateForm(league: ILeague): void {
    this.league = league;
    this.leagueFormService.resetForm(this.editForm, league);

    this.congregantsSharedCollection = this.congregantService.addCongregantToCollectionIfMissing<ICongregant>(
      this.congregantsSharedCollection,
      ...(league.congregants ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.congregantService
      .query()
      .pipe(map((res: HttpResponse<ICongregant[]>) => res.body ?? []))
      .pipe(
        map((congregants: ICongregant[]) =>
          this.congregantService.addCongregantToCollectionIfMissing<ICongregant>(congregants, ...(this.league?.congregants ?? []))
        )
      )
      .subscribe((congregants: ICongregant[]) => (this.congregantsSharedCollection = congregants));
  }
}

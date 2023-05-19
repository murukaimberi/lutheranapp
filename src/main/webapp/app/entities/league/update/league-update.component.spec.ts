import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LeagueFormService } from './league-form.service';
import { LeagueService } from '../service/league.service';
import { ILeague } from '../league.model';
import { ICongregant } from 'app/entities/congregant/congregant.model';
import { CongregantService } from 'app/entities/congregant/service/congregant.service';

import { LeagueUpdateComponent } from './league-update.component';

describe('League Management Update Component', () => {
  let comp: LeagueUpdateComponent;
  let fixture: ComponentFixture<LeagueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leagueFormService: LeagueFormService;
  let leagueService: LeagueService;
  let congregantService: CongregantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LeagueUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LeagueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeagueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leagueFormService = TestBed.inject(LeagueFormService);
    leagueService = TestBed.inject(LeagueService);
    congregantService = TestBed.inject(CongregantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Congregant query and add missing value', () => {
      const league: ILeague = { id: 456 };
      const congregants: ICongregant[] = [{ id: 89289 }];
      league.congregants = congregants;

      const congregantCollection: ICongregant[] = [{ id: 88736 }];
      jest.spyOn(congregantService, 'query').mockReturnValue(of(new HttpResponse({ body: congregantCollection })));
      const additionalCongregants = [...congregants];
      const expectedCollection: ICongregant[] = [...additionalCongregants, ...congregantCollection];
      jest.spyOn(congregantService, 'addCongregantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ league });
      comp.ngOnInit();

      expect(congregantService.query).toHaveBeenCalled();
      expect(congregantService.addCongregantToCollectionIfMissing).toHaveBeenCalledWith(
        congregantCollection,
        ...additionalCongregants.map(expect.objectContaining)
      );
      expect(comp.congregantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const league: ILeague = { id: 456 };
      const congregants: ICongregant = { id: 79492 };
      league.congregants = [congregants];

      activatedRoute.data = of({ league });
      comp.ngOnInit();

      expect(comp.congregantsSharedCollection).toContain(congregants);
      expect(comp.league).toEqual(league);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeague>>();
      const league = { id: 123 };
      jest.spyOn(leagueFormService, 'getLeague').mockReturnValue(league);
      jest.spyOn(leagueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ league });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: league }));
      saveSubject.complete();

      // THEN
      expect(leagueFormService.getLeague).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(leagueService.update).toHaveBeenCalledWith(expect.objectContaining(league));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeague>>();
      const league = { id: 123 };
      jest.spyOn(leagueFormService, 'getLeague').mockReturnValue({ id: null });
      jest.spyOn(leagueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ league: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: league }));
      saveSubject.complete();

      // THEN
      expect(leagueFormService.getLeague).toHaveBeenCalled();
      expect(leagueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeague>>();
      const league = { id: 123 };
      jest.spyOn(leagueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ league });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leagueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCongregant', () => {
      it('Should forward to congregantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(congregantService, 'compareCongregant');
        comp.compareCongregant(entity, entity2);
        expect(congregantService.compareCongregant).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

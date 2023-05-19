import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CongregantFormService } from './congregant-form.service';
import { CongregantService } from '../service/congregant.service';
import { ICongregant } from '../congregant.model';
import { IMarriageHistory } from 'app/entities/marriage-history/marriage-history.model';
import { MarriageHistoryService } from 'app/entities/marriage-history/service/marriage-history.service';
import { IBaptismHistory } from 'app/entities/baptism-history/baptism-history.model';
import { BaptismHistoryService } from 'app/entities/baptism-history/service/baptism-history.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CongregantUpdateComponent } from './congregant-update.component';

describe('Congregant Management Update Component', () => {
  let comp: CongregantUpdateComponent;
  let fixture: ComponentFixture<CongregantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let congregantFormService: CongregantFormService;
  let congregantService: CongregantService;
  let marriageHistoryService: MarriageHistoryService;
  let baptismHistoryService: BaptismHistoryService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CongregantUpdateComponent],
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
      .overrideTemplate(CongregantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CongregantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    congregantFormService = TestBed.inject(CongregantFormService);
    congregantService = TestBed.inject(CongregantService);
    marriageHistoryService = TestBed.inject(MarriageHistoryService);
    baptismHistoryService = TestBed.inject(BaptismHistoryService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call marriageHistory query and add missing value', () => {
      const congregant: ICongregant = { id: 456 };
      const marriageHistory: IMarriageHistory = { id: 19530 };
      congregant.marriageHistory = marriageHistory;

      const marriageHistoryCollection: IMarriageHistory[] = [{ id: 6399 }];
      jest.spyOn(marriageHistoryService, 'query').mockReturnValue(of(new HttpResponse({ body: marriageHistoryCollection })));
      const expectedCollection: IMarriageHistory[] = [marriageHistory, ...marriageHistoryCollection];
      jest.spyOn(marriageHistoryService, 'addMarriageHistoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ congregant });
      comp.ngOnInit();

      expect(marriageHistoryService.query).toHaveBeenCalled();
      expect(marriageHistoryService.addMarriageHistoryToCollectionIfMissing).toHaveBeenCalledWith(
        marriageHistoryCollection,
        marriageHistory
      );
      expect(comp.marriageHistoriesCollection).toEqual(expectedCollection);
    });

    it('Should call baptismHistory query and add missing value', () => {
      const congregant: ICongregant = { id: 456 };
      const baptismHistory: IBaptismHistory = { id: 58950 };
      congregant.baptismHistory = baptismHistory;

      const baptismHistoryCollection: IBaptismHistory[] = [{ id: 8115 }];
      jest.spyOn(baptismHistoryService, 'query').mockReturnValue(of(new HttpResponse({ body: baptismHistoryCollection })));
      const expectedCollection: IBaptismHistory[] = [baptismHistory, ...baptismHistoryCollection];
      jest.spyOn(baptismHistoryService, 'addBaptismHistoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ congregant });
      comp.ngOnInit();

      expect(baptismHistoryService.query).toHaveBeenCalled();
      expect(baptismHistoryService.addBaptismHistoryToCollectionIfMissing).toHaveBeenCalledWith(baptismHistoryCollection, baptismHistory);
      expect(comp.baptismHistoriesCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const congregant: ICongregant = { id: 456 };
      const user: IUser = { id: 36632 };
      congregant.user = user;

      const userCollection: IUser[] = [{ id: 25033 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ congregant });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const congregant: ICongregant = { id: 456 };
      const marriageHistory: IMarriageHistory = { id: 21656 };
      congregant.marriageHistory = marriageHistory;
      const baptismHistory: IBaptismHistory = { id: 52450 };
      congregant.baptismHistory = baptismHistory;
      const user: IUser = { id: 48523 };
      congregant.user = user;

      activatedRoute.data = of({ congregant });
      comp.ngOnInit();

      expect(comp.marriageHistoriesCollection).toContain(marriageHistory);
      expect(comp.baptismHistoriesCollection).toContain(baptismHistory);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.congregant).toEqual(congregant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICongregant>>();
      const congregant = { id: 123 };
      jest.spyOn(congregantFormService, 'getCongregant').mockReturnValue(congregant);
      jest.spyOn(congregantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ congregant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: congregant }));
      saveSubject.complete();

      // THEN
      expect(congregantFormService.getCongregant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(congregantService.update).toHaveBeenCalledWith(expect.objectContaining(congregant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICongregant>>();
      const congregant = { id: 123 };
      jest.spyOn(congregantFormService, 'getCongregant').mockReturnValue({ id: null });
      jest.spyOn(congregantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ congregant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: congregant }));
      saveSubject.complete();

      // THEN
      expect(congregantFormService.getCongregant).toHaveBeenCalled();
      expect(congregantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICongregant>>();
      const congregant = { id: 123 };
      jest.spyOn(congregantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ congregant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(congregantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMarriageHistory', () => {
      it('Should forward to marriageHistoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(marriageHistoryService, 'compareMarriageHistory');
        comp.compareMarriageHistory(entity, entity2);
        expect(marriageHistoryService.compareMarriageHistory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBaptismHistory', () => {
      it('Should forward to baptismHistoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(baptismHistoryService, 'compareBaptismHistory');
        comp.compareBaptismHistory(entity, entity2);
        expect(baptismHistoryService.compareBaptismHistory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

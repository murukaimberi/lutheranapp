import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MarriageHistoryFormService } from './marriage-history-form.service';
import { MarriageHistoryService } from '../service/marriage-history.service';
import { IMarriageHistory } from '../marriage-history.model';

import { MarriageHistoryUpdateComponent } from './marriage-history-update.component';

describe('MarriageHistory Management Update Component', () => {
  let comp: MarriageHistoryUpdateComponent;
  let fixture: ComponentFixture<MarriageHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let marriageHistoryFormService: MarriageHistoryFormService;
  let marriageHistoryService: MarriageHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MarriageHistoryUpdateComponent],
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
      .overrideTemplate(MarriageHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MarriageHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    marriageHistoryFormService = TestBed.inject(MarriageHistoryFormService);
    marriageHistoryService = TestBed.inject(MarriageHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const marriageHistory: IMarriageHistory = { id: 456 };

      activatedRoute.data = of({ marriageHistory });
      comp.ngOnInit();

      expect(comp.marriageHistory).toEqual(marriageHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMarriageHistory>>();
      const marriageHistory = { id: 123 };
      jest.spyOn(marriageHistoryFormService, 'getMarriageHistory').mockReturnValue(marriageHistory);
      jest.spyOn(marriageHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marriageHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marriageHistory }));
      saveSubject.complete();

      // THEN
      expect(marriageHistoryFormService.getMarriageHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(marriageHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(marriageHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMarriageHistory>>();
      const marriageHistory = { id: 123 };
      jest.spyOn(marriageHistoryFormService, 'getMarriageHistory').mockReturnValue({ id: null });
      jest.spyOn(marriageHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marriageHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marriageHistory }));
      saveSubject.complete();

      // THEN
      expect(marriageHistoryFormService.getMarriageHistory).toHaveBeenCalled();
      expect(marriageHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMarriageHistory>>();
      const marriageHistory = { id: 123 };
      jest.spyOn(marriageHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marriageHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(marriageHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

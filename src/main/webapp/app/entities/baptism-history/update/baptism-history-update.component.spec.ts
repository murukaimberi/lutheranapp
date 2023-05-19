import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BaptismHistoryFormService } from './baptism-history-form.service';
import { BaptismHistoryService } from '../service/baptism-history.service';
import { IBaptismHistory } from '../baptism-history.model';

import { BaptismHistoryUpdateComponent } from './baptism-history-update.component';

describe('BaptismHistory Management Update Component', () => {
  let comp: BaptismHistoryUpdateComponent;
  let fixture: ComponentFixture<BaptismHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let baptismHistoryFormService: BaptismHistoryFormService;
  let baptismHistoryService: BaptismHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BaptismHistoryUpdateComponent],
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
      .overrideTemplate(BaptismHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BaptismHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    baptismHistoryFormService = TestBed.inject(BaptismHistoryFormService);
    baptismHistoryService = TestBed.inject(BaptismHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const baptismHistory: IBaptismHistory = { id: 456 };

      activatedRoute.data = of({ baptismHistory });
      comp.ngOnInit();

      expect(comp.baptismHistory).toEqual(baptismHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaptismHistory>>();
      const baptismHistory = { id: 123 };
      jest.spyOn(baptismHistoryFormService, 'getBaptismHistory').mockReturnValue(baptismHistory);
      jest.spyOn(baptismHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baptismHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: baptismHistory }));
      saveSubject.complete();

      // THEN
      expect(baptismHistoryFormService.getBaptismHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(baptismHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(baptismHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaptismHistory>>();
      const baptismHistory = { id: 123 };
      jest.spyOn(baptismHistoryFormService, 'getBaptismHistory').mockReturnValue({ id: null });
      jest.spyOn(baptismHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baptismHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: baptismHistory }));
      saveSubject.complete();

      // THEN
      expect(baptismHistoryFormService.getBaptismHistory).toHaveBeenCalled();
      expect(baptismHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaptismHistory>>();
      const baptismHistory = { id: 123 };
      jest.spyOn(baptismHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baptismHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(baptismHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

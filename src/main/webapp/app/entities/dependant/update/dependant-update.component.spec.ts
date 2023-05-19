import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DependantFormService } from './dependant-form.service';
import { DependantService } from '../service/dependant.service';
import { IDependant } from '../dependant.model';
import { ICongregant } from 'app/entities/congregant/congregant.model';
import { CongregantService } from 'app/entities/congregant/service/congregant.service';

import { DependantUpdateComponent } from './dependant-update.component';

describe('Dependant Management Update Component', () => {
  let comp: DependantUpdateComponent;
  let fixture: ComponentFixture<DependantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dependantFormService: DependantFormService;
  let dependantService: DependantService;
  let congregantService: CongregantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DependantUpdateComponent],
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
      .overrideTemplate(DependantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DependantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dependantFormService = TestBed.inject(DependantFormService);
    dependantService = TestBed.inject(DependantService);
    congregantService = TestBed.inject(CongregantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Congregant query and add missing value', () => {
      const dependant: IDependant = { id: 456 };
      const congregant: ICongregant = { id: 60946 };
      dependant.congregant = congregant;

      const congregantCollection: ICongregant[] = [{ id: 30516 }];
      jest.spyOn(congregantService, 'query').mockReturnValue(of(new HttpResponse({ body: congregantCollection })));
      const additionalCongregants = [congregant];
      const expectedCollection: ICongregant[] = [...additionalCongregants, ...congregantCollection];
      jest.spyOn(congregantService, 'addCongregantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dependant });
      comp.ngOnInit();

      expect(congregantService.query).toHaveBeenCalled();
      expect(congregantService.addCongregantToCollectionIfMissing).toHaveBeenCalledWith(
        congregantCollection,
        ...additionalCongregants.map(expect.objectContaining)
      );
      expect(comp.congregantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dependant: IDependant = { id: 456 };
      const congregant: ICongregant = { id: 90867 };
      dependant.congregant = congregant;

      activatedRoute.data = of({ dependant });
      comp.ngOnInit();

      expect(comp.congregantsSharedCollection).toContain(congregant);
      expect(comp.dependant).toEqual(dependant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDependant>>();
      const dependant = { id: 123 };
      jest.spyOn(dependantFormService, 'getDependant').mockReturnValue(dependant);
      jest.spyOn(dependantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dependant }));
      saveSubject.complete();

      // THEN
      expect(dependantFormService.getDependant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dependantService.update).toHaveBeenCalledWith(expect.objectContaining(dependant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDependant>>();
      const dependant = { id: 123 };
      jest.spyOn(dependantFormService, 'getDependant').mockReturnValue({ id: null });
      jest.spyOn(dependantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dependant }));
      saveSubject.complete();

      // THEN
      expect(dependantFormService.getDependant).toHaveBeenCalled();
      expect(dependantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDependant>>();
      const dependant = { id: 123 };
      jest.spyOn(dependantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dependantService.update).toHaveBeenCalled();
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

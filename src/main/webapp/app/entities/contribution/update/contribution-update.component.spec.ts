import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContributionFormService } from './contribution-form.service';
import { ContributionService } from '../service/contribution.service';
import { IContribution } from '../contribution.model';
import { ICongregant } from 'app/entities/congregant/congregant.model';
import { CongregantService } from 'app/entities/congregant/service/congregant.service';

import { ContributionUpdateComponent } from './contribution-update.component';

describe('Contribution Management Update Component', () => {
  let comp: ContributionUpdateComponent;
  let fixture: ComponentFixture<ContributionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contributionFormService: ContributionFormService;
  let contributionService: ContributionService;
  let congregantService: CongregantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContributionUpdateComponent],
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
      .overrideTemplate(ContributionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContributionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contributionFormService = TestBed.inject(ContributionFormService);
    contributionService = TestBed.inject(ContributionService);
    congregantService = TestBed.inject(CongregantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Congregant query and add missing value', () => {
      const contribution: IContribution = { id: 456 };
      const congregant: ICongregant = { id: 64509 };
      contribution.congregant = congregant;

      const congregantCollection: ICongregant[] = [{ id: 14870 }];
      jest.spyOn(congregantService, 'query').mockReturnValue(of(new HttpResponse({ body: congregantCollection })));
      const additionalCongregants = [congregant];
      const expectedCollection: ICongregant[] = [...additionalCongregants, ...congregantCollection];
      jest.spyOn(congregantService, 'addCongregantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contribution });
      comp.ngOnInit();

      expect(congregantService.query).toHaveBeenCalled();
      expect(congregantService.addCongregantToCollectionIfMissing).toHaveBeenCalledWith(
        congregantCollection,
        ...additionalCongregants.map(expect.objectContaining)
      );
      expect(comp.congregantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contribution: IContribution = { id: 456 };
      const congregant: ICongregant = { id: 2780 };
      contribution.congregant = congregant;

      activatedRoute.data = of({ contribution });
      comp.ngOnInit();

      expect(comp.congregantsSharedCollection).toContain(congregant);
      expect(comp.contribution).toEqual(contribution);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContribution>>();
      const contribution = { id: 123 };
      jest.spyOn(contributionFormService, 'getContribution').mockReturnValue(contribution);
      jest.spyOn(contributionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contribution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contribution }));
      saveSubject.complete();

      // THEN
      expect(contributionFormService.getContribution).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contributionService.update).toHaveBeenCalledWith(expect.objectContaining(contribution));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContribution>>();
      const contribution = { id: 123 };
      jest.spyOn(contributionFormService, 'getContribution').mockReturnValue({ id: null });
      jest.spyOn(contributionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contribution: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contribution }));
      saveSubject.complete();

      // THEN
      expect(contributionFormService.getContribution).toHaveBeenCalled();
      expect(contributionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContribution>>();
      const contribution = { id: 123 };
      jest.spyOn(contributionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contribution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contributionService.update).toHaveBeenCalled();
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

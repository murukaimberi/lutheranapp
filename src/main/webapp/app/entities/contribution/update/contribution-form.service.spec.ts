import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contribution.test-samples';

import { ContributionFormService } from './contribution-form.service';

describe('Contribution Form Service', () => {
  let service: ContributionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContributionFormService);
  });

  describe('Service methods', () => {
    describe('createContributionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContributionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contributionType: expect.any(Object),
            frequency: expect.any(Object),
            month: expect.any(Object),
            amount: expect.any(Object),
            congregant: expect.any(Object),
          })
        );
      });

      it('passing IContribution should create a new form with FormGroup', () => {
        const formGroup = service.createContributionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contributionType: expect.any(Object),
            frequency: expect.any(Object),
            month: expect.any(Object),
            amount: expect.any(Object),
            congregant: expect.any(Object),
          })
        );
      });
    });

    describe('getContribution', () => {
      it('should return NewContribution for default Contribution initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createContributionFormGroup(sampleWithNewData);

        const contribution = service.getContribution(formGroup) as any;

        expect(contribution).toMatchObject(sampleWithNewData);
      });

      it('should return NewContribution for empty Contribution initial value', () => {
        const formGroup = service.createContributionFormGroup();

        const contribution = service.getContribution(formGroup) as any;

        expect(contribution).toMatchObject({});
      });

      it('should return IContribution', () => {
        const formGroup = service.createContributionFormGroup(sampleWithRequiredData);

        const contribution = service.getContribution(formGroup) as any;

        expect(contribution).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContribution should not enable id FormControl', () => {
        const formGroup = service.createContributionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContribution should disable id FormControl', () => {
        const formGroup = service.createContributionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

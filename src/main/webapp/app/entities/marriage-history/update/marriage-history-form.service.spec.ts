import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../marriage-history.test-samples';

import { MarriageHistoryFormService } from './marriage-history-form.service';

describe('MarriageHistory Form Service', () => {
  let service: MarriageHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MarriageHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createMarriageHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMarriageHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            marriageDate: expect.any(Object),
            parishMarriedAt: expect.any(Object),
          })
        );
      });

      it('passing IMarriageHistory should create a new form with FormGroup', () => {
        const formGroup = service.createMarriageHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            marriageDate: expect.any(Object),
            parishMarriedAt: expect.any(Object),
          })
        );
      });
    });

    describe('getMarriageHistory', () => {
      it('should return NewMarriageHistory for default MarriageHistory initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMarriageHistoryFormGroup(sampleWithNewData);

        const marriageHistory = service.getMarriageHistory(formGroup) as any;

        expect(marriageHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewMarriageHistory for empty MarriageHistory initial value', () => {
        const formGroup = service.createMarriageHistoryFormGroup();

        const marriageHistory = service.getMarriageHistory(formGroup) as any;

        expect(marriageHistory).toMatchObject({});
      });

      it('should return IMarriageHistory', () => {
        const formGroup = service.createMarriageHistoryFormGroup(sampleWithRequiredData);

        const marriageHistory = service.getMarriageHistory(formGroup) as any;

        expect(marriageHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMarriageHistory should not enable id FormControl', () => {
        const formGroup = service.createMarriageHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMarriageHistory should disable id FormControl', () => {
        const formGroup = service.createMarriageHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

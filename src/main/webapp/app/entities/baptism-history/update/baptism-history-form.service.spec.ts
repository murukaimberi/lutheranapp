import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../baptism-history.test-samples';

import { BaptismHistoryFormService } from './baptism-history-form.service';

describe('BaptismHistory Form Service', () => {
  let service: BaptismHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BaptismHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createBaptismHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBaptismHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parishName: expect.any(Object),
            baptismDate: expect.any(Object),
            confirmedDate: expect.any(Object),
            parishBaptisedAt: expect.any(Object),
            prishedConfirmedAt: expect.any(Object),
          })
        );
      });

      it('passing IBaptismHistory should create a new form with FormGroup', () => {
        const formGroup = service.createBaptismHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            parishName: expect.any(Object),
            baptismDate: expect.any(Object),
            confirmedDate: expect.any(Object),
            parishBaptisedAt: expect.any(Object),
            prishedConfirmedAt: expect.any(Object),
          })
        );
      });
    });

    describe('getBaptismHistory', () => {
      it('should return NewBaptismHistory for default BaptismHistory initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBaptismHistoryFormGroup(sampleWithNewData);

        const baptismHistory = service.getBaptismHistory(formGroup) as any;

        expect(baptismHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewBaptismHistory for empty BaptismHistory initial value', () => {
        const formGroup = service.createBaptismHistoryFormGroup();

        const baptismHistory = service.getBaptismHistory(formGroup) as any;

        expect(baptismHistory).toMatchObject({});
      });

      it('should return IBaptismHistory', () => {
        const formGroup = service.createBaptismHistoryFormGroup(sampleWithRequiredData);

        const baptismHistory = service.getBaptismHistory(formGroup) as any;

        expect(baptismHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBaptismHistory should not enable id FormControl', () => {
        const formGroup = service.createBaptismHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBaptismHistory should disable id FormControl', () => {
        const formGroup = service.createBaptismHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

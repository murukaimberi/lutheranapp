import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../congregant.test-samples';

import { CongregantFormService } from './congregant-form.service';

describe('Congregant Form Service', () => {
  let service: CongregantFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CongregantFormService);
  });

  describe('Service methods', () => {
    describe('createCongregantFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCongregantFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            firstNames: expect.any(Object),
            surname: expect.any(Object),
            email: expect.any(Object),
            dob: expect.any(Object),
            gender: expect.any(Object),
            profession: expect.any(Object),
            maritalStatus: expect.any(Object),
            profilePicture: expect.any(Object),
            marriageHistory: expect.any(Object),
            baptismHistory: expect.any(Object),
            user: expect.any(Object),
            leagues: expect.any(Object),
          })
        );
      });

      it('passing ICongregant should create a new form with FormGroup', () => {
        const formGroup = service.createCongregantFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            firstNames: expect.any(Object),
            surname: expect.any(Object),
            email: expect.any(Object),
            dob: expect.any(Object),
            gender: expect.any(Object),
            profession: expect.any(Object),
            maritalStatus: expect.any(Object),
            profilePicture: expect.any(Object),
            marriageHistory: expect.any(Object),
            baptismHistory: expect.any(Object),
            user: expect.any(Object),
            leagues: expect.any(Object),
          })
        );
      });
    });

    describe('getCongregant', () => {
      it('should return NewCongregant for default Congregant initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCongregantFormGroup(sampleWithNewData);

        const congregant = service.getCongregant(formGroup) as any;

        expect(congregant).toMatchObject(sampleWithNewData);
      });

      it('should return NewCongregant for empty Congregant initial value', () => {
        const formGroup = service.createCongregantFormGroup();

        const congregant = service.getCongregant(formGroup) as any;

        expect(congregant).toMatchObject({});
      });

      it('should return ICongregant', () => {
        const formGroup = service.createCongregantFormGroup(sampleWithRequiredData);

        const congregant = service.getCongregant(formGroup) as any;

        expect(congregant).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICongregant should not enable id FormControl', () => {
        const formGroup = service.createCongregantFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCongregant should disable id FormControl', () => {
        const formGroup = service.createCongregantFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

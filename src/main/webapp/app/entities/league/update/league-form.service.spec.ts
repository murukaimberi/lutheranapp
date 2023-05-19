import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../league.test-samples';

import { LeagueFormService } from './league-form.service';

describe('League Form Service', () => {
  let service: LeagueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeagueFormService);
  });

  describe('Service methods', () => {
    describe('createLeagueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLeagueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            createdDate: expect.any(Object),
            congregants: expect.any(Object),
          })
        );
      });

      it('passing ILeague should create a new form with FormGroup', () => {
        const formGroup = service.createLeagueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            createdDate: expect.any(Object),
            congregants: expect.any(Object),
          })
        );
      });
    });

    describe('getLeague', () => {
      it('should return NewLeague for default League initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLeagueFormGroup(sampleWithNewData);

        const league = service.getLeague(formGroup) as any;

        expect(league).toMatchObject(sampleWithNewData);
      });

      it('should return NewLeague for empty League initial value', () => {
        const formGroup = service.createLeagueFormGroup();

        const league = service.getLeague(formGroup) as any;

        expect(league).toMatchObject({});
      });

      it('should return ILeague', () => {
        const formGroup = service.createLeagueFormGroup(sampleWithRequiredData);

        const league = service.getLeague(formGroup) as any;

        expect(league).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILeague should not enable id FormControl', () => {
        const formGroup = service.createLeagueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLeague should disable id FormControl', () => {
        const formGroup = service.createLeagueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

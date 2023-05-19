import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IContribution } from '../contribution.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contribution.test-samples';

import { ContributionService, RestContribution } from './contribution.service';

const requireRestSample: RestContribution = {
  ...sampleWithRequiredData,
  month: sampleWithRequiredData.month?.format(DATE_FORMAT),
};

describe('Contribution Service', () => {
  let service: ContributionService;
  let httpMock: HttpTestingController;
  let expectedResult: IContribution | IContribution[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContributionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Contribution', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const contribution = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contribution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contribution', () => {
      const contribution = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contribution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contribution', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contribution', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Contribution', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContributionToCollectionIfMissing', () => {
      it('should add a Contribution to an empty array', () => {
        const contribution: IContribution = sampleWithRequiredData;
        expectedResult = service.addContributionToCollectionIfMissing([], contribution);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contribution);
      });

      it('should not add a Contribution to an array that contains it', () => {
        const contribution: IContribution = sampleWithRequiredData;
        const contributionCollection: IContribution[] = [
          {
            ...contribution,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContributionToCollectionIfMissing(contributionCollection, contribution);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contribution to an array that doesn't contain it", () => {
        const contribution: IContribution = sampleWithRequiredData;
        const contributionCollection: IContribution[] = [sampleWithPartialData];
        expectedResult = service.addContributionToCollectionIfMissing(contributionCollection, contribution);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contribution);
      });

      it('should add only unique Contribution to an array', () => {
        const contributionArray: IContribution[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contributionCollection: IContribution[] = [sampleWithRequiredData];
        expectedResult = service.addContributionToCollectionIfMissing(contributionCollection, ...contributionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contribution: IContribution = sampleWithRequiredData;
        const contribution2: IContribution = sampleWithPartialData;
        expectedResult = service.addContributionToCollectionIfMissing([], contribution, contribution2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contribution);
        expect(expectedResult).toContain(contribution2);
      });

      it('should accept null and undefined values', () => {
        const contribution: IContribution = sampleWithRequiredData;
        expectedResult = service.addContributionToCollectionIfMissing([], null, contribution, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contribution);
      });

      it('should return initial array if no Contribution is added', () => {
        const contributionCollection: IContribution[] = [sampleWithRequiredData];
        expectedResult = service.addContributionToCollectionIfMissing(contributionCollection, undefined, null);
        expect(expectedResult).toEqual(contributionCollection);
      });
    });

    describe('compareContribution', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContribution(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContribution(entity1, entity2);
        const compareResult2 = service.compareContribution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContribution(entity1, entity2);
        const compareResult2 = service.compareContribution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContribution(entity1, entity2);
        const compareResult2 = service.compareContribution(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

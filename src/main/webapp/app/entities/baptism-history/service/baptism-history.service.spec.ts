import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBaptismHistory } from '../baptism-history.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../baptism-history.test-samples';

import { BaptismHistoryService, RestBaptismHistory } from './baptism-history.service';

const requireRestSample: RestBaptismHistory = {
  ...sampleWithRequiredData,
  baptismDate: sampleWithRequiredData.baptismDate?.format(DATE_FORMAT),
  confirmedDate: sampleWithRequiredData.confirmedDate?.format(DATE_FORMAT),
};

describe('BaptismHistory Service', () => {
  let service: BaptismHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IBaptismHistory | IBaptismHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BaptismHistoryService);
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

    it('should create a BaptismHistory', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const baptismHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(baptismHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BaptismHistory', () => {
      const baptismHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(baptismHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BaptismHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BaptismHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BaptismHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBaptismHistoryToCollectionIfMissing', () => {
      it('should add a BaptismHistory to an empty array', () => {
        const baptismHistory: IBaptismHistory = sampleWithRequiredData;
        expectedResult = service.addBaptismHistoryToCollectionIfMissing([], baptismHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(baptismHistory);
      });

      it('should not add a BaptismHistory to an array that contains it', () => {
        const baptismHistory: IBaptismHistory = sampleWithRequiredData;
        const baptismHistoryCollection: IBaptismHistory[] = [
          {
            ...baptismHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBaptismHistoryToCollectionIfMissing(baptismHistoryCollection, baptismHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BaptismHistory to an array that doesn't contain it", () => {
        const baptismHistory: IBaptismHistory = sampleWithRequiredData;
        const baptismHistoryCollection: IBaptismHistory[] = [sampleWithPartialData];
        expectedResult = service.addBaptismHistoryToCollectionIfMissing(baptismHistoryCollection, baptismHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(baptismHistory);
      });

      it('should add only unique BaptismHistory to an array', () => {
        const baptismHistoryArray: IBaptismHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const baptismHistoryCollection: IBaptismHistory[] = [sampleWithRequiredData];
        expectedResult = service.addBaptismHistoryToCollectionIfMissing(baptismHistoryCollection, ...baptismHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const baptismHistory: IBaptismHistory = sampleWithRequiredData;
        const baptismHistory2: IBaptismHistory = sampleWithPartialData;
        expectedResult = service.addBaptismHistoryToCollectionIfMissing([], baptismHistory, baptismHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(baptismHistory);
        expect(expectedResult).toContain(baptismHistory2);
      });

      it('should accept null and undefined values', () => {
        const baptismHistory: IBaptismHistory = sampleWithRequiredData;
        expectedResult = service.addBaptismHistoryToCollectionIfMissing([], null, baptismHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(baptismHistory);
      });

      it('should return initial array if no BaptismHistory is added', () => {
        const baptismHistoryCollection: IBaptismHistory[] = [sampleWithRequiredData];
        expectedResult = service.addBaptismHistoryToCollectionIfMissing(baptismHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(baptismHistoryCollection);
      });
    });

    describe('compareBaptismHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBaptismHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBaptismHistory(entity1, entity2);
        const compareResult2 = service.compareBaptismHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBaptismHistory(entity1, entity2);
        const compareResult2 = service.compareBaptismHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBaptismHistory(entity1, entity2);
        const compareResult2 = service.compareBaptismHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

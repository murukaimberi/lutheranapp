import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICongregant } from '../congregant.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../congregant.test-samples';

import { CongregantService, RestCongregant } from './congregant.service';

const requireRestSample: RestCongregant = {
  ...sampleWithRequiredData,
  dob: sampleWithRequiredData.dob?.format(DATE_FORMAT),
};

describe('Congregant Service', () => {
  let service: CongregantService;
  let httpMock: HttpTestingController;
  let expectedResult: ICongregant | ICongregant[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CongregantService);
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

    it('should create a Congregant', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const congregant = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(congregant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Congregant', () => {
      const congregant = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(congregant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Congregant', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Congregant', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Congregant', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCongregantToCollectionIfMissing', () => {
      it('should add a Congregant to an empty array', () => {
        const congregant: ICongregant = sampleWithRequiredData;
        expectedResult = service.addCongregantToCollectionIfMissing([], congregant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(congregant);
      });

      it('should not add a Congregant to an array that contains it', () => {
        const congregant: ICongregant = sampleWithRequiredData;
        const congregantCollection: ICongregant[] = [
          {
            ...congregant,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCongregantToCollectionIfMissing(congregantCollection, congregant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Congregant to an array that doesn't contain it", () => {
        const congregant: ICongregant = sampleWithRequiredData;
        const congregantCollection: ICongregant[] = [sampleWithPartialData];
        expectedResult = service.addCongregantToCollectionIfMissing(congregantCollection, congregant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(congregant);
      });

      it('should add only unique Congregant to an array', () => {
        const congregantArray: ICongregant[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const congregantCollection: ICongregant[] = [sampleWithRequiredData];
        expectedResult = service.addCongregantToCollectionIfMissing(congregantCollection, ...congregantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const congregant: ICongregant = sampleWithRequiredData;
        const congregant2: ICongregant = sampleWithPartialData;
        expectedResult = service.addCongregantToCollectionIfMissing([], congregant, congregant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(congregant);
        expect(expectedResult).toContain(congregant2);
      });

      it('should accept null and undefined values', () => {
        const congregant: ICongregant = sampleWithRequiredData;
        expectedResult = service.addCongregantToCollectionIfMissing([], null, congregant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(congregant);
      });

      it('should return initial array if no Congregant is added', () => {
        const congregantCollection: ICongregant[] = [sampleWithRequiredData];
        expectedResult = service.addCongregantToCollectionIfMissing(congregantCollection, undefined, null);
        expect(expectedResult).toEqual(congregantCollection);
      });
    });

    describe('compareCongregant', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCongregant(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCongregant(entity1, entity2);
        const compareResult2 = service.compareCongregant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCongregant(entity1, entity2);
        const compareResult2 = service.compareCongregant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCongregant(entity1, entity2);
        const compareResult2 = service.compareCongregant(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

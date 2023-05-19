import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDependant } from '../dependant.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../dependant.test-samples';

import { DependantService, RestDependant } from './dependant.service';

const requireRestSample: RestDependant = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
};

describe('Dependant Service', () => {
  let service: DependantService;
  let httpMock: HttpTestingController;
  let expectedResult: IDependant | IDependant[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DependantService);
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

    it('should create a Dependant', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const dependant = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dependant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dependant', () => {
      const dependant = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dependant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dependant', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dependant', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Dependant', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDependantToCollectionIfMissing', () => {
      it('should add a Dependant to an empty array', () => {
        const dependant: IDependant = sampleWithRequiredData;
        expectedResult = service.addDependantToCollectionIfMissing([], dependant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dependant);
      });

      it('should not add a Dependant to an array that contains it', () => {
        const dependant: IDependant = sampleWithRequiredData;
        const dependantCollection: IDependant[] = [
          {
            ...dependant,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDependantToCollectionIfMissing(dependantCollection, dependant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dependant to an array that doesn't contain it", () => {
        const dependant: IDependant = sampleWithRequiredData;
        const dependantCollection: IDependant[] = [sampleWithPartialData];
        expectedResult = service.addDependantToCollectionIfMissing(dependantCollection, dependant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependant);
      });

      it('should add only unique Dependant to an array', () => {
        const dependantArray: IDependant[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dependantCollection: IDependant[] = [sampleWithRequiredData];
        expectedResult = service.addDependantToCollectionIfMissing(dependantCollection, ...dependantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dependant: IDependant = sampleWithRequiredData;
        const dependant2: IDependant = sampleWithPartialData;
        expectedResult = service.addDependantToCollectionIfMissing([], dependant, dependant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependant);
        expect(expectedResult).toContain(dependant2);
      });

      it('should accept null and undefined values', () => {
        const dependant: IDependant = sampleWithRequiredData;
        expectedResult = service.addDependantToCollectionIfMissing([], null, dependant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dependant);
      });

      it('should return initial array if no Dependant is added', () => {
        const dependantCollection: IDependant[] = [sampleWithRequiredData];
        expectedResult = service.addDependantToCollectionIfMissing(dependantCollection, undefined, null);
        expect(expectedResult).toEqual(dependantCollection);
      });
    });

    describe('compareDependant', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDependant(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDependant(entity1, entity2);
        const compareResult2 = service.compareDependant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDependant(entity1, entity2);
        const compareResult2 = service.compareDependant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDependant(entity1, entity2);
        const compareResult2 = service.compareDependant(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

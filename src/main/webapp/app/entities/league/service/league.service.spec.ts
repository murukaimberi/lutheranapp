import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILeague } from '../league.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../league.test-samples';

import { LeagueService, RestLeague } from './league.service';

const requireRestSample: RestLeague = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.format(DATE_FORMAT),
};

describe('League Service', () => {
  let service: LeagueService;
  let httpMock: HttpTestingController;
  let expectedResult: ILeague | ILeague[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeagueService);
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

    it('should create a League', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const league = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(league).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a League', () => {
      const league = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(league).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a League', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of League', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a League', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLeagueToCollectionIfMissing', () => {
      it('should add a League to an empty array', () => {
        const league: ILeague = sampleWithRequiredData;
        expectedResult = service.addLeagueToCollectionIfMissing([], league);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(league);
      });

      it('should not add a League to an array that contains it', () => {
        const league: ILeague = sampleWithRequiredData;
        const leagueCollection: ILeague[] = [
          {
            ...league,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLeagueToCollectionIfMissing(leagueCollection, league);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a League to an array that doesn't contain it", () => {
        const league: ILeague = sampleWithRequiredData;
        const leagueCollection: ILeague[] = [sampleWithPartialData];
        expectedResult = service.addLeagueToCollectionIfMissing(leagueCollection, league);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(league);
      });

      it('should add only unique League to an array', () => {
        const leagueArray: ILeague[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const leagueCollection: ILeague[] = [sampleWithRequiredData];
        expectedResult = service.addLeagueToCollectionIfMissing(leagueCollection, ...leagueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const league: ILeague = sampleWithRequiredData;
        const league2: ILeague = sampleWithPartialData;
        expectedResult = service.addLeagueToCollectionIfMissing([], league, league2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(league);
        expect(expectedResult).toContain(league2);
      });

      it('should accept null and undefined values', () => {
        const league: ILeague = sampleWithRequiredData;
        expectedResult = service.addLeagueToCollectionIfMissing([], null, league, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(league);
      });

      it('should return initial array if no League is added', () => {
        const leagueCollection: ILeague[] = [sampleWithRequiredData];
        expectedResult = service.addLeagueToCollectionIfMissing(leagueCollection, undefined, null);
        expect(expectedResult).toEqual(leagueCollection);
      });
    });

    describe('compareLeague', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLeague(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLeague(entity1, entity2);
        const compareResult2 = service.compareLeague(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLeague(entity1, entity2);
        const compareResult2 = service.compareLeague(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLeague(entity1, entity2);
        const compareResult2 = service.compareLeague(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMarriageHistory, NewMarriageHistory } from '../marriage-history.model';

export type PartialUpdateMarriageHistory = Partial<IMarriageHistory> & Pick<IMarriageHistory, 'id'>;

type RestOf<T extends IMarriageHistory | NewMarriageHistory> = Omit<T, 'marriageDate'> & {
  marriageDate?: string | null;
};

export type RestMarriageHistory = RestOf<IMarriageHistory>;

export type NewRestMarriageHistory = RestOf<NewMarriageHistory>;

export type PartialUpdateRestMarriageHistory = RestOf<PartialUpdateMarriageHistory>;

export type EntityResponseType = HttpResponse<IMarriageHistory>;
export type EntityArrayResponseType = HttpResponse<IMarriageHistory[]>;

@Injectable({ providedIn: 'root' })
export class MarriageHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/marriage-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(marriageHistory: NewMarriageHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(marriageHistory);
    return this.http
      .post<RestMarriageHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(marriageHistory: IMarriageHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(marriageHistory);
    return this.http
      .put<RestMarriageHistory>(`${this.resourceUrl}/${this.getMarriageHistoryIdentifier(marriageHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(marriageHistory: PartialUpdateMarriageHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(marriageHistory);
    return this.http
      .patch<RestMarriageHistory>(`${this.resourceUrl}/${this.getMarriageHistoryIdentifier(marriageHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMarriageHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMarriageHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMarriageHistoryIdentifier(marriageHistory: Pick<IMarriageHistory, 'id'>): number {
    return marriageHistory.id;
  }

  compareMarriageHistory(o1: Pick<IMarriageHistory, 'id'> | null, o2: Pick<IMarriageHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getMarriageHistoryIdentifier(o1) === this.getMarriageHistoryIdentifier(o2) : o1 === o2;
  }

  addMarriageHistoryToCollectionIfMissing<Type extends Pick<IMarriageHistory, 'id'>>(
    marriageHistoryCollection: Type[],
    ...marriageHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const marriageHistories: Type[] = marriageHistoriesToCheck.filter(isPresent);
    if (marriageHistories.length > 0) {
      const marriageHistoryCollectionIdentifiers = marriageHistoryCollection.map(
        marriageHistoryItem => this.getMarriageHistoryIdentifier(marriageHistoryItem)!
      );
      const marriageHistoriesToAdd = marriageHistories.filter(marriageHistoryItem => {
        const marriageHistoryIdentifier = this.getMarriageHistoryIdentifier(marriageHistoryItem);
        if (marriageHistoryCollectionIdentifiers.includes(marriageHistoryIdentifier)) {
          return false;
        }
        marriageHistoryCollectionIdentifiers.push(marriageHistoryIdentifier);
        return true;
      });
      return [...marriageHistoriesToAdd, ...marriageHistoryCollection];
    }
    return marriageHistoryCollection;
  }

  protected convertDateFromClient<T extends IMarriageHistory | NewMarriageHistory | PartialUpdateMarriageHistory>(
    marriageHistory: T
  ): RestOf<T> {
    return {
      ...marriageHistory,
      marriageDate: marriageHistory.marriageDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMarriageHistory: RestMarriageHistory): IMarriageHistory {
    return {
      ...restMarriageHistory,
      marriageDate: restMarriageHistory.marriageDate ? dayjs(restMarriageHistory.marriageDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMarriageHistory>): HttpResponse<IMarriageHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMarriageHistory[]>): HttpResponse<IMarriageHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

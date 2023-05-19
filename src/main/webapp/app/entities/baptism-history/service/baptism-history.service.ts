import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBaptismHistory, NewBaptismHistory } from '../baptism-history.model';

export type PartialUpdateBaptismHistory = Partial<IBaptismHistory> & Pick<IBaptismHistory, 'id'>;

type RestOf<T extends IBaptismHistory | NewBaptismHistory> = Omit<T, 'baptismDate' | 'confirmedDate'> & {
  baptismDate?: string | null;
  confirmedDate?: string | null;
};

export type RestBaptismHistory = RestOf<IBaptismHistory>;

export type NewRestBaptismHistory = RestOf<NewBaptismHistory>;

export type PartialUpdateRestBaptismHistory = RestOf<PartialUpdateBaptismHistory>;

export type EntityResponseType = HttpResponse<IBaptismHistory>;
export type EntityArrayResponseType = HttpResponse<IBaptismHistory[]>;

@Injectable({ providedIn: 'root' })
export class BaptismHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/baptism-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(baptismHistory: NewBaptismHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(baptismHistory);
    return this.http
      .post<RestBaptismHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(baptismHistory: IBaptismHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(baptismHistory);
    return this.http
      .put<RestBaptismHistory>(`${this.resourceUrl}/${this.getBaptismHistoryIdentifier(baptismHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(baptismHistory: PartialUpdateBaptismHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(baptismHistory);
    return this.http
      .patch<RestBaptismHistory>(`${this.resourceUrl}/${this.getBaptismHistoryIdentifier(baptismHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBaptismHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBaptismHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBaptismHistoryIdentifier(baptismHistory: Pick<IBaptismHistory, 'id'>): number {
    return baptismHistory.id;
  }

  compareBaptismHistory(o1: Pick<IBaptismHistory, 'id'> | null, o2: Pick<IBaptismHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getBaptismHistoryIdentifier(o1) === this.getBaptismHistoryIdentifier(o2) : o1 === o2;
  }

  addBaptismHistoryToCollectionIfMissing<Type extends Pick<IBaptismHistory, 'id'>>(
    baptismHistoryCollection: Type[],
    ...baptismHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const baptismHistories: Type[] = baptismHistoriesToCheck.filter(isPresent);
    if (baptismHistories.length > 0) {
      const baptismHistoryCollectionIdentifiers = baptismHistoryCollection.map(
        baptismHistoryItem => this.getBaptismHistoryIdentifier(baptismHistoryItem)!
      );
      const baptismHistoriesToAdd = baptismHistories.filter(baptismHistoryItem => {
        const baptismHistoryIdentifier = this.getBaptismHistoryIdentifier(baptismHistoryItem);
        if (baptismHistoryCollectionIdentifiers.includes(baptismHistoryIdentifier)) {
          return false;
        }
        baptismHistoryCollectionIdentifiers.push(baptismHistoryIdentifier);
        return true;
      });
      return [...baptismHistoriesToAdd, ...baptismHistoryCollection];
    }
    return baptismHistoryCollection;
  }

  protected convertDateFromClient<T extends IBaptismHistory | NewBaptismHistory | PartialUpdateBaptismHistory>(
    baptismHistory: T
  ): RestOf<T> {
    return {
      ...baptismHistory,
      baptismDate: baptismHistory.baptismDate?.format(DATE_FORMAT) ?? null,
      confirmedDate: baptismHistory.confirmedDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBaptismHistory: RestBaptismHistory): IBaptismHistory {
    return {
      ...restBaptismHistory,
      baptismDate: restBaptismHistory.baptismDate ? dayjs(restBaptismHistory.baptismDate) : undefined,
      confirmedDate: restBaptismHistory.confirmedDate ? dayjs(restBaptismHistory.confirmedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBaptismHistory>): HttpResponse<IBaptismHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBaptismHistory[]>): HttpResponse<IBaptismHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

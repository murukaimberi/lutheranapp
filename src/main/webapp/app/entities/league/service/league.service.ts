import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeague, NewLeague } from '../league.model';

export type PartialUpdateLeague = Partial<ILeague> & Pick<ILeague, 'id'>;

type RestOf<T extends ILeague | NewLeague> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestLeague = RestOf<ILeague>;

export type NewRestLeague = RestOf<NewLeague>;

export type PartialUpdateRestLeague = RestOf<PartialUpdateLeague>;

export type EntityResponseType = HttpResponse<ILeague>;
export type EntityArrayResponseType = HttpResponse<ILeague[]>;

@Injectable({ providedIn: 'root' })
export class LeagueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leagues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(league: NewLeague): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(league);
    return this.http
      .post<RestLeague>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(league: ILeague): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(league);
    return this.http
      .put<RestLeague>(`${this.resourceUrl}/${this.getLeagueIdentifier(league)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(league: PartialUpdateLeague): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(league);
    return this.http
      .patch<RestLeague>(`${this.resourceUrl}/${this.getLeagueIdentifier(league)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLeague>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLeague[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLeagueIdentifier(league: Pick<ILeague, 'id'>): number {
    return league.id;
  }

  compareLeague(o1: Pick<ILeague, 'id'> | null, o2: Pick<ILeague, 'id'> | null): boolean {
    return o1 && o2 ? this.getLeagueIdentifier(o1) === this.getLeagueIdentifier(o2) : o1 === o2;
  }

  addLeagueToCollectionIfMissing<Type extends Pick<ILeague, 'id'>>(
    leagueCollection: Type[],
    ...leaguesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const leagues: Type[] = leaguesToCheck.filter(isPresent);
    if (leagues.length > 0) {
      const leagueCollectionIdentifiers = leagueCollection.map(leagueItem => this.getLeagueIdentifier(leagueItem)!);
      const leaguesToAdd = leagues.filter(leagueItem => {
        const leagueIdentifier = this.getLeagueIdentifier(leagueItem);
        if (leagueCollectionIdentifiers.includes(leagueIdentifier)) {
          return false;
        }
        leagueCollectionIdentifiers.push(leagueIdentifier);
        return true;
      });
      return [...leaguesToAdd, ...leagueCollection];
    }
    return leagueCollection;
  }

  protected convertDateFromClient<T extends ILeague | NewLeague | PartialUpdateLeague>(league: T): RestOf<T> {
    return {
      ...league,
      createdDate: dayjs(league.createdDate).format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restLeague: RestLeague): ILeague {
    return {
      ...restLeague,
      createdDate: restLeague.createdDate ? dayjs(restLeague.createdDate).toDate() : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLeague>): HttpResponse<ILeague> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLeague[]>): HttpResponse<ILeague[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

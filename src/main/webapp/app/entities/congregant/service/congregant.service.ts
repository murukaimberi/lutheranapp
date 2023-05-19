import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICongregant, NewCongregant } from '../congregant.model';

export type PartialUpdateCongregant = Partial<ICongregant> & Pick<ICongregant, 'id'>;

type RestOf<T extends ICongregant | NewCongregant> = Omit<T, 'dob'> & {
  dob?: string | null;
};

export type RestCongregant = RestOf<ICongregant>;

export type NewRestCongregant = RestOf<NewCongregant>;

export type PartialUpdateRestCongregant = RestOf<PartialUpdateCongregant>;

export type EntityResponseType = HttpResponse<ICongregant>;
export type EntityArrayResponseType = HttpResponse<ICongregant[]>;

@Injectable({ providedIn: 'root' })
export class CongregantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/congregants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(congregant: NewCongregant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(congregant);
    return this.http
      .post<RestCongregant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(congregant: ICongregant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(congregant);
    return this.http
      .put<RestCongregant>(`${this.resourceUrl}/${this.getCongregantIdentifier(congregant)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(congregant: PartialUpdateCongregant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(congregant);
    return this.http
      .patch<RestCongregant>(`${this.resourceUrl}/${this.getCongregantIdentifier(congregant)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCongregant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCongregant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCongregantIdentifier(congregant: Pick<ICongregant, 'id'>): number {
    return congregant.id;
  }

  compareCongregant(o1: Pick<ICongregant, 'id'> | null, o2: Pick<ICongregant, 'id'> | null): boolean {
    return o1 && o2 ? this.getCongregantIdentifier(o1) === this.getCongregantIdentifier(o2) : o1 === o2;
  }

  addCongregantToCollectionIfMissing<Type extends Pick<ICongregant, 'id'>>(
    congregantCollection: Type[],
    ...congregantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const congregants: Type[] = congregantsToCheck.filter(isPresent);
    if (congregants.length > 0) {
      const congregantCollectionIdentifiers = congregantCollection.map(congregantItem => this.getCongregantIdentifier(congregantItem)!);
      const congregantsToAdd = congregants.filter(congregantItem => {
        const congregantIdentifier = this.getCongregantIdentifier(congregantItem);
        if (congregantCollectionIdentifiers.includes(congregantIdentifier)) {
          return false;
        }
        congregantCollectionIdentifiers.push(congregantIdentifier);
        return true;
      });
      return [...congregantsToAdd, ...congregantCollection];
    }
    return congregantCollection;
  }

  protected convertDateFromClient<T extends ICongregant | NewCongregant | PartialUpdateCongregant>(congregant: T): RestOf<T> {
    return {
      ...congregant,
      dob: congregant.dob?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCongregant: RestCongregant): ICongregant {
    return {
      ...restCongregant,
      dob: restCongregant.dob ? dayjs(restCongregant.dob) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCongregant>): HttpResponse<ICongregant> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCongregant[]>): HttpResponse<ICongregant[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

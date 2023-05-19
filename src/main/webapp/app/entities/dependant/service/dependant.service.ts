import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDependant, NewDependant } from '../dependant.model';

export type PartialUpdateDependant = Partial<IDependant> & Pick<IDependant, 'id'>;

type RestOf<T extends IDependant | NewDependant> = Omit<T, 'dateOfBirth'> & {
  dateOfBirth?: string | null;
};

export type RestDependant = RestOf<IDependant>;

export type NewRestDependant = RestOf<NewDependant>;

export type PartialUpdateRestDependant = RestOf<PartialUpdateDependant>;

export type EntityResponseType = HttpResponse<IDependant>;
export type EntityArrayResponseType = HttpResponse<IDependant[]>;

@Injectable({ providedIn: 'root' })
export class DependantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dependants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dependant: NewDependant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dependant);
    return this.http
      .post<RestDependant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(dependant: IDependant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dependant);
    return this.http
      .put<RestDependant>(`${this.resourceUrl}/${this.getDependantIdentifier(dependant)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(dependant: PartialUpdateDependant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dependant);
    return this.http
      .patch<RestDependant>(`${this.resourceUrl}/${this.getDependantIdentifier(dependant)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDependant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDependant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDependantIdentifier(dependant: Pick<IDependant, 'id'>): number {
    return dependant.id;
  }

  compareDependant(o1: Pick<IDependant, 'id'> | null, o2: Pick<IDependant, 'id'> | null): boolean {
    return o1 && o2 ? this.getDependantIdentifier(o1) === this.getDependantIdentifier(o2) : o1 === o2;
  }

  addDependantToCollectionIfMissing<Type extends Pick<IDependant, 'id'>>(
    dependantCollection: Type[],
    ...dependantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dependants: Type[] = dependantsToCheck.filter(isPresent);
    if (dependants.length > 0) {
      const dependantCollectionIdentifiers = dependantCollection.map(dependantItem => this.getDependantIdentifier(dependantItem)!);
      const dependantsToAdd = dependants.filter(dependantItem => {
        const dependantIdentifier = this.getDependantIdentifier(dependantItem);
        if (dependantCollectionIdentifiers.includes(dependantIdentifier)) {
          return false;
        }
        dependantCollectionIdentifiers.push(dependantIdentifier);
        return true;
      });
      return [...dependantsToAdd, ...dependantCollection];
    }
    return dependantCollection;
  }

  protected convertDateFromClient<T extends IDependant | NewDependant | PartialUpdateDependant>(dependant: T): RestOf<T> {
    return {
      ...dependant,
      dateOfBirth: dependant.dateOfBirth?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDependant: RestDependant): IDependant {
    return {
      ...restDependant,
      dateOfBirth: restDependant.dateOfBirth ? dayjs(restDependant.dateOfBirth) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDependant>): HttpResponse<IDependant> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDependant[]>): HttpResponse<IDependant[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

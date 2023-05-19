import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContribution, NewContribution } from '../contribution.model';

export type PartialUpdateContribution = Partial<IContribution> & Pick<IContribution, 'id'>;

type RestOf<T extends IContribution | NewContribution> = Omit<T, 'month'> & {
  month?: string | null;
};

export type RestContribution = RestOf<IContribution>;

export type NewRestContribution = RestOf<NewContribution>;

export type PartialUpdateRestContribution = RestOf<PartialUpdateContribution>;

export type EntityResponseType = HttpResponse<IContribution>;
export type EntityArrayResponseType = HttpResponse<IContribution[]>;

@Injectable({ providedIn: 'root' })
export class ContributionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contributions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contribution: NewContribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contribution);
    return this.http
      .post<RestContribution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contribution: IContribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contribution);
    return this.http
      .put<RestContribution>(`${this.resourceUrl}/${this.getContributionIdentifier(contribution)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contribution: PartialUpdateContribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contribution);
    return this.http
      .patch<RestContribution>(`${this.resourceUrl}/${this.getContributionIdentifier(contribution)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContribution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContribution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContributionIdentifier(contribution: Pick<IContribution, 'id'>): number {
    return contribution.id;
  }

  compareContribution(o1: Pick<IContribution, 'id'> | null, o2: Pick<IContribution, 'id'> | null): boolean {
    return o1 && o2 ? this.getContributionIdentifier(o1) === this.getContributionIdentifier(o2) : o1 === o2;
  }

  addContributionToCollectionIfMissing<Type extends Pick<IContribution, 'id'>>(
    contributionCollection: Type[],
    ...contributionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contributions: Type[] = contributionsToCheck.filter(isPresent);
    if (contributions.length > 0) {
      const contributionCollectionIdentifiers = contributionCollection.map(
        contributionItem => this.getContributionIdentifier(contributionItem)!
      );
      const contributionsToAdd = contributions.filter(contributionItem => {
        const contributionIdentifier = this.getContributionIdentifier(contributionItem);
        if (contributionCollectionIdentifiers.includes(contributionIdentifier)) {
          return false;
        }
        contributionCollectionIdentifiers.push(contributionIdentifier);
        return true;
      });
      return [...contributionsToAdd, ...contributionCollection];
    }
    return contributionCollection;
  }

  protected convertDateFromClient<T extends IContribution | NewContribution | PartialUpdateContribution>(contribution: T): RestOf<T> {
    return {
      ...contribution,
      month: contribution.month?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restContribution: RestContribution): IContribution {
    return {
      ...restContribution,
      month: restContribution.month ? dayjs(restContribution.month) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContribution>): HttpResponse<IContribution> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContribution[]>): HttpResponse<IContribution[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMarriageHistory } from '../marriage-history.model';
import { MarriageHistoryService } from '../service/marriage-history.service';

@Injectable({ providedIn: 'root' })
export class MarriageHistoryRoutingResolveService implements Resolve<IMarriageHistory | null> {
  constructor(protected service: MarriageHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMarriageHistory | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((marriageHistory: HttpResponse<IMarriageHistory>) => {
          if (marriageHistory.body) {
            return of(marriageHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}

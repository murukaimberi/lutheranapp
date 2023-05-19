import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBaptismHistory } from '../baptism-history.model';
import { BaptismHistoryService } from '../service/baptism-history.service';

@Injectable({ providedIn: 'root' })
export class BaptismHistoryRoutingResolveService implements Resolve<IBaptismHistory | null> {
  constructor(protected service: BaptismHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBaptismHistory | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((baptismHistory: HttpResponse<IBaptismHistory>) => {
          if (baptismHistory.body) {
            return of(baptismHistory.body);
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

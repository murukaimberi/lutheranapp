import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICongregant } from '../congregant.model';
import { CongregantService } from '../service/congregant.service';

@Injectable({ providedIn: 'root' })
export class CongregantRoutingResolveService implements Resolve<ICongregant | null> {
  constructor(protected service: CongregantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICongregant | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((congregant: HttpResponse<ICongregant>) => {
          if (congregant.body) {
            return of(congregant.body);
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

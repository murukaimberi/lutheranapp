import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDependant } from '../dependant.model';
import { DependantService } from '../service/dependant.service';

@Injectable({ providedIn: 'root' })
export class DependantRoutingResolveService implements Resolve<IDependant | null> {
  constructor(protected service: DependantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDependant | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dependant: HttpResponse<IDependant>) => {
          if (dependant.body) {
            return of(dependant.body);
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

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DependantComponent } from '../list/dependant.component';
import { DependantDetailComponent } from '../detail/dependant-detail.component';
import { DependantUpdateComponent } from '../update/dependant-update.component';
import { DependantRoutingResolveService } from './dependant-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dependantRoute: Routes = [
  {
    path: '',
    component: DependantComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DependantDetailComponent,
    resolve: {
      dependant: DependantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DependantUpdateComponent,
    resolve: {
      dependant: DependantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DependantUpdateComponent,
    resolve: {
      dependant: DependantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dependantRoute)],
  exports: [RouterModule],
})
export class DependantRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CongregantComponent } from '../list/congregant.component';
import { CongregantDetailComponent } from '../detail/congregant-detail.component';
import { CongregantUpdateComponent } from '../update/congregant-update.component';
import { CongregantRoutingResolveService } from './congregant-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const congregantRoute: Routes = [
  {
    path: '',
    component: CongregantComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CongregantDetailComponent,
    resolve: {
      congregant: CongregantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CongregantUpdateComponent,
    resolve: {
      congregant: CongregantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CongregantUpdateComponent,
    resolve: {
      congregant: CongregantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(congregantRoute)],
  exports: [RouterModule],
})
export class CongregantRoutingModule {}

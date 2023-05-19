import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BaptismHistoryComponent } from '../list/baptism-history.component';
import { BaptismHistoryDetailComponent } from '../detail/baptism-history-detail.component';
import { BaptismHistoryUpdateComponent } from '../update/baptism-history-update.component';
import { BaptismHistoryRoutingResolveService } from './baptism-history-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const baptismHistoryRoute: Routes = [
  {
    path: '',
    component: BaptismHistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BaptismHistoryDetailComponent,
    resolve: {
      baptismHistory: BaptismHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BaptismHistoryUpdateComponent,
    resolve: {
      baptismHistory: BaptismHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BaptismHistoryUpdateComponent,
    resolve: {
      baptismHistory: BaptismHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(baptismHistoryRoute)],
  exports: [RouterModule],
})
export class BaptismHistoryRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MarriageHistoryComponent } from '../list/marriage-history.component';
import { MarriageHistoryDetailComponent } from '../detail/marriage-history-detail.component';
import { MarriageHistoryUpdateComponent } from '../update/marriage-history-update.component';
import { MarriageHistoryRoutingResolveService } from './marriage-history-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const marriageHistoryRoute: Routes = [
  {
    path: '',
    component: MarriageHistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MarriageHistoryDetailComponent,
    resolve: {
      marriageHistory: MarriageHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MarriageHistoryUpdateComponent,
    resolve: {
      marriageHistory: MarriageHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MarriageHistoryUpdateComponent,
    resolve: {
      marriageHistory: MarriageHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(marriageHistoryRoute)],
  exports: [RouterModule],
})
export class MarriageHistoryRoutingModule {}

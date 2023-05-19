import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContributionComponent } from '../list/contribution.component';
import { ContributionDetailComponent } from '../detail/contribution-detail.component';
import { ContributionUpdateComponent } from '../update/contribution-update.component';
import { ContributionRoutingResolveService } from './contribution-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const contributionRoute: Routes = [
  {
    path: '',
    component: ContributionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContributionDetailComponent,
    resolve: {
      contribution: ContributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContributionUpdateComponent,
    resolve: {
      contribution: ContributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContributionUpdateComponent,
    resolve: {
      contribution: ContributionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contributionRoute)],
  exports: [RouterModule],
})
export class ContributionRoutingModule {}

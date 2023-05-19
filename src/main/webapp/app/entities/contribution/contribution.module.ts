import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContributionComponent } from './list/contribution.component';
import { ContributionDetailComponent } from './detail/contribution-detail.component';
import { ContributionUpdateComponent } from './update/contribution-update.component';
import { ContributionDeleteDialogComponent } from './delete/contribution-delete-dialog.component';
import { ContributionRoutingModule } from './route/contribution-routing.module';

@NgModule({
  imports: [SharedModule, ContributionRoutingModule],
  declarations: [ContributionComponent, ContributionDetailComponent, ContributionUpdateComponent, ContributionDeleteDialogComponent],
})
export class ContributionModule {}

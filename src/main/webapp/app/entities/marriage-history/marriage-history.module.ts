import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MarriageHistoryComponent } from './list/marriage-history.component';
import { MarriageHistoryDetailComponent } from './detail/marriage-history-detail.component';
import { MarriageHistoryUpdateComponent } from './update/marriage-history-update.component';
import { MarriageHistoryDeleteDialogComponent } from './delete/marriage-history-delete-dialog.component';
import { MarriageHistoryRoutingModule } from './route/marriage-history-routing.module';

@NgModule({
  imports: [SharedModule, MarriageHistoryRoutingModule],
  declarations: [
    MarriageHistoryComponent,
    MarriageHistoryDetailComponent,
    MarriageHistoryUpdateComponent,
    MarriageHistoryDeleteDialogComponent,
  ],
})
export class MarriageHistoryModule {}

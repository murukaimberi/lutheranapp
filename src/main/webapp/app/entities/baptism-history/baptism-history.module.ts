import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BaptismHistoryComponent } from './list/baptism-history.component';
import { BaptismHistoryDetailComponent } from './detail/baptism-history-detail.component';
import { BaptismHistoryUpdateComponent } from './update/baptism-history-update.component';
import { BaptismHistoryDeleteDialogComponent } from './delete/baptism-history-delete-dialog.component';
import { BaptismHistoryRoutingModule } from './route/baptism-history-routing.module';

@NgModule({
  imports: [SharedModule, BaptismHistoryRoutingModule],
  declarations: [
    BaptismHistoryComponent,
    BaptismHistoryDetailComponent,
    BaptismHistoryUpdateComponent,
    BaptismHistoryDeleteDialogComponent,
  ],
})
export class BaptismHistoryModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CongregantComponent } from './list/congregant.component';
import { CongregantDetailComponent } from './detail/congregant-detail.component';
import { CongregantUpdateComponent } from './update/congregant-update.component';
import { CongregantDeleteDialogComponent } from './delete/congregant-delete-dialog.component';
import { CongregantRoutingModule } from './route/congregant-routing.module';

@NgModule({
  imports: [SharedModule, CongregantRoutingModule],
  declarations: [CongregantComponent, CongregantDetailComponent, CongregantUpdateComponent, CongregantDeleteDialogComponent],
})
export class CongregantModule {}

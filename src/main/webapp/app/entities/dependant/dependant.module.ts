import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DependantComponent } from './list/dependant.component';
import { DependantDetailComponent } from './detail/dependant-detail.component';
import { DependantUpdateComponent } from './update/dependant-update.component';
import { DependantDeleteDialogComponent } from './delete/dependant-delete-dialog.component';
import { DependantRoutingModule } from './route/dependant-routing.module';

@NgModule({
  imports: [SharedModule, DependantRoutingModule],
  declarations: [DependantComponent, DependantDetailComponent, DependantUpdateComponent, DependantDeleteDialogComponent],
})
export class DependantModule {}

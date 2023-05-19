import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICongregant } from '../congregant.model';
import { CongregantService } from '../service/congregant.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './congregant-delete-dialog.component.html',
})
export class CongregantDeleteDialogComponent {
  congregant?: ICongregant;

  constructor(protected congregantService: CongregantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.congregantService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

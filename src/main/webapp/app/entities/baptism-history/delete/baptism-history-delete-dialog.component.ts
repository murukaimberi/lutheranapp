import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBaptismHistory } from '../baptism-history.model';
import { BaptismHistoryService } from '../service/baptism-history.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './baptism-history-delete-dialog.component.html',
})
export class BaptismHistoryDeleteDialogComponent {
  baptismHistory?: IBaptismHistory;

  constructor(protected baptismHistoryService: BaptismHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.baptismHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

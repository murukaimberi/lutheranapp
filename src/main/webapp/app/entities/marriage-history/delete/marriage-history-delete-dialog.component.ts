import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMarriageHistory } from '../marriage-history.model';
import { MarriageHistoryService } from '../service/marriage-history.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './marriage-history-delete-dialog.component.html',
})
export class MarriageHistoryDeleteDialogComponent {
  marriageHistory?: IMarriageHistory;

  constructor(protected marriageHistoryService: MarriageHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.marriageHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

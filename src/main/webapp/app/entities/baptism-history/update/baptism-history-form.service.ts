import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBaptismHistory, NewBaptismHistory } from '../baptism-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBaptismHistory for edit and NewBaptismHistoryFormGroupInput for create.
 */
type BaptismHistoryFormGroupInput = IBaptismHistory | PartialWithRequiredKeyOf<NewBaptismHistory>;

type BaptismHistoryFormDefaults = Pick<NewBaptismHistory, 'id'>;

type BaptismHistoryFormGroupContent = {
  id: FormControl<IBaptismHistory['id'] | NewBaptismHistory['id']>;
  parishName: FormControl<IBaptismHistory['parishName']>;
  baptismDate: FormControl<IBaptismHistory['baptismDate']>;
  confirmedDate: FormControl<IBaptismHistory['confirmedDate']>;
  parishBaptisedAt: FormControl<IBaptismHistory['parishBaptisedAt']>;
  prishedConfirmedAt: FormControl<IBaptismHistory['prishedConfirmedAt']>;
};

export type BaptismHistoryFormGroup = FormGroup<BaptismHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BaptismHistoryFormService {
  createBaptismHistoryFormGroup(baptismHistory: BaptismHistoryFormGroupInput = { id: null }): BaptismHistoryFormGroup {
    const baptismHistoryRawValue = {
      ...this.getFormDefaults(),
      ...baptismHistory,
    };
    return new FormGroup<BaptismHistoryFormGroupContent>({
      id: new FormControl(
        { value: baptismHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      parishName: new FormControl(baptismHistoryRawValue.parishName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      baptismDate: new FormControl(baptismHistoryRawValue.baptismDate, {
        validators: [Validators.required],
      }),
      confirmedDate: new FormControl(baptismHistoryRawValue.confirmedDate, {
        validators: [Validators.required],
      }),
      parishBaptisedAt: new FormControl(baptismHistoryRawValue.parishBaptisedAt, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      prishedConfirmedAt: new FormControl(baptismHistoryRawValue.prishedConfirmedAt, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
    });
  }

  getBaptismHistory(form: BaptismHistoryFormGroup): IBaptismHistory | NewBaptismHistory {
    return form.getRawValue() as IBaptismHistory | NewBaptismHistory;
  }

  resetForm(form: BaptismHistoryFormGroup, baptismHistory: BaptismHistoryFormGroupInput): void {
    const baptismHistoryRawValue = { ...this.getFormDefaults(), ...baptismHistory };
    form.reset(
      {
        ...baptismHistoryRawValue,
        id: { value: baptismHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BaptismHistoryFormDefaults {
    return {
      id: null,
    };
  }
}

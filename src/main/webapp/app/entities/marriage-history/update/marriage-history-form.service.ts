import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMarriageHistory, NewMarriageHistory } from '../marriage-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMarriageHistory for edit and NewMarriageHistoryFormGroupInput for create.
 */
type MarriageHistoryFormGroupInput = IMarriageHistory | PartialWithRequiredKeyOf<NewMarriageHistory>;

type MarriageHistoryFormDefaults = Pick<NewMarriageHistory, 'id'>;

type MarriageHistoryFormGroupContent = {
  id: FormControl<IMarriageHistory['id'] | NewMarriageHistory['id']>;
  marriageDate: FormControl<IMarriageHistory['marriageDate']>;
  parishMarriedAt: FormControl<IMarriageHistory['parishMarriedAt']>;
};

export type MarriageHistoryFormGroup = FormGroup<MarriageHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MarriageHistoryFormService {
  createMarriageHistoryFormGroup(marriageHistory: MarriageHistoryFormGroupInput = { id: null }): MarriageHistoryFormGroup {
    const marriageHistoryRawValue = {
      ...this.getFormDefaults(),
      ...marriageHistory,
    };
    return new FormGroup<MarriageHistoryFormGroupContent>({
      id: new FormControl(
        { value: marriageHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      marriageDate: new FormControl(marriageHistoryRawValue.marriageDate, {
        validators: [Validators.required],
      }),
      parishMarriedAt: new FormControl(marriageHistoryRawValue.parishMarriedAt, {
        validators: [Validators.required],
      }),
    });
  }

  getMarriageHistory(form: MarriageHistoryFormGroup): IMarriageHistory | NewMarriageHistory {
    return form.getRawValue() as IMarriageHistory | NewMarriageHistory;
  }

  resetForm(form: MarriageHistoryFormGroup, marriageHistory: MarriageHistoryFormGroupInput): void {
    const marriageHistoryRawValue = { ...this.getFormDefaults(), ...marriageHistory };
    form.reset(
      {
        ...marriageHistoryRawValue,
        id: { value: marriageHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MarriageHistoryFormDefaults {
    return {
      id: null,
    };
  }
}

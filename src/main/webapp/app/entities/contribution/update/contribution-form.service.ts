import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContribution, NewContribution } from '../contribution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContribution for edit and NewContributionFormGroupInput for create.
 */
type ContributionFormGroupInput = IContribution | PartialWithRequiredKeyOf<NewContribution>;

type ContributionFormDefaults = Pick<NewContribution, 'id'>;

type ContributionFormGroupContent = {
  id: FormControl<IContribution['id'] | NewContribution['id']>;
  contributionType: FormControl<IContribution['contributionType']>;
  frequency: FormControl<IContribution['frequency']>;
  month: FormControl<IContribution['month']>;
  amount: FormControl<IContribution['amount']>;
  congregant: FormControl<IContribution['congregant']>;
};

export type ContributionFormGroup = FormGroup<ContributionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContributionFormService {
  createContributionFormGroup(contribution: ContributionFormGroupInput = { id: null }): ContributionFormGroup {
    const contributionRawValue = {
      ...this.getFormDefaults(),
      ...contribution,
    };
    return new FormGroup<ContributionFormGroupContent>({
      id: new FormControl(
        { value: contributionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      contributionType: new FormControl(contributionRawValue.contributionType, {
        validators: [Validators.required],
      }),
      frequency: new FormControl(contributionRawValue.frequency, {
        validators: [Validators.required],
      }),
      month: new FormControl(contributionRawValue.month, {
        validators: [Validators.required],
      }),
      amount: new FormControl(contributionRawValue.amount, {
        validators: [Validators.required],
      }),
      congregant: new FormControl(contributionRawValue.congregant, {
        validators: [Validators.required],
      }),
    });
  }

  getContribution(form: ContributionFormGroup): IContribution | NewContribution {
    return form.getRawValue() as IContribution | NewContribution;
  }

  resetForm(form: ContributionFormGroup, contribution: ContributionFormGroupInput): void {
    const contributionRawValue = { ...this.getFormDefaults(), ...contribution };
    form.reset(
      {
        ...contributionRawValue,
        id: { value: contributionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContributionFormDefaults {
    return {
      id: null,
    };
  }
}

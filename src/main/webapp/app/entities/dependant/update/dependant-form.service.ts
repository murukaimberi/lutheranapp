import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDependant, NewDependant } from '../dependant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDependant for edit and NewDependantFormGroupInput for create.
 */
type DependantFormGroupInput = IDependant | PartialWithRequiredKeyOf<NewDependant>;

type DependantFormDefaults = Pick<NewDependant, 'id'>;

type DependantFormGroupContent = {
  id: FormControl<IDependant['id'] | NewDependant['id']>;
  fullNames: FormControl<IDependant['fullNames']>;
  dateOfBirth: FormControl<IDependant['dateOfBirth']>;
  gender: FormControl<IDependant['gender']>;
  congregant: FormControl<IDependant['congregant']>;
};

export type DependantFormGroup = FormGroup<DependantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DependantFormService {
  createDependantFormGroup(dependant: DependantFormGroupInput = { id: null }): DependantFormGroup {
    const dependantRawValue = {
      ...this.getFormDefaults(),
      ...dependant,
    };
    return new FormGroup<DependantFormGroupContent>({
      id: new FormControl(
        { value: dependantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fullNames: new FormControl(dependantRawValue.fullNames, {
        validators: [Validators.required, Validators.maxLength(250)],
      }),
      dateOfBirth: new FormControl(dependantRawValue.dateOfBirth, {
        validators: [Validators.required],
      }),
      gender: new FormControl(dependantRawValue.gender, {
        validators: [Validators.required],
      }),
      congregant: new FormControl(dependantRawValue.congregant, {
        validators: [Validators.required],
      }),
    });
  }

  getDependant(form: DependantFormGroup): IDependant | NewDependant {
    return form.getRawValue() as IDependant | NewDependant;
  }

  resetForm(form: DependantFormGroup, dependant: DependantFormGroupInput): void {
    const dependantRawValue = { ...this.getFormDefaults(), ...dependant };
    form.reset(
      {
        ...dependantRawValue,
        id: { value: dependantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DependantFormDefaults {
    return {
      id: null,
    };
  }
}

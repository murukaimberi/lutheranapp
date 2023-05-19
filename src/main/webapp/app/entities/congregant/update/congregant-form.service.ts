import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICongregant, NewCongregant } from '../congregant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICongregant for edit and NewCongregantFormGroupInput for create.
 */
type CongregantFormGroupInput = ICongregant | PartialWithRequiredKeyOf<NewCongregant>;

type CongregantFormDefaults = Pick<NewCongregant, 'id' | 'leagues'>;

type CongregantFormGroupContent = {
  id: FormControl<ICongregant['id'] | NewCongregant['id']>;
  title: FormControl<ICongregant['title']>;
  firstNames: FormControl<ICongregant['firstNames']>;
  surname: FormControl<ICongregant['surname']>;
  email: FormControl<ICongregant['email']>;
  dob: FormControl<ICongregant['dob']>;
  gender: FormControl<ICongregant['gender']>;
  profession: FormControl<ICongregant['profession']>;
  maritalStatus: FormControl<ICongregant['maritalStatus']>;
  profilePicture: FormControl<ICongregant['profilePicture']>;
  profilePictureContentType: FormControl<ICongregant['profilePictureContentType']>;
  marriageHistory: FormControl<ICongregant['marriageHistory']>;
  baptismHistory: FormControl<ICongregant['baptismHistory']>;
  user: FormControl<ICongregant['user']>;
  leagues: FormControl<ICongregant['leagues']>;
};

export type CongregantFormGroup = FormGroup<CongregantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CongregantFormService {
  createCongregantFormGroup(congregant: CongregantFormGroupInput = { id: null }): CongregantFormGroup {
    const congregantRawValue = {
      ...this.getFormDefaults(),
      ...congregant,
    };
    return new FormGroup<CongregantFormGroupContent>({
      id: new FormControl(
        { value: congregantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(congregantRawValue.title, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      firstNames: new FormControl(congregantRawValue.firstNames, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      surname: new FormControl(congregantRawValue.surname, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      email: new FormControl(congregantRawValue.email, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      dob: new FormControl(congregantRawValue.dob, {
        validators: [Validators.required],
      }),
      gender: new FormControl(congregantRawValue.gender),
      profession: new FormControl(congregantRawValue.profession, {
        validators: [Validators.maxLength(100)],
      }),
      maritalStatus: new FormControl(congregantRawValue.maritalStatus, {
        validators: [Validators.required],
      }),
      profilePicture: new FormControl(congregantRawValue.profilePicture),
      profilePictureContentType: new FormControl(congregantRawValue.profilePictureContentType),
      marriageHistory: new FormControl(congregantRawValue.marriageHistory),
      baptismHistory: new FormControl(congregantRawValue.baptismHistory),
      user: new FormControl(congregantRawValue.user, {
        validators: [Validators.required],
      }),
      leagues: new FormControl(congregantRawValue.leagues ?? []),
    });
  }

  getCongregant(form: CongregantFormGroup): ICongregant | NewCongregant {
    return form.getRawValue() as ICongregant | NewCongregant;
  }

  resetForm(form: CongregantFormGroup, congregant: CongregantFormGroupInput): void {
    const congregantRawValue = { ...this.getFormDefaults(), ...congregant };
    form.reset(
      {
        ...congregantRawValue,
        id: { value: congregantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CongregantFormDefaults {
    return {
      id: null,
      leagues: [],
    };
  }
}

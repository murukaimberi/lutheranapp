import dayjs from 'dayjs/esm';
import { ICongregant } from 'app/entities/congregant/congregant.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IDependant {
  id: number;
  fullNames?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  gender?: Gender | null;
  congregant?: Pick<ICongregant, 'id' | 'surname'> | null;
}

export type NewDependant = Omit<IDependant, 'id'> & { id: null };

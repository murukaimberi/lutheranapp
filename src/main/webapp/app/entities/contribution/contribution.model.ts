import dayjs from 'dayjs/esm';
import { ICongregant } from 'app/entities/congregant/congregant.model';
import { ContributionType } from 'app/entities/enumerations/contribution-type.model';
import { Frequency } from 'app/entities/enumerations/frequency.model';

export interface IContribution {
  id: number;
  contributionType?: ContributionType | null;
  frequency?: Frequency | null;
  month?: dayjs.Dayjs | null;
  amount?: number | null;
  congregant?: Pick<ICongregant, 'id' | 'surname'> | null;
}

export type NewContribution = Omit<IContribution, 'id'> & { id: null };

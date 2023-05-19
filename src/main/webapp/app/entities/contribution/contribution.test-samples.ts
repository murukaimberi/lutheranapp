import dayjs from 'dayjs/esm';

import { ContributionType } from 'app/entities/enumerations/contribution-type.model';
import { Frequency } from 'app/entities/enumerations/frequency.model';

import { IContribution, NewContribution } from './contribution.model';

export const sampleWithRequiredData: IContribution = {
  id: 36574,
  contributionType: ContributionType['PLEDGES'],
  frequency: Frequency['MONTHLY'],
  month: dayjs('2023-05-18'),
  amount: 13537,
};

export const sampleWithPartialData: IContribution = {
  id: 16587,
  contributionType: ContributionType['HAVERST'],
  frequency: Frequency['LUMP_SUM'],
  month: dayjs('2023-05-18'),
  amount: 22212,
};

export const sampleWithFullData: IContribution = {
  id: 52366,
  contributionType: ContributionType['HAVERST'],
  frequency: Frequency['LUMP_SUM'],
  month: dayjs('2023-05-17'),
  amount: 48268,
};

export const sampleWithNewData: NewContribution = {
  contributionType: ContributionType['ANNUAL_DUES'],
  frequency: Frequency['YEARLY'],
  month: dayjs('2023-05-18'),
  amount: 29956,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import dayjs from 'dayjs/esm';

import { IMarriageHistory, NewMarriageHistory } from './marriage-history.model';

export const sampleWithRequiredData: IMarriageHistory = {
  id: 1789,
  marriageDate: dayjs('2023-05-17'),
  parishMarriedAt: 'alarm',
};

export const sampleWithPartialData: IMarriageHistory = {
  id: 37263,
  marriageDate: dayjs('2023-05-18'),
  parishMarriedAt: 'disintermediate',
};

export const sampleWithFullData: IMarriageHistory = {
  id: 78636,
  marriageDate: dayjs('2023-05-18'),
  parishMarriedAt: 'Soft dot-com',
};

export const sampleWithNewData: NewMarriageHistory = {
  marriageDate: dayjs('2023-05-18'),
  parishMarriedAt: 'Kansas Account Philippine',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

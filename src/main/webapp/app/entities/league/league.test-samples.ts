import dayjs from 'dayjs/esm';

import { ILeague, NewLeague } from './league.model';

export const sampleWithRequiredData: ILeague = {
  id: 11233,
  name: 'streamline Automated Manager',
  description: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2023-05-17'),
};

export const sampleWithPartialData: ILeague = {
  id: 62584,
  name: 'CFP Fantastic',
  description: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2023-05-18'),
};

export const sampleWithFullData: ILeague = {
  id: 42072,
  name: 'International Lane',
  description: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2023-05-17'),
};

export const sampleWithNewData: NewLeague = {
  name: 'Computers Product engage',
  description: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2023-05-18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

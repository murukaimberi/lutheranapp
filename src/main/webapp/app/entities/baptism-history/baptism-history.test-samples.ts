import dayjs from 'dayjs/esm';

import { IBaptismHistory, NewBaptismHistory } from './baptism-history.model';

export const sampleWithRequiredData: IBaptismHistory = {
  id: 89574,
  parishName: 'value-added',
  baptismDate: dayjs('2023-05-18'),
  confirmedDate: dayjs('2023-05-17'),
  parishBaptisedAt: 'Granite connecting',
  prishedConfirmedAt: 'Optimization 6th digital',
};

export const sampleWithPartialData: IBaptismHistory = {
  id: 98957,
  parishName: 'bandwidth Monitored policy',
  baptismDate: dayjs('2023-05-18'),
  confirmedDate: dayjs('2023-05-18'),
  parishBaptisedAt: 'pixel',
  prishedConfirmedAt: 'Alaska',
};

export const sampleWithFullData: IBaptismHistory = {
  id: 20307,
  parishName: 'redundant',
  baptismDate: dayjs('2023-05-17'),
  confirmedDate: dayjs('2023-05-18'),
  parishBaptisedAt: 'Checking architect',
  prishedConfirmedAt: 'port',
};

export const sampleWithNewData: NewBaptismHistory = {
  parishName: 'Ergonomic',
  baptismDate: dayjs('2023-05-18'),
  confirmedDate: dayjs('2023-05-18'),
  parishBaptisedAt: 'Account hacking Functionality',
  prishedConfirmedAt: 'tan',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

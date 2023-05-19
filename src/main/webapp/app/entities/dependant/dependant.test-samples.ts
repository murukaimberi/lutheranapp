import dayjs from 'dayjs/esm';

import { Gender } from 'app/entities/enumerations/gender.model';

import { IDependant, NewDependant } from './dependant.model';

export const sampleWithRequiredData: IDependant = {
  id: 76848,
  fullNames: 'e-tailers Kroon forecast',
  dateOfBirth: dayjs('2023-05-18'),
  gender: Gender['FEMALE'],
};

export const sampleWithPartialData: IDependant = {
  id: 19277,
  fullNames: 'drive emulation',
  dateOfBirth: dayjs('2023-05-18'),
  gender: Gender['FEMALE'],
};

export const sampleWithFullData: IDependant = {
  id: 30280,
  fullNames: 'Franc Accountability',
  dateOfBirth: dayjs('2023-05-17'),
  gender: Gender['FEMALE'],
};

export const sampleWithNewData: NewDependant = {
  fullNames: 'Officer Games',
  dateOfBirth: dayjs('2023-05-18'),
  gender: Gender['FEMALE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

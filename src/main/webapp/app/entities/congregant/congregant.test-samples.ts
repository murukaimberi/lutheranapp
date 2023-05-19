import dayjs from 'dayjs/esm';

import { Gender } from 'app/entities/enumerations/gender.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';

import { ICongregant, NewCongregant } from './congregant.model';

export const sampleWithRequiredData: ICongregant = {
  id: 21483,
  title: 'Bedfordshire enhance',
  firstNames: 'compressing National primary',
  surname: 'primary',
  email: 'Al.Wiegand98@hotmail.com',
  dob: dayjs('2023-05-18'),
  maritalStatus: MaritalStatus['DEVORCED'],
};

export const sampleWithPartialData: ICongregant = {
  id: 78436,
  title: 'HDD',
  firstNames: 'deposit International deposit',
  surname: 'bluetooth reboot',
  email: 'Gus.Larson31@gmail.com',
  dob: dayjs('2023-05-18'),
  maritalStatus: MaritalStatus['WIDOWED'],
  profilePicture: '../fake-data/blob/hipster.png',
  profilePictureContentType: 'unknown',
};

export const sampleWithFullData: ICongregant = {
  id: 12751,
  title: 'Small Plastic',
  firstNames: 'Plain',
  surname: 'Optimized',
  email: 'Koby.Koch@hotmail.com',
  dob: dayjs('2023-05-17'),
  gender: Gender['FEMALE'],
  profession: 'Loan Soft bandwidth-monitored',
  maritalStatus: MaritalStatus['MARRIED'],
  profilePicture: '../fake-data/blob/hipster.png',
  profilePictureContentType: 'unknown',
};

export const sampleWithNewData: NewCongregant = {
  title: 'GB hack Rubber',
  firstNames: 'open-source',
  surname: 'Producer synergistic Palestinian',
  email: 'Alvena_Schimmel75@yahoo.com',
  dob: dayjs('2023-05-17'),
  maritalStatus: MaritalStatus['WIDOWED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import dayjs from 'dayjs/esm';

import { IPost, NewPost } from './post.model';

export const sampleWithRequiredData: IPost = {
  id: 35989,
  title: 'Fantastic',
  description: '../fake-data/blob/hipster.txt',
  postedDate: dayjs('2023-05-18'),
};

export const sampleWithPartialData: IPost = {
  id: 54350,
  title: 'Personal index Forward',
  subtitle: 'copying',
  description: '../fake-data/blob/hipster.txt',
  postedDate: dayjs('2023-05-17'),
};

export const sampleWithFullData: IPost = {
  id: 18600,
  title: 'Stravenue Account',
  subtitle: 'program neural-net',
  description: '../fake-data/blob/hipster.txt',
  postedDate: dayjs('2023-05-18'),
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewPost = {
  title: 'synthesize Metrics',
  description: '../fake-data/blob/hipster.txt',
  postedDate: dayjs('2023-05-17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

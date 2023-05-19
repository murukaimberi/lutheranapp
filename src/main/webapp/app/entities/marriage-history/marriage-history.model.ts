import dayjs from 'dayjs/esm';

export interface IMarriageHistory {
  id: number;
  marriageDate?: dayjs.Dayjs | null;
  parishMarriedAt?: string | null;
}

export type NewMarriageHistory = Omit<IMarriageHistory, 'id'> & { id: null };

import dayjs from 'dayjs/esm';

export interface IBaptismHistory {
  id: number;
  parishName?: string | null;
  baptismDate?: dayjs.Dayjs | null;
  confirmedDate?: dayjs.Dayjs | null;
  parishBaptisedAt?: string | null;
  prishedConfirmedAt?: string | null;
}

export type NewBaptismHistory = Omit<IBaptismHistory, 'id'> & { id: null };

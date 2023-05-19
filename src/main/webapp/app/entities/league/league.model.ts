import dayjs from 'dayjs/esm';
import { ICongregant } from 'app/entities/congregant/congregant.model';

export interface ILeague {
  id: number;
  name?: string | null;
  description?: string | null;
  createdDate?: dayjs.Dayjs | null;
  congregants?: Pick<ICongregant, 'id'>[] | null;
}

export type NewLeague = Omit<ILeague, 'id'> & { id: null };

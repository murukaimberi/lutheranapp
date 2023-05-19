import dayjs from 'dayjs/esm';
import { ICongregant } from 'app/entities/congregant/congregant.model';

export interface IPost {
  id: number;
  title?: string | null;
  subtitle?: string | null;
  description?: string | null;
  postedDate?: dayjs.Dayjs | null;
  image?: string | null;
  imageContentType?: string | null;
  congregant?: Pick<ICongregant, 'id' | 'surname'> | null;
}

export type NewPost = Omit<IPost, 'id'> & { id: null };

import dayjs from 'dayjs/esm';
import { IMarriageHistory } from 'app/entities/marriage-history/marriage-history.model';
import { IBaptismHistory } from 'app/entities/baptism-history/baptism-history.model';
import { IUser } from 'app/entities/user/user.model';
import { ILeague } from 'app/entities/league/league.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';

export interface ICongregant {
  id: number;
  title?: string | null;
  firstNames?: string | null;
  surname?: string | null;
  email?: string | null;
  dob?: dayjs.Dayjs | null;
  gender?: Gender | null;
  profession?: string | null;
  maritalStatus?: MaritalStatus | null;
  profilePicture?: string | null;
  profilePictureContentType?: string | null;
  marriageHistory?: Pick<IMarriageHistory, 'id'> | null;
  baptismHistory?: Pick<IBaptismHistory, 'id'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  leagues?: Pick<ILeague, 'id'>[] | null;
}

export type NewCongregant = Omit<ICongregant, 'id'> & { id: null };

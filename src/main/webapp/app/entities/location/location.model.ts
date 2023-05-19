import { ICountry } from 'app/entities/country/country.model';
import { ICongregant } from 'app/entities/congregant/congregant.model';

export interface ILocation {
  id: number;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  country?: Pick<ICountry, 'id'> | null;
  congregant?: Pick<ICongregant, 'id' | 'surname'> | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };

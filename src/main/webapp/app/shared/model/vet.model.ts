import { ISpeciality } from 'app/shared/model/speciality.model';

export interface IVet {
  id?: number;
  firstName?: string;
  lastName?: string;
  salary?: number;
  specialities?: ISpeciality[] | null;
}

export const defaultValue: Readonly<IVet> = {};

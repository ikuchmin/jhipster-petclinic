import { IVet } from 'app/shared/model/vet.model';

export interface ISpeciality {
  id?: number;
  name?: string;
  vets?: IVet[] | null;
}

export const defaultValue: Readonly<ISpeciality> = {};

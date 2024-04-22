import dayjs from 'dayjs';
import { IVet } from 'app/shared/model/vet.model';
import { IPet } from 'app/shared/model/pet.model';

export interface IVisit {
  id?: number;
  date?: dayjs.Dayjs | null;
  vet?: IVet | null;
  pet?: IPet | null;
}

export const defaultValue: Readonly<IVisit> = {};

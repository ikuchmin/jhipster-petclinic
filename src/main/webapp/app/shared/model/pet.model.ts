import dayjs from 'dayjs';
import { IPetType } from 'app/shared/model/pet-type.model';
import { IOwner } from 'app/shared/model/owner.model';

export interface IPet {
  id?: number;
  name?: string;
  birthDate?: dayjs.Dayjs | null;
  type?: IPetType | null;
  owner?: IOwner | null;
}

export const defaultValue: Readonly<IPet> = {};

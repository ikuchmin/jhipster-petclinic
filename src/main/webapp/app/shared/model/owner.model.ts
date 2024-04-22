export interface IOwner {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string | null;
  address?: string | null;
  city?: string | null;
  telephone?: string | null;
}

export const defaultValue: Readonly<IOwner> = {};

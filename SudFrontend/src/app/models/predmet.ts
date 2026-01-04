import { Sud } from './sud';

export class Predmet {
  id!: number;
  brojPredmeta!: string;
  opis!: string;
  datumPocetka!: Date;
  aktivan!: boolean;
  sud?: Sud;
}
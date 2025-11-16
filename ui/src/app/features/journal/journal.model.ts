export interface Journal {
  date: Date;
  content: string;
  tags: string[];
}

export interface JournalDto {
  date: string;
  content: string;
  tags: string[];
}

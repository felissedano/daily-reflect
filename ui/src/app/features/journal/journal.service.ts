import { Injectable } from '@angular/core';
import {delay, Observable, of} from "rxjs";
import {Journal} from "./journal.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class JournalService {

  private API_URL = environment.baseUrl;

  constructor(private httpClient: HttpClient) { }

  private mockJournals: Journal[] = [
    {
      date: new Date(new Date().getFullYear(), new Date().getMonth(), 20),
      labels: ['neutral', 'quiet day'],
      content: "I wish something interesting happened"
    },
    {
      date: new Date(new Date().getFullYear(), new Date().getMonth() - 1, 18),
      labels: [],
      content: "What a day"
    },
    {
      date: new Date(new Date().getFullYear(), new Date().getMonth(), 13),
      labels: ['happy'],
      content: "Today was one of the day of all time"
    }
  ];

  getJournalOfMonth(year: number, month: number): Observable<Journal[]> {
    // TODO replace mock with actual logic
    const filteredJournal = this.mockJournals.filter(journal => {
      console.log(journal.date)
      console.log(journal.date.getFullYear(), year, journal.date.getMonth(), month)
      return journal.date.getFullYear() === year &&
        journal.date.getMonth() === month;
    });

    return of(filteredJournal).pipe(delay(300));
  }
}

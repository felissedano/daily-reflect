import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Journal } from './journal.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { stringfyDate, stringfyYearMonth } from '../../shared/util/dateUtil';

@Injectable({
  providedIn: 'root',
})
export class JournalService {
  private API_URL = environment.baseUrl;

  constructor(private httpClient: HttpClient) {}

  getJournalByDate(date: Date): Observable<Journal> {
    const dateString: string = stringfyDate(date);
    return this.httpClient.get<Journal>(
      this.API_URL + 'api/journal/date/' + dateString,
    );
  }

  getJournalByYearMonth(year: number, month: number): Observable<Journal[]> {
    const yearMonthString: string = stringfyYearMonth(year, month);
    return this.httpClient
      .get<
        { content: string; tags: string[]; date: string }[]
      >(this.API_URL + 'api/journal/year-month/' + yearMonthString)
      .pipe(
        map((journalObjs) =>
          journalObjs.map((journalObj) => ({
            content: journalObj.content,
            tags: journalObj.tags,
            date: new Date(journalObj.date),
          })),
        ),
      );
  }

  saveJournal(journal: Journal): Observable<Journal> {
    const headers: HttpHeaders = new HttpHeaders().set(
      'content-type',
      'application/json',
    );

    return this.httpClient.post<Journal>(
      this.API_URL + 'api/journal/edit',
      journal,
      {
        headers: headers,
      },
    );
  }
}

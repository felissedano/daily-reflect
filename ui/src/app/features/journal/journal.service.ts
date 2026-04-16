import { Injectable } from '@angular/core';
import { forkJoin, from, map, Observable, of, switchMap } from 'rxjs';
import { Journal, JournalDto } from './journal.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { stringfyDate, stringfyYearMonth } from '../../shared/util/dateUtil';
import { CryptoService } from '../../core/crypto/crypto.service';

@Injectable({
  providedIn: 'root',
})
export class JournalService {
  private API_URL = environment.baseUrl;

  constructor(
    private httpClient: HttpClient,
    private cryptoService: CryptoService,
  ) {}

  getJournalByDate(date: Date): Observable<Journal | null> {
    const dateString: string = stringfyDate(date);
    return this.httpClient
      .get<JournalDto>(this.API_URL + 'api/journal/date/' + dateString)
      .pipe(switchMap((journalObj) => this.decryptJournal(journalObj)));
  }

  getJournalByYearMonth(year: number, month: number): Observable<Journal[]> {
    const yearMonthString: string = stringfyYearMonth(year, month);
    return this.httpClient
      .get<
        JournalDto[]
      >(this.API_URL + 'api/journal/year-month/' + yearMonthString)
      .pipe(
        switchMap((journalObjs: JournalDto[]) =>
          forkJoin(
            journalObjs.map((journalObj) => this.decryptJournal(journalObj)),
          ),
        ),
        switchMap((journals) =>
          of(journals.filter((journal) => journal !== null)),
        ),
      );
  }

  saveJournal(journal: Journal): Observable<void> {
    const headers: HttpHeaders = new HttpHeaders().set(
      'content-type',
      'application/json',
    );

    return this.encryptJournal(journal).pipe(
      switchMap((journalDto) =>
        this.httpClient.post<void>(
          this.API_URL + 'api/journal/edit',
          journalDto,
          {
            headers: headers,
          },
        ),
      ),
    );
  }

  private encryptJournal(journal: Journal): Observable<JournalDto> {
    return from(this.cryptoService.encryptData(journal.content)).pipe(
      switchMap((value) => {
        const prevIv: Uint8Array<ArrayBuffer> = new Uint8Array(
          this.cryptoService.fromBase64(value.iv),
        );

        // Do not do fork join if tags len is 0 as forkJoin will not execute
        if (journal.tags.length === 0) {
          return of({
            content: value.cipherText,
            tags: [],
            iv: value.iv,
            date: stringfyDate(journal.date),
          });
        }

        return forkJoin(
          journal.tags.map((tag) =>
            this.cryptoService.encryptData(tag, prevIv),
          ),
        ).pipe(
          map((encryptedTags): JournalDto => {
            return {
              content: value.cipherText,
              tags: encryptedTags.map((tagAndIv) => tagAndIv.cipherText),
              iv: value.iv,
              date: stringfyDate(journal.date),
            };
          }),
        );
      }),
    );
  }

  private decryptJournal(
    journalDto: JournalDto | null,
  ): Observable<Journal | null> {
    if (journalDto == null) {
      return of(null);
    }

    const iv = journalDto.iv;

    return forkJoin({
      content: from(this.cryptoService.decryptData(journalDto.content, iv)),
      // Do not do fork join if tags len is 0 as forkJoin will not execute
      tags:
        journalDto.tags.length === 0
          ? of([])
          : forkJoin<string[]>(
              journalDto.tags.map((tag) =>
                from(this.cryptoService.decryptData(tag, iv)),
              ),
            ),
    }).pipe(
      map(
        ({ content, tags }): Journal => ({
          tags: tags,
          content: content,
          date: new Date(journalDto.date),
        }),
      ),
    );
  }
}

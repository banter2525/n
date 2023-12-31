import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

enum TYPE {
    SQL = 'SQL',
    JSON = 'JSON'
}

@Injectable()
export class SyntaxHighlightDataService {
    private url = 'api/bind';
    constructor(private http: HttpClient) { }
    getData({type, originalContents, bindValue}: ISyntaxHighlightData): Observable<{bindedQuery: string}> {
        let requestType;
        switch (type) {
            case TYPE.SQL:
                requestType = 'sql';
                break;
            case TYPE.JSON:
                requestType = 'mongoJson';
                break;
        }
        return this.http.post<{bindedQuery: string}>(
            this.url,
            `type=${requestType}&metaData=${encodeURIComponent(originalContents)}&bind=${encodeURIComponent(bindValue)}`,
            {
                headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
            }
        );
    }
}

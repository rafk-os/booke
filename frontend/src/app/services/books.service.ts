import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Book} from "../model/Book";

@Injectable({
  providedIn: 'root'
})
export class BooksService {

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Book[]>(`http://localhost:8080/api/books`);
  }

  create(book: Book) {
    return this.http.post(`http://localhost:8080/api/books`, book);
  }

  addToCart(id :number) {
    // @ts-ignore
    return this.http.post(`http://localhost:8080/api/books/${id}/cart`);
  }
  deleteFromCart(id:number){
    return this.http.delete(`http://localhost:8080/api/books/${id}/cart`);
  }
  delete(id: number) {
    return this.http.delete(`http://localhost:8080/api/books/${id}`);
  }

}

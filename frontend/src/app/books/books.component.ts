import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService} from "../services/alert.service";
import {BooksService} from "../services/books.service";
import {first} from "rxjs";
import {Book} from "../model/Book";
import {LoginService} from "../services/login.service";
import {UserService} from "../services/user.service";
import {NgbModal, ModalDismissReasons, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Title} from "@angular/platform-browser";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {OrdersService} from "../services/orders.service";

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
   newBookForm!: FormGroup;
   books : Book[] = []
   bookInCart : Book[] = []
   isAdmin : boolean = false
   submitted = false;
   loading = false;
   modalReference!: NgbModalRef;
   sum : number = 0.0;


  constructor(

    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private titleService:Title,
    private route: ActivatedRoute,
    private router: Router,
    private bookService: BooksService,
    private orderService: OrdersService,
    private loginService: LoginService,
    private userService: UserService,
    private alertService: AlertService
  ) {
    this.titleService.setTitle("Books");
    this.bookService.getAll().pipe(first())
      .subscribe({
        next: data => {
          data.forEach((value: Book) => this.books.push(Object.assign({},value)))
        },
        error: error => {
          this.alertService.error("Wystąpił błąd przy pobieraniu książek z bazy danych!");
        }

      })

    this.userService.getUserCart(loginService.currentUserValue.id).pipe(first())
      .subscribe({
        next: data => {
          data.forEach(
            (value: Book) => {
              this.bookInCart.push(Object.assign({},value))
              this.sum += value.price;
              const bookIndex = this.books.findIndex((book => book.id == value.id));
              if(bookIndex != -1)
                this.books[bookIndex].addedToCart = true;
            }

          )
          orderService.setPrice(this.sum);
        },
        error: error => {
          this.alertService.error("Wystąpił błąd przy pobieraniu koszyka zakupu użytkownika!");
        }

      })
    // @ts-ignore
    this.userService.getUserRole(loginService.currentUserValue.id).pipe(first())
      .subscribe({
        next: data => {
          if (data.find(role => role.name == "ADMIN"))
            this.isAdmin = true;
        },
        error: error => {
          this.alertService.error("Wystąpił błąd przy pobieraniu roli użytkownika!");
        }

      })




  }

  onSubmit() {
    this.submitted = true;

    this.alertService.clear();

    if (this.newBookForm.invalid) {
      return;
    }
    this.loading = true;

    this.bookService.create( new Book(0,this.f['title'].value,this.f['author'].value,this.f['description'].value, this.f['price'].value, false)).pipe(first())
      .subscribe({
        next: data => {
          window.location.reload();
          this.modalReference.close()
        },
        error: error => {
          this.alertService.error("Wystąpił błąd przy dodawaniu książki z bazy danych!");
        }

      })
  }

  get f() {
    return this.newBookForm.controls;
  }

  ngOnInit(): void {
    this.newBookForm = this.formBuilder.group({
      title: ['', Validators.required],
      author: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', Validators.required]
    });

  }

  reloadComponent() {
    let currentUrl = this.router.url;
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([currentUrl]);
  }

  deleteBook(id : number){

    this.bookService.delete(id).pipe(first())
      .subscribe({
        next: data => {
          window.location.reload();
        },
        error: error => {
          this.alertService.error("Wystąpił błąd przy usuwaniu książki z bazy danych!");
        }

      })

  }

  addBookToCart(id : number){
    this.bookService.addToCart(id).pipe(first())
      .subscribe({
        next: data => {
          window.location.reload();
        },
        error: error => {
          this.alertService.error("Wystąpił błąd przy dodawaniu książki do koszyka!");
        }

      })
  }

  deleteBookFromCart(id : number){

    this.bookService.deleteFromCart(id).pipe(first())
      .subscribe({
        next: data => {
          window.location.reload();
        },
        error: error => {
          this.alertService.error("Wystąpił błąd przy usuwaniu książki z koszyka!");
        }

      })

  }



  open(content: any) {
   this.modalReference = this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'});
  }

  open_cart(content_cart: any) {
   this.modalReference = this.modalService.open(content_cart, { size: 'lg', centered : true, ariaLabelledBy: 'modal-basic-title'});
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  goToOrders() {
    this.router.navigate(['orders']);
  }
  goToAddOrders() {
    this.modalReference.close()
    this.router.navigate(['add-order']);
  }
}

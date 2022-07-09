import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {BooksComponent} from "./books/books.component";
import {OrdersComponent} from "./orders/orders.component";
import {AddOrderComponent} from "./add-order/add-order.component";
import {AuthGuard} from "./util/AuthGuard";

const routes: Routes = [
  { path: '', component: LoginComponent},
  { path: 'register', component: RegisterComponent},
  { path: 'books', component: BooksComponent, canActivate: [AuthGuard]},
  {path: 'orders', component:OrdersComponent, canActivate: [AuthGuard]},
  {path: 'add-order', component:AddOrderComponent, canActivate: [AuthGuard]},

  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

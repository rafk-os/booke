export class Book {
  id: number
  title: string
  author: string
  description: string
  price: number
  addedToCart: boolean
  constructor(id :number, title:string,author:string,description:string, price:number, added : boolean) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.description = description;
    this.price = price;
    this.addedToCart = added;
  }
}

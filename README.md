# Aplikacja booke
Jest to aplikacja  zaliczeniowa na przedmiot systemy klasy enterprise - persystencje. Zrobiona jest zgodnie ze wszystikimi wymaganiami na ocęnę 5 czyli posiada wykorzystanie jpa wraz z springboot, maven i spring security. W aplikacji jest zrobiony podział na dwie role : admina oraz usera. User moze przeglądać ksiażki dodawać i usuwać je do koszyka jak i do tworzenia zamówień na podstawie koszyka. Admin moze dodawać i usuwać ksiażki z całego zbioru aplikacji jak i zmieniać status zamówienia książki. W aplikacji został też wykozystany system płatności Stripe do zapłaty za książki np. przy pomocy karty płatniczej. Do procesu autoryzacji wykorzystywany jest token JWT który jest wydawany użytkownikowi przy zalogowaniu na aktualne konto. Do prezentacji REST Api została stworzona aplikacja webowa przy użyciu framework'u Angular.
## Backend
Aplikacja wykorzystuje baze typu MySql na lokalnym porcie nr 3306 o nazwie "bookstore" oraz działa przy Javie w wersji 17. Nazwa użytkownika to root a hasło admin. Trzeba posiadać również odpowiedni sterownik do tej bazy. Wszystko jest ustawione w pliku application.properties i można to zmienic według własnych preferencji. Aby uruchomić nasze REST Api potrzeba uruchomić klase BackendApplication (która znajduje się w folderze backend i dalej w src/main/java) i w tym momencie jpa stworzy dla nas schemat bazy danych na podstawie klas encyjnych. Zaraz po tym zostaną dodane testowe dane z skryptu o nazwie import.sql który znajduje się w folderze resources. Aplikacja uruchomi się na lokalnym adresie i porcie 8080. Endpointy poza logowaniem i rejestracją są tylko wykonywane jezeli z nagłówkach znajduje się JWT token oraz rola przypisana do tego tokenu pozwala na to.
Po uruchomieniu można przetestować wszystkie endpointy za pomocą swaggera, wystarczy wejść pod adres:
- localhost:8080/swagger-ui/
### Dodatkowe endpointy
Poza wymaganymi endpointami do oceny na 5 są dodatkowie takie jak:
- Endpointy związane z wyświetlaniem,usuwaniem,dodawaniem,modyfikacją użytkowników czy tez wyswietleniem roli użytkownika, jego koszyka zakupów, logowanie użytkownika i rejestracja nowego.
- Do książek oraz zamówień są to takie dodatkowe endpointy jak wyświetlanie list wszystkich książek lub zamówień dla admina.
### Schema Bazy danych
![obraz](https://user-images.githubusercontent.com/61945072/178116528-f26331d3-44b1-48da-b092-50a404a6d899.png)

## Frontend
Do tej aplikacji wymagane jest posiadanie npm oraz nodejs wraz z angular/cli. Po tym wystarczy wejść do katalogu frontend oraz uruchomić aplikację na lokalnym adresie i porcie 4200 wywołując te polecenie:
- ng serve
### Działanie
Całość zostało wykonane w opraciu o Angular oraz bootstrap. Aplikacja po zalogowaniu umieszcza dane odnośni użytkownika w local storage oraz na podstawie tego jest potem do kazdego requesta dokejany nagłówek z JWT tokenem. Poza tym prezentuje ona wszystkie funkcjonalności zgodne z wymaganiami do oceny 5. Integracja z systemem płatności odbywa się za pośrednictem Stripe dzięki któremu mozna zobaczyc wszystkie transackcje w formie testowej na ich stronie.

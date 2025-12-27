# Traceability – wymagania → klasy/metody

FR-01 Rejestracja: `UżytkownikSystemu.zarejestrujSię()`  
FR-02 Logowanie/wylogowanie: `UżytkownikSystemu.zalogujSię()`, `UżytkownikSystemu.wylogujSię()`  
FR-03 Dane użytkownika: `UżytkownikSystemu` (+ `Klient.daneDoFaktury`)  
FR-04 Wyszukiwanie lotów: `Klient.wyszukajLoty()` + `Lot.sprawdźDostępność()`  
FR-05 Lista lotów: `Klient.wyszukajLoty()`  
FR-06 Szczegóły lotu: `Klient.wyświetlSzczegółyLotu()` + `Lot.obliczCenę()`  
FR-07 Utworzenie rezerwacji: `Klient.utwórzRezerwację()` + `Rezerwacja.ustawStatus()`  
FR-08 Dane pasażerów: `Rezerwacja.dodajPasażera()` + `Pasażer.zaktualizujDane()`  
FR-09 Płatność: `Płatność.rozpocznijPłatność()` + `oznaczJakoPotwierdzona()/oznaczJakoOdrzucona()`  
FR-10 Potwierdzenie rezerwacji: `Rezerwacja.ustawStatus("PAID")`  
FR-11 Lista rezerwacji: `Klient.wyświetlRezerwacje()`  
FR-12 Anulowanie: `Rezerwacja.anuluj()` / `Klient.anulujRezerwację()`  
FR-13 Zmiana rezerwacji: `Klient.zmieńRezerwację()` + `Pasażer.zaktualizujDane()`  
FR-14 Admin loty: `Administrator.dodajLot()/edytujLot()/usuńLot()`  
FR-15 Admin rezerwacje/raporty: `Administrator.przeglądajRezerwacje()`

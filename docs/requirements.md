# Wymagania systemu – System wyszukiwania i zakupu biletów lotniczych

Data: 2025-12-26

## 1. Cel systemu
System ma umożliwić użytkownikom (klientom) wyszukiwanie lotów, przeglądanie szczegółów, tworzenie rezerwacji dla pasażerów oraz wykonanie płatności online (na potrzeby MVP dopuszczalna płatność „mock”).
Administrator zarządza ofertą lotów oraz przegląda rezerwacje.

## 2. Zakres MVP (implementacja częściowa na 1 tydzień)
**MVP (must-have):**
1. Wyszukiwanie lotów i lista wyników.
2. Szczegóły lotu.
3. Utworzenie rezerwacji dla wybranego lotu (wraz z pasażerami).
4. Płatność jako **mock** (SUCCESS/FAIL) i aktualizacja statusu rezerwacji.
5. „Moje rezerwacje” (lista).

**Poza MVP / później:**
- Zmiana rezerwacji (pełna).
- Dodatki: bagaż dodatkowy, ubezpieczenie podróżne.
- Panel admina (jeśli zabraknie czasu — ograniczyć do CRUD lotów).
- Integracja z zewnętrznym dostawcą danych lotów (jeśli występuje — na razie mock/seed).

## 3. Aktorzy
1. **Klient** – wyszukuje i kupuje bilety.
2. **Administrator** – zarządza lotami, użytkownikami i przegląda rezerwacje.
3. **System płatności (zewnętrzny)** – przetwarza płatność (dla MVP może być mock).
4. **Dostawca danych lotów / system linii lotniczych (zewnętrzny, opcjonalnie)** – udostępnia dane lotów (dla MVP możliwy seed danych w bazie).

## 4. Wymagania funkcjonalne (FR)
FR-01. Rejestracja nowego użytkownika.  
FR-02. Logowanie i wylogowanie użytkownika.  
FR-03. Przechowywanie danych użytkownika (imię, nazwisko, email, telefon, dane do faktury).  
FR-04. Wyszukiwanie lotów (wylot, przylot, data, liczba pasażerów, klasa podróży).  
FR-05. Wyświetlenie listy dostępnych lotów.  
FR-06. Wyświetlenie szczegółów wybranego lotu.  
FR-07. Utworzenie rezerwacji dla wybranego lotu.  
FR-08. Wprowadzenie danych pasażerów (imię, nazwisko, data urodzenia, dokument).  
FR-09. Przekazanie danych do systemu płatności i obsługa płatności online (MVP: mock).  
FR-10. Zapis potwierdzonej rezerwacji i przypisanie jej do konta użytkownika.  
FR-11. Wyświetlenie listy rezerwacji użytkownika.  
FR-12. Anulowanie rezerwacji (gdy dozwolone).  
FR-13. Zmiana rezerwacji (np. dane pasażera / termin – jeśli dozwolone).  
FR-14. Administrator: dodawanie/edycja/usuwanie lotów.  
FR-15. Administrator: przegląd rezerwacji i proste raporty.

## 5. Wymagania niefunkcjonalne (NFR)
NFR-01. System jako aplikacja webowa (frontend React, backend Spring Boot).  
NFR-02. Czas odpowiedzi wyszukiwania lotów: „kilka sekund”.  
NFR-03. Obsługa wielu użytkowników jednocześnie.  
NFR-04. Bezpieczeństwo danych (RODO): hasła haszowane, komunikacja HTTPS (dla środowisk docelowych).  
NFR-05. UI responsywne (desktop + mobile).

## 6. Przypadki użycia (lista)
### Klient
UC-01 Zarejestruj konto  
UC-02 Zaloguj się / Wyloguj się  
UC-03 Wyszukaj loty  
UC-04 Wyświetl wyniki wyszukiwania  
UC-05 Wyświetl szczegóły lotu  
UC-06 Zarezerwuj lot  
UC-07 Zapłać za rezerwację  
UC-08 Wyświetl swoje rezerwacje  
UC-09 Anuluj rezerwację  
UC-10 Zmień rezerwację  

### Administrator
UC-11 Zaloguj się jako administrator  
UC-12 Zarządzaj lotami (dodaj/edytuj/usuń)  
UC-13 Przeglądaj rezerwacje  
UC-14 Zarządzaj użytkownikami  

## 7. Relacje «include» i «extend»
**«include»:**
- UC-06 Zarezerwuj lot «include» UC-06a Wprowadź dane pasażerów  
- UC-06 Zarezerwuj lot «include» UC-07 Zapłać za rezerwację  
- UC-02 Zaloguj się «include» UC-02a Zweryfikuj dane logowania  

**«extend»:**
- UC-06 Zarezerwuj lot «extend» UC-06b Dodaj bagaż dodatkowy  
- UC-06 Zarezerwuj lot «extend» UC-06c Dodaj ubezpieczenie podróżne  
- UC-08 Wyświetl szczegóły rezerwacji «extend» UC-09 Anuluj rezerwację  
- UC-08 Wyświetl szczegóły rezerwacji «extend» UC-10 Zmień rezerwację

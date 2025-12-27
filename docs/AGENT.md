# Instrukcje dla AntiGravITy (Agent)

## Źródła prawdy
- `docs/requirements.md`
- `docs/api.md`
- `docs/db.md`
- `docs/traceability.md`
- `docs/diagrams/*`

## Reguły
1) Stack: Spring Boot + React.  
2) Implementuj funkcje zgodnie z `docs/api.md`.  
3) Model domeny zgodny z diagramem klas.  
4) MVP dopuszcza płatność mock (`/api/payments/mock`).  
5) Każda funkcja backendu: Controller → Service → Repository + DTO.  
6) Waliduj dane wejściowe (Bean Validation).  
7) Po zmianach wypisz listę plików, które zmieniłeś lub dodałeś.

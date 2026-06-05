# Personal Budget API

Proste REST API do zarządzania budżetem osobistym, stworzone w ramach zadania rekrutacyjnego. Aplikacja umożliwia śledzenie przychodów i wydatków przypisanych do kont, obsługę transakcji oraz generowanie podsumowań finansowych.

## Wykorzystane technologie
* **Język:** Java 21
* **Framework:** Spring Boot
* **Baza danych:** PostgreSQL 18.1
* **Dokumentacja API:** Swagger
* **Konteneryzacja:** Docker & Docker Compose
* **Testy:** JUnit 5 & Mockito

## Uruchomienie aplikacji (Docker)
### Wymagania wstępne
Upewnij się, że masz zainstalowane:
* [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Kolejne kroki
1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/PawelJasinski25/personal-budget-api.git
   cd personal-budget-api

2. Uruchom aplikację razem z bazą danych:
   ```bash
   docker compose up -d --build

## Uruchomienie aplikacji (bez Dockera)
### Wymagania wstępne
* Posiadanie lokalnie zainstalowanej bazy **PostgreSQL**.
* Skonfigurowanie poniższych zmiennych środowiskowych przed uruchomieniem aplikacji:
  * `DB_HOST` (np. `localhost`)
  * `DB_PORT` (np. `5432`)
  * `DB_NAME` (nazwa Twojej bazy, np. `budget_db`)
  * `DB_USERNAME` (nazwa użytkowinka)
  * `DB_PASSWORD` (hasło do bazy danych)

### Kolejne kroki
1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/PawelJasinski25/personal-budget-api.git
   cd personal-budget-api
   
2. Uruchom aplikację:
   ```bash
   ./mvnw spring-boot:run
   
## Testy jednostkowe 
1. Uruchom testy:
   ```bash
   cd personal-budget-api
   ./mvnw.cmd test
   
## Dokumentacja
Dokumentację API znajdziesz pod adresem:
   http://localhost:8080/swagger-ui.html  
   W tym samym miejscu można również przetestować wszystkie endpointy bezpośrednio z przeglądarki.

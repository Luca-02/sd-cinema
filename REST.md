# Progetto Sistemi Distribuiti 2022-2023 - API REST

Documentazione delle API REST. Si assume che i dati vengano scambiati in formato JSON.

## `/api/film`

* ### GET

    - **Descrizione**: Restituisce l'elenco di tutti i film.
    
    - **Parametri**: None
    
    - **Body richiesta**: None
    
    - **Risposta**: Viene restituita una lista JSON di tutti i film, nel formato

          [
            {
              "id": [int],
              "film": [string],
              "durataMinuti": [int]
            },
            ...
          ]

  - **Codici di stato restituiti**:
      * `200 OK`

* ### POST

  - **Descrizione**: aggiunge un film alla base di dati.
  
  - **Parametri**: ci deve essere l'header `Content-Type: application/json`.
  
  - **Body richiesta**: rappresentazione in formato JSON del film del tipo

        {
          "film": [string],
          "durataMinuti": [int]
        }

    il parametro `id` sarà generato automaticamente dal server che lo renderà unico.
    
  - **Risposta**: in caso di successo il body è vuoto e la risorsa creata è indicata nell'header `Location`.
  
  - **Codici di stato restituiti**:
    * `201 Created`
    * `400 Bad Request`: c'è un errore del client, il formato JSON errato, c'è un campo errato o mancante.

## `/api/film/{id}`

* ### GET

  - **Descrizione**: restituisce il film con l'`id` fornito.
  
  - **Parametri**: un parametro nel percorso `id` che rappresenta l'identificativo del film da restituire.
  
  - **Body richiesta**: None
  
  - **Risposta**: In caso di successo viene restituita la rappresentazione in JSON del film, nel formato
  
        {
          "id": [int],
          "film": [string],
          "durataMinuti": [int]
        }

  - **Codici di stato restituiti**:
    * `200 OK`
    * `404 Not Found`: film non trovato.

## `/api/sala`

* ### GET

    - **Descrizione**: Restituisce l'elenco di tutte le sale.

    - **Parametri**: None

    - **Body richiesta**: None

    - **Risposta**: Viene restituita una lista JSON di tute le sale, nel formato

          [
              {
                  "id": [int],
                  "nome": [string],
                  "rows": [int],
                  "columns": [int]
              },
            ...
          ]

    - **Codici di stato restituiti**:
        * `200 OK`

* ### POST

    - **Descrizione**: aggiunge una sala alla base di dati.

    - **Parametri**: ci deve essere l'header `Content-Type: application/json`.

    - **Body richiesta**: rappresentazione in formato JSON della sala del tipo

          {
              "nome": [string],
              "rows": [int],
              "columns": [int]
          }

      il parametro `id` sarà generato automaticamente dal server che lo renderà unico.

    - **Risposta**: in caso di successo il body è vuoto e la risorsa creata è indicata nell'header `Location`.

    - **Codici di stato restituiti**:
        * `201 Created`
        * `400 Bad Request`: c'è un errore del client, il formato JSON errato, c'è un campo errato o mancante.

## `/api/sala/{id}`

* ### GET

    - **Descrizione**: restituisce la sala con l'`id` fornito.

    - **Parametri**: un parametro nel percorso `id` che rappresenta l'identificativo della sala da restituire.

    - **Body richiesta**: None

  - **Risposta**: In caso di successo viene restituita la rappresentazione in JSON della sala, nel formato

        {
            "id": [int],
            "nome": [string],
            "rows": [int],
            "columns": [int]
        }

  - **Codici di stato restituiti**:
      * `200 OK`
      * `404 Not Found`: sala non trovata.

## `/api/proiezione`

* ### GET

    - **Descrizione**: Restituisce l'elenco di tutte le proiezioni.

    - **Parametri**: None

    - **Body richiesta**: None

    - **Risposta**: Viene restituita una lista JSON di tute le proiezioni, nel formato

          [
              {
                  "id": [int],
                  "idFilm": [int],
                  "idSala": [int],
                  "data": [string],
                  "orario": [string]
              },
            ...
          ]

    - **Codici di stato restituiti**:
        * `200 OK`

* ### POST

    - **Descrizione**: aggiunge una proiezione alla base di dati.

    - **Parametri**: ci deve essere l'header `Content-Type: application/json`.

  - **Body richiesta**: rappresentazione in formato JSON della proiezione del tipo

        {
            "idFilm": [int],
            "idSala": [int],
            "data": [string],
            "orario": [string]
        }

    il parametro `id` sarà generato automaticamente dal server che lo renderà unico.

  - **Risposta**: in caso di successo il body è vuoto e la risorsa creata è indicata nell'header `Location`.

  - **Codici di stato restituiti**:
      * `201 Created`
      * `400 Bad Request`: c'è un errore del client, il formato JSON errato, c'è un campo errato o mancante.

## `/api/proiezione/{id}`

* ### GET

    - **Descrizione**: restituisce la proiezione con l'`id` fornito.

    - **Parametri**: un parametro nel percorso `id` che rappresenta l'identificativo della proiezione da restituire.

    - **Body richiesta**: None

    - **Risposta**: In caso di successo viene restituita la rappresentazione in JSON della proiezione, nel formato

          {
              "id": [int],
              "idFilm": [int],
              "idSala": [int],
              "data": [string],
              "orario": [string]
          }

    - **Codici di stato restituiti**:
        * `200 OK`
        * `404 Not Found`: proiezione non trovata.

## `/api/prenotazione?idProiezione={id}`

* ### GET

    - **Descrizione**: Restituisce l'elenco di tutte le prenotazioni.

    - **Parametri**: se viene specificato nella query della richiesta il valore di `idProiezione` dato da `{id}` 
  che rappresenta un identificativo della proieizone, allora verrà restituito l'elenco di tutte le prenotazioni 
  che hanno `idProiezione = {id}`, altrimenti se la query non è specificata verrà restituito l'elenco di tutte le 
  prenotazioni.

    - **Body richiesta**: None

    - **Risposta**: Viene restituita una lista JSON di tute le prenotazioni, nel formato

          [
              {
                  "id": [int],
                  "idProiezione": [int],
                  "data": [string],
                  "orario": [string],
                  "posti": [
                      {
                          "id": [int],
                          "row": [int],
                          "column": [int]
                      },
                      ...
                  ]
              },
            ...
          ]

    - **Codici di stato restituiti**:
        * `200 OK`

* ### POST

    - **Descrizione**: aggiunge una proiezione (e i posti annessi) alla base di dati.

    - **Parametri**: ci deve essere l'header `Content-Type: application/json`.

    - **Body richiesta**: rappresentazione in formato JSON della proiezione del tipo

          {
              "idProiezione": [int],
              "posti": [
                  {
                      "row": [int],
                      "column": [int]
                  },
                  ...
              ]
          }

        il parametro `id` delle prenotazioni e dei posti sarà generato automaticamente dal server che lo renderà unico.
    I parametri `data` e `orario` saranno inizializzati automaticamente alla data e ora attuali nel momento della 
    richiesta 

    - **Risposta**: in caso di successo il body è vuoto e la risorsa creata è indicata nell'header `Location`.

    - **Codici di stato restituiti**:
        * `201 Created`
        * `400 Bad Request`: c'è un errore del client, il formato JSON errato, c'è un campo errato o mancante oppure 
      i posti specificati nella prenotazione non sono validi o sono già prenotati.
        * `404 Not Found`: `idProiezione` specificato nella richiesta non è correlato a nessuna proiezione esistente.

## `/api/prenotazione/{id}`

* ### GET

    - **Descrizione**: restituisce la prenotazione con l'`id` fornito.

    - **Parametri**: un parametro nel percorso `id` che rappresenta l'identificativo della prenotazione da restituire.

    - **Body richiesta**: None

    - **Risposta**: In caso di successo viene restituita la rappresentazione in JSON della prenotazione, nel formato

          {
              "id": [int],
              "idProiezione": [int],
              "data": [string],
              "orario": [string],
              "posti": [
                  {
                      "id": [int],
                      "row": [int],
                      "column": [int]
                  },
                  ...
              ]
          }

      - **Codici di stato restituiti**:
          * `200 OK`
          * `404 Not Found`: prenotazione non trovata.

## `/api/prenotazione/{id}/posto`

## `/api/prenotazione/{id1}/posto/{id2}`
database dictionary:
-

    "film:999": "{\"id\": 999, \"film\": \"film1\", \"durataMinuti\": 999}",
    ...
    "sala:999": "{\"id\": 999, \"nome\": \"sala1\", \"row\": 999, \"columns\": 999}",
    ...
    "proiezione:999": "{"id": 999, "idFilm": 999, "idSala": 999, "data": "0000-00-00", "orario": "00:00"}",
    ...
    "proiezione:999:prenotazione:999": "{"id": 999, "data": "0000-00-00", "orario": "00:00"}",
    ...
    "proiezione:999:prenotazione:999:posto:999": "{"id": 999, "idPrenotazione": 999, "row": 999, "column": 999}",
    ...

NOTA: le chiavi del dizionario non devono contenere spazi per il parsing del comando nel database

ispirato al protocollo di Redis:
- MSGETALL subKey: ritorna tutti valori dove la chiave di essi ha come sottostringa subKey, il formato dei dati ritornati sarà una stringa con la seguente sintassi: 

  - se non ha trovato valori:
        
        (empty)
      
  - altrimenti
  
        [record]/r/n!#!/r/n[record]/r/n!#!/r/n[record]/r/n!#!/r/n[record]/r/n!#!/r/n[record]/r/n!#!/r/n...
  
      dove `[record]` sarà il valore di un record trovato e `/r/n!#!/r/n` il delimitatore dei valori, su questo risultato si può effettuare parsing molto semplice
      usando il metodo `split([delimiter]: String)` di java (dove in questo caso `[delimiter]` sarà `"/r/n!#!/r/n"`) che ritornerà un array di Stringhe contenente 
      tutti i valori ritornati dal server.
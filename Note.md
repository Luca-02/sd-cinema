database dictionary:
-

    "film:999": "\"{'id': 999, 'nome': 'film1', 'durataMinuti': 999}\"",
    "film:999": "\"{'id': 999, 'nome': 'film1', 'durataMinuti': 999}\"",
    "film:999": "\"{'id': 999, 'nome': 'film1', 'durataMinuti': 999}\"",
    ...
    "sala:999": "\"{'id': 999, 'nome': 'sala1', 'row': 999, 'columns': 999}\"",
    "sala:999": "\"{'id': 999, 'nome': 'sala1', 'row': 999, 'columns': 999}\"",
    "sala:999": "\"{'id': 999, 'nome': 'sala1', 'row': 999, 'columns': 999}\"",
    ...
    "proiezione:999": "\"{"id": 999, "idFilm": 999, "idSala": 999, "data": "0000-00-00", "orario": "00:00"}\"",
    "proiezione:999": "\"{"id": 999, "idFilm": 999, "idSala": 999, "data": "0000-00-00", "orario": "00:00"}\"",
    "proiezione:999": "\"{"id": 999, "idFilm": 999, "idSala": 999, "data": "0000-00-00", "orario": "00:00"}\"",
    ...
    "proiezione:999:prenotazione:999": "\"{"id": 999, "data": "0000-00-00", "orario": "00:00", "posti": [[0, 0], [0, 1], [1, 1] ...]}\"",
    "proiezione:999:prenotazione:999": "\"{"id": 999, "data": "0000-00-00", "orario": "00:00", "posti": [[0, 0], [0, 1], [1, 1] ...]}\"",
    "proiezione:999:prenotazione:999": "\"{"id": 999, "data": "0000-00-00", "orario": "00:00", "posti": [[0, 0], [0, 1], [1, 1] ...]}\"",
    ...
    "proiezione:999:prenotazione:999:posto:999": "\"[999, 999]]\"",
    "proiezione:999:prenotazione:999:posto:999": "\"[999, 999]]\"",
    "proiezione:999:prenotazione:999:posto:999": "\"[999, 999]]\"",

ispirato al protocollo di Redis:
- HSUBGETALL subKey: ritorna tutti valori dove la chiave (dei valori) ha come sottostringa subKey
nel formato "[value]"
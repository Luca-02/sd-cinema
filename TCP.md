# Progetto Sistemi Distribuiti 2022-2023 - TCP

Documentazione del protoccolo socket su TCP.

## Introduzione

Il protocollo costruito per la comunicazione tra server-web e database prende ispirazione dal
protocollo di [Redis](https://redis.io/docs/reference/protocol-spec/) in particolar modo alla gestione
di una tabella di [hash](https://redis.io/docs/data-types/hashes/).

- È basato su una richiesta da parte di un client posto nel server-web, e una risposta da parte di un server
posto nel database;

## Gestione dei dati

I dati salvati sul server sono getsiti attraverso una tabella di hash composta da coppie **chiave**-**valore**:
- **chiave**: sono stringhe nel formato `"context:id"`, dove con `context` si indica il contesto in cui il valore fa riferimento e 
`id` invece per indicare l'identificatore univoco del valore;
- **valore**: sono stringhe che rappresentano un qualsiasi oggetto JSON relativo a una specifica **key**;

**Nota**: i valori **chiave** non devono contenere caratteri di spazio al fine delle funzionalità del protocollo.

## Client (server-web)

Un client posto nel server-web aprirà una **socket** sull'**host** `localhost` e sulla **porta** `3030` per comunicare con il server posto 
sul database sul rispettivo host e porta.

- Il client manderà al server una semplice stringa che rappresenterà il comando da eseguire nel server sul database.
- come risposta, dal server, riceverà una stringa contenente la risposta alla richiesta precedente.

## Server (database)

La **socket** del server posto nel database rimarrà in ascolto sull'**host** `localhost` e sulla **porta** 3030 per soddisfare le richieste
che richiedono i client.

- La risposta del server è una stringa che varierà in base al comando da eseguire richiesto dal client.

### Comandi

## `MGET`

- **Sintassi**:

        MGET [key]

- **Descrizione**: ritorna la coppia chiave-valore che ha come chiave `[key]` presente nella tabella di hash..

- **Risposta**:
    - se non ha trovato nessun valore associato a `[key]`:

          (empty)

    - altrimenti

          [key]!#![value]

      una coppia chiave-valore `[key]-[value]` unita da aggregatore `!#!`.




## `MSGETALL`

- **Sintassi**:

        MSGETALL [subKey]

- **Descrizione**: ritorna tutte le coppie chiave-valore dove la chiave contiene la sottostringa `[subKey]` presenti nella tabella 
di hash.

- **Risposta**:
    - se non ha trovato valori associati a chiavi che hanno come sottostringa `[subKey]`:

          (empty)

  - altrimenti

        [key]!#![value]/r/n!#!/r/n[key]!#![value]/r/n!#!/r/n[key]!#![value]/r/n!#!/r/n...

      una lista di coppie chiave-valore `[key]-[value]` unite da aggregatore `!#!`, dove ogni coppia sarà divisa da un delimitatore
    `/r/n!#!/r/n`.




## `MSET`

- **Sintassi**:

        MSET [key] [value]

- **Descrizione**: imposta nella tabella di hash la chiave `[key]` associandola al valore `[value]`

- **Risposta**:
    - ritorna un semplice messaggio dopo l'esecuzione:

          OK

## `MDEL`

- **Sintassi**:

        MDEL [key]

- **Descrizione**: rimuove dalla tabella di hash la coppia chiave-valore che ha come chiave `[key]`

- **Risposta**:
    - se la coppia chiave-valore che ha come chiave `[key]` esiste e viene eliminata:

          (true)

  - altrimenti:

        (false)

## `MSDEL`

- **Sintassi**:

        MSDEL [subKey]

- **Descrizione**: rimuove dalla tabella di hash tutte le coppie chiave-valore dove la chiave contiene la sottostringa `[subKey]`

- **Risposta**:
    - se esiste e viene eliminata almeno una coppia chiave-valore dove la chiave contiene la sottostringa `[subKey]`:

          (true)

    - altrimenti:

          (false)

## `MEXISTS`

- **Sintassi**:

        MEXISTS [key]

- **Descrizione**: ritorna se la tabella di hash contiene una coppia chiave valore che ha come chiave `[key]`

- **Risposta**:
    - se la tabella di hash contiene una coppia chiave valore che ha come chiave `[key]`:

          (true)

    - altrimenti:

          (false)

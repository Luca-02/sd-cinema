# Progetto Sistemi Distribuiti 2022-2023

Il progetto d’esame del corso di “Sistemi Distribuiti” dell’anno 2022-2023 consiste nella progettazione e sviluppo di un’applicazione distribuita per la 
prenotazione di posti di proiezioni cinematografiche in un cinema.

## Componenti del gruppo

* Luca Milanesi (886279) <l.milanesi7@campus.unimib.it>
* Luca Garavaglia (885917) <l.garavaglia13@campus.unimib.it>

## Compilazione ed esecuzione

Il server Web e il database sono dei progetti Java che utilizano Maven per gestire le dipendenze, la compilazione e l'esecuzione. È necessario eseguire, per ogni progetto maven, i seguenti obiettivi 
per compilare ed eseguire: `clean`, che rimuove la cartella `target`, `compile` per compilare e `exec:java` per avviare il componente.

I tre obiettivi possono essere eseguiti insieme in una sola riga di comando da terminale tramite `./mvnw clean compile exec:java` per Linux/Mac e `mvnw.cmd clean compile exec:java` per Windows. 
L'unico requisito è un'istallazione di Java (versione da noi utilizzata: `17.0`), verificando che la variabile `JAVA_PATH` sia correttamente configurata. Il nostro gruppo come sistema operativo principale
ha utilizzato Windows.

Il client Web è invece un solo file HTML chiamato `index.html`, può essere aperto su un qualsiasi browser. È importante disabilitare CORS, come mostrato nel laboratorio 8 su JavaScript (AJAX).

## Porte e indirizzi

Il server Web si pone in ascolto all'indirizzo `localhost` alla porta `8080`. Il database si pone in ascolto allo stesso indirizzo del server Web ma alla porta `3030`.

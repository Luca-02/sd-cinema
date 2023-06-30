package it.unimib.finalproject.server.handler;

import it.unimib.finalproject.server.entities.Posto;
import it.unimib.finalproject.server.entities.Prenotazione;
import it.unimib.finalproject.server.entities.Sala;

import java.util.List;
import java.util.Objects;

public class HandlerPrenotazione {

    private final Sala sala;
    private final List<Prenotazione> prenotazioneList;

    public HandlerPrenotazione(Sala sala, List<Prenotazione> prenotazioneList) {
        this.sala = sala;
        this.prenotazioneList = prenotazioneList;
    }

    public boolean available(Prenotazione prenotazione) {
        if (prenotazione.getPosti() != null) {
            for (Posto posto : prenotazione.getPosti()) {
                if (!posto.notNullAttributes() || !permitted(posto, prenotazione)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean permitted(Posto posto, Prenotazione prenotazione) {
        return exist(posto) && !alreadyBooked(posto, prenotazione);
    }

    public boolean exist(Posto posto) {
        return posto.getRow() < sala.getRows() && posto.getColumn() < sala.getColumns();
    }

    public boolean alreadyBooked(Posto posto, Prenotazione prenotazione) {
        for (Prenotazione pre : prenotazioneList) {
            if (Objects.equals(prenotazione.getIdProiezione(), pre.getIdProiezione())) {
                for (Posto postoPre : pre.getPosti()) {
                    if (posto.equals(postoPre))
                        return true;
                }
            }
        }
        return false;
    }


}

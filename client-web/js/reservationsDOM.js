const SEATS_TABLE_ID = "seleziona-posti";
const SEATS_INPUT_ID = "posti";
const RESERVATION_FORM_ID = "form-prenotazione";

function extractReservationObj() {
    const seatsList = extractSeatsList();

    let reservationObj = {
        "posti": seatsList
    }

    return reservationObj;
}

function extractSeatsList() {
    const selectedSeatsInpt = document.getElementById("posti");
    const seats = selectedSeatsInpt.value.split(';');
    seats.pop() //tolgo ultimo valore che è stringa vuota
    const seatsList = [];

    for (let i = 0; i < seats.length; i++) {
        let seatRowCol = seats[i].split('-');
        seatsList[i] = { "row": seatRowCol[0], "column": seatRowCol[1] };
    }

    return seatsList;
}

function showReservationPanel(reservationsObj, room, projId) {
    const seatsMatrix = createSeatsMatrix(reservationsObj, room.rows,
        room.columns);

    cleanReservationPanel();
    fillSeatsTable(SEATS_TABLE_ID, seatsMatrix, newSeatCheckbox);
    setInputValue("id-proiezione", projId);
    changePanel("reservations", PANEL_IDS);
}

function cleanReservationPanel() {
    clearTableById(SEATS_TABLE_ID);
    cleanInptValue(SEATS_INPUT_ID); //pulisco posti selezionati
    cleanInptValue("id-proiezione"); //pulisco id proiezione
}

function newSeatCheckbox(row, col, seatObj) {
    let checkbox = newSeatCheckboxInpt(row, col);

    if (seatObj.status == 1) {
        checkbox.checked = true;
        checkbox.disabled = true;
    }

    checkbox.addEventListener('change', function () {
        if (this.checked) {
            appendToInputValue(this.value + ';', SEATS_INPUT_ID);
        }
        else {
            removeSeatFromInput(this.value, SEATS_INPUT_ID);
        }
    });
    return checkbox;
}

function setReservationPanelEvents(newReservationCb) {
    const reservationForm = document.getElementById(RESERVATION_FORM_ID);
    reservationForm.addEventListener("submit", newReservationCb);
}

function getProjIdForNewReservation(){
    return document.getElementById("id-proiezione").value;
}
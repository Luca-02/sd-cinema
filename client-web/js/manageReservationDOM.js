function newManageSeatCheckbox(row, col, seatObj) {
    let checkbox = newSeatCheckboxInpt(row, col);

    if (seatObj.status == 1) {
        checkbox.checked = true;
        checkbox.disabled = false;
    }
    else {
        checkbox.checked = false;
        checkbox.disabled = true;
    }

    checkbox.addEventListener('change', function () {
        if (this.checked) {
            removeSeatFromInput(this.value, "posti-da-cancellare");
            removeSeatFromInput(seatObj.id, "id-posti-da-cancellare");
        }
        else {
            appendToInputValue(this.value + ';', "posti-da-cancellare");
            appendToInputValue(seatObj.id + ';', "id-posti-da-cancellare");
        }
    });
    return checkbox;
}

function showManageReservation() {
    cleanManageReservationPanel();

    document.getElementById("div-modifica-prenotazione")
        .style.display = "none";

    changePanel("manage-reservation", PANEL_IDS);
}

function showEditReservationSubPanel(reservation, room, projId) {
    cleanManageReservationSubPanel();

    const seatsMatrix = createSeatsMatrix([reservation], room.row,
        room.columns);

    fillSeatsTable("tabella-modifica-prenotazione", seatsMatrix,
        newManageSeatCheckbox);

    setInputValue("id-proiezione-mod", projId);
    setInputValue("id-prenotazione-mod", reservation.id);

    document.getElementById("div-modifica-prenotazione")
        .style.display = "block";
}

function cleanManageReservationPanel() {
    cleanInptValue("id-prenotazione");
}

function cleanManageReservationSubPanel() {
    clearTableById("tabella-modifica-prenotazione");
    cleanInptValue("posti-da-cancellare");
    cleanInptValue("id-posti-da-cancellare");
    cleanInptValue("id-proiezione-mod");
    cleanInptValue("id-prenotazione-mod");
}

function setManageReservationPanelEvents(searchCb, deleteSeatsCb, deleteCb) {
    const searchForm = document.getElementById("form-cerca-prenotazione");
    searchForm.addEventListener("submit", searchCb);

    const deleteSeatsForm = document.getElementById("modifica-prenotazione");
    deleteSeatsForm.addEventListener("submit", deleteSeatsCb);

    const deleteForm = document.getElementById("cancella-prenotazione");
    deleteForm.addEventListener("submit", deleteCb);
}

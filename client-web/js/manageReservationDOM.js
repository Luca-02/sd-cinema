const PROJ_MANAGE_INPT_ID = "id-proiezione-mod";
const RESERVATION_MANAGE_INPT_ID = "id-prenotazione-mod";
const SEATSID_TO_DELETE_INPT_ID = "id-posti-da-cancellare";
const SEATS_TO_DELETE_INPT_ID = "posti-da-cancellare";
const TXT_RESERVATION_INFO_ID = "info-modifica-prenotazione";

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
            removeSeatFromInput(this.value, SEATS_TO_DELETE_INPT_ID);
            removeSeatFromInput(seatObj.id, SEATSID_TO_DELETE_INPT_ID);
        }
        else {
            appendToInputValue(this.value + ';', SEATS_TO_DELETE_INPT_ID);
            appendToInputValue(seatObj.id + ';', SEATSID_TO_DELETE_INPT_ID);
        }
    });
    return checkbox;
}

function getSeatsIdToDelete() {
    let seats = document.getElementById(SEATSID_TO_DELETE_INPT_ID).value;
    const seatsToDelete = seats.split(';');
    seatsToDelete.pop();

    return seatsToDelete;
}

function getReservationIdToModify() {
    return document.getElementById(RESERVATION_MANAGE_INPT_ID).value;
}

function getMREditProjId() {
    return document.getElementById(PROJ_MANAGE_INPT_ID).value;
}

function showManageReservation() {
    cleanManageReservationPanel();

    document.getElementById("div-modifica-prenotazione")
        .style.display = "none";

    changePanel("manage-reservation", PANEL_IDS);
}

function showEditReservationSubPanel(reservation, room, projObj, movieObj) {
    let projId = projObj["id"];

    let reservationInfo = movieObj["film"] + "\n" +
        projObj["data"] + " - " + projObj["orario"] + "\n" +
        "Sala: " + room["id"];

    cleanManageReservationSubPanel();

    const seatsMatrix = createSeatsMatrix([reservation], room.rows,
        room.columns);

    fillSeatsTable("tabella-modifica-prenotazione", seatsMatrix,
        newManageSeatCheckbox);

    setInputValue(PROJ_MANAGE_INPT_ID, projId);
    setInputValue(RESERVATION_MANAGE_INPT_ID, reservation.id);

    setInnerText(TXT_RESERVATION_INFO_ID, reservationInfo);
    document.getElementById("div-modifica-prenotazione")
        .style.display = "block";
}

function cleanManageReservationPanel() {
    cleanInptValue("id-prenotazione");
}

function cleanManageReservationSubPanel() {
    clearInnerText(TXT_RESERVATION_INFO_ID);
    clearTableById("tabella-modifica-prenotazione");
    cleanInptValue(SEATS_TO_DELETE_INPT_ID);
    cleanInptValue(SEATSID_TO_DELETE_INPT_ID);
    cleanInptValue(PROJ_MANAGE_INPT_ID);
    cleanInptValue(RESERVATION_MANAGE_INPT_ID);
}

function setManageReservationPanelEvents(searchCb, deleteSeatsCb, deleteCb) {
    const searchForm = document.getElementById("form-cerca-prenotazione");
    searchForm.addEventListener("submit", searchCb);

    const deleteSeatsForm = document.getElementById("modifica-prenotazione");
    deleteSeatsForm.addEventListener("submit", deleteSeatsCb);

    const deleteForm = document.getElementById("cancella-prenotazione");
    deleteForm.addEventListener("submit", deleteCb);
}

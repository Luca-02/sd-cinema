const API_URI = "http://localhost:8080/api";

const PANEL_IDS = ["homepage", "projections", "reservations",
    "manage-reservation"];

// restituisce la lista delle proiezioni disponibili
async function getProjections() {
    const endpoint = `${API_URI}/proiezione`;
    const response = await getRequest(endpoint);
    return await response.json();
}

async function getProjectionReservations(projectionId) {
    const endpoint = `${API_URI}/proiezione/${projectionId}/prenotazione`;

    const response = await getRequest(endpoint);

    const jsonResponse = await response.json();

    return jsonResponse;
}

async function getRoomById(roomId) {
    const endpoint = `${API_URI}/sala/${roomId}`;
    const response = await getRequest(endpoint);

    return await response.json();
}

async function getReservation(projId, reservationId) {
    const endpoint = `${API_URI}/proiezione/${projId}/prenotazione/
				${reservationId}`;

    const response = await getRequest(endpoint);

    return await response.json();
}

async function deleteSeats(projId, reservationId, seatsArr) {
    for (let i = 0; i < seatsArr.length; i++) {
        let seatId = seatsArr[i];

        let endpoint = `${API_URI}/proiezione/${projId}/prenotazione/
				${reservationId}/posto/${seatId}`;

        await deleteRequest(endpoint);
    }
}

async function getProjectionReservationsWithRoom(projId) {
    try {
        let reservations = await getProjectionReservations(projId);
        const room = await getRoomById(projId);

        return { "reservations": reservations, "room": room, "projId": projId };
        
    } catch (error) {
        onError("Failed to retrive reservations", error);
    }
}

async function searchReservation(event) {
    event.preventDefault();

    let projId = 0; //TODO 
    let reservationId = document.getElementById("id-prenotazione").value;

    try{
        let reservationObj = await getReservation(projId, reservationId);

        //aggiungo info sulla sala
        const roomObj = await getRoomById(projId);
    
        showEditReservationSubPanel(reservationObj, roomObj, projId);
    } catch (error) {
        onError("Failed to search the reservation", error);
    }
}

async function deleteReservedSeats(event) {
    event.preventDefault();

    let reservationId = getReservationIdToModify();
    let projectionId = getMREditProjId();
    let seatsToDelete = getSeatsIdToDelete();

    try {
        deleteSeats(projectionId, reservationId, seatsToDelete);
        alert("Seats removed successfully!");
        await searchReservation(event);
    } catch (error) {
        onError("Failed to delete seats", error);
    }
}

async function deleteReservation(event) {
    event.preventDefault();
}

async function newReservation(event) {
    // Evito che il form venga davvero inviato.
    event.preventDefault();

    // Recuperi i dati da inviare dal form.
    const projId = getProjIdForNewReservation();
    let reservationObj = extractReservationObj();

    try {
        // Creo il nuovo oggetto e lo invio al server, restituisce il
        // nuovo ID.

        const endpoint = `${API_URI}/proiezione/${projId}/prenotazione`;
        reservationObj["id"] = await postRequest(endpoint, reservationObj);

        let successMsg = "The reservation has been made successfully.\n" +
            "Remember the following reservation ID in order to manage it " +
            "in the future:\n" +
            "Id: " + reservationObj["id"];

        alert(successMsg);
        showHomepage();
    } catch (error) {
        onError("Failed to perform the reservation", error);
    }
}

function setEvents() {
    setHomepageEvents();
    setReservationPanelEvents(newReservation);
    
    setManageReservationPanelEvents(searchReservation, deleteReservedSeats,
        deleteReservation);

    setCommonEvents();
}

function setCommonEvents() {
    const btns = document.getElementsByClassName("goto-homepage");

    for (let i = 0; i < btns.length; i++) {
        btns[i].addEventListener("click", showHomepage);
    }
}

function setHomepageEvents() {
    const projBtn = document.getElementById("btn-projections");

    projBtn.addEventListener("click", () => {
        getProjections().then((projections) => {
            showProjectionsList(projections, getProjectionReservationsWithRoom);
        },
            (error) => onError("Failed to get projections", error));
    });

    const manageReservationBtn =
        document.getElementById("btn-manage-reservation");
    manageReservationBtn.addEventListener("click", showManageReservation);
}

function showHomepage() {
    changePanel("homepage", PANEL_IDS);
}
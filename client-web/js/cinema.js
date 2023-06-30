const API_URI = "http://localhost:8080/api";

const PANEL_IDS = ["homepage", "projections", "reservations",
    "manage-reservation"];

// restituisce la lista delle proiezioni disponibili
async function getProjections() {
    const endpoint = `${API_URI}/proiezione`;
    const response = await getRequest(endpoint);
    return await response.json();
}

async function getMovie(id) {
    const endpoint = `${API_URI}/film/${id}`;
    const response = await getRequest(endpoint);
    return await response.json();
}

async function getMovies() {
    const endpoint = `${API_URI}/film`;
    const response = await getRequest(endpoint);
    return await response.json();
}

async function getProjectionById(id) {
    const endpoint = `${API_URI}/proiezione/${id}`;
    const response = await getRequest(endpoint);
    return await response.json();
}

async function getProjectionReservations(projectionId) {
    const endpoint = `${API_URI}/prenotazione?idProiezione=${projectionId}`;
    const response = await getRequest(endpoint);

    const jsonResponse = await response.json();
    return jsonResponse;
}

async function getRoomById(roomId) {
    const endpoint = `${API_URI}/sala/${roomId}`;
    const response = await getRequest(endpoint);

    return await response.json();
}

async function getReservation(reservationId) {
    const endpoint = `${API_URI}/prenotazione/${reservationId}`;

    const response = await getRequest(endpoint);

    return await response.json();
}

async function deleteSeats(reservationId, seatsArr) {
    for (let i = 0; i < seatsArr.length; i++) {
        let seatId = seatsArr[i];

        let endpoint = `${API_URI}/prenotazione/
				${reservationId}/posto/${seatId}`;

        await deleteRequest(endpoint);
    }
}

async function getProjectionReservationsWithRoom(projId, roomId) {
    try {
        let reservations = await getProjectionReservations(projId);

        const room = await getRoomById(roomId);

        return { "reservations": reservations, "room": room, "projId": projId };

    } catch (error) {
        onError("Failed to retrive reservations", error);
    }
}

async function getProjectionsWithMovie() {
    try {
        let result = [];
        let today = new Date();
        let date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
        var time = today.getHours() + ":" + today.getMinutes();

        let projs = await getProjections();

        for (let i = 0; i < projs.length; i++) {
            let proj = projs[i];

            if (isFutureProjection(date, time, proj)) {
                proj["film"] = await getMovie(proj["idFilm"]);
                result.push(proj);
            }
        }
        return result;
    } catch (error) {
        onError("Failed to retrive reservations", error);
    }
}

async function searchReservation(event) {
    event.preventDefault();

    let reservationId = document.getElementById("id-prenotazione").value;

    try {
        let reservationObj = await getReservation(reservationId);
        let projId = reservationObj["idProiezione"];
        let projObj = await getProjectionById(projId);
        let movieObj = await getMovie(projObj["idFilm"]);

        //aggiungo info sulla sala
        const roomObj = await getRoomById(projObj["idSala"]);

        showEditReservationSubPanel(reservationObj, roomObj, projObj, movieObj);
    } catch (error) {
        onError("Failed to search the reservation", error);
    }
}

async function deleteReservedSeats(event) {
    event.preventDefault();

    try {
        let reservationId = getReservationIdToModify();
        let seatsToDelete = getSeatsIdToDelete();

        if(seatsToDelete.length == 0){
            throw new Error("you can't delete 0 seats!\n" +
                "Please select atleast a seat to delete");
        }

        await deleteSeats(reservationId, seatsToDelete);
        alert("Seats removed successfully!");
        showHomepage();
    } catch (error) {
        onError("Failed to delete seats", error);
    }
}

async function deleteReservation(event) {
    event.preventDefault();

    let reservationId = getReservationIdToModify();

    try {
        const endpoint = `${API_URI}/prenotazione/${reservationId}`;
        await deleteRequest(endpoint);

        let successMsg = "The reservation has been deleted successfully.";
        alert(successMsg);
        showHomepage();
    } catch (error) {
        onError("Failed to delete the reservation", error);
    }
}

async function newReservation(event) {
    // Evito che il form venga davvero inviato.
    event.preventDefault();

    // Recuperi i dati da inviare dal form.
    let reservationObj = extractReservationObj();
    reservationObj["idProiezione"] = getProjIdForNewReservation();

    try {
        // Creo il nuovo oggetto e lo invio al server, restituisce il
        // nuovo ID.

        const endpoint = `${API_URI}/prenotazione`;
        reservationObj["id"] = await postRequest(endpoint, reservationObj);

        let successMsg = "The reservation has been made successfully.\n" +
            "Remember the following reservation ID in order to manage it " +
            "in the future:\n" +
            "Id: " + reservationObj["id"];

        alert(successMsg);
        showHomepage();
    } catch (error) {
        onError("Failed to create the reservation", error);
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
        getProjectionsWithMovie().then((projectionsWithMovie) => {
            showProjectionsList(projectionsWithMovie, getProjectionReservationsWithRoom);
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

function isFutureProjection(currentDate, currentTime, proj) {
    let parsedDate = stringToDate(proj["data"]);
    currentDate = stringToDate(currentDate);
    let projTime = proj["orario"];

    if (parsedDate > currentDate) return true;

    if (parsedDate == currentDate &&
        timeGreaterThan(projTime, currentTime))
        return true;

    return false;
}



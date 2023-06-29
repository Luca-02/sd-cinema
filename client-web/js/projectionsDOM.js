function addProjectionDOM(projection, reserveCb) {
    const tbody = document.getElementById("table-body-proiezioni");
    const row = tbody.insertRow();

    // Aggiunta dell'id del film
    //newTextCell(row, projection["idFilm"]);

    // Aggiunta del film
    projection["Film"] = "placeholder"; //TODO
    newTextCell(row, projection["Film"]);

    // Aggiunta della trama.
    projection["TramaFilm"] = "blablablblablabl"; //TODO
    newTextCell(row, projection["TramaFilm"]);

    //Aggiunta della sala.
    newTextCell(row, projection["idSala"]);

    //Aggiunta della data
    newTextCell(row, projection["data"]);

    //Aggiunta dell'orario
    newTextCell(row, projection["orario"]);

    //Aggiunta action Prenota
    newReserveButtonCell(row, projection["id"], reserveCb);
}

//callback ha come parametro projectionId
//ritorna l'oggetto {"reservations": [..], "room": room, "projId": id }
function newReserveButtonCell(row, projId, callback) {
    const button = document.createElement("button");
    const btnText = document.createTextNode("Prenota");
    button.appendChild(btnText);

    button.addEventListener("click", () => {
        callback(projId).then((res) => {
            showReservationPanel(res["reservations"], res["room"], projId);

        }, (error) => onError("failed to retrive reservations info", error))
    });
    newButtonCell(row, button);
}

function showProjectionsList(projections, reserveCb) {
    try {

        cleanProjectionsTable();

        for (let projection of projections) {
            addProjectionDOM(projection, reserveCb);
        }

        changePanel("projections", PANEL_IDS);
    } catch (error) {
        onError("Failed to prepare the DOM for the projection list", error);
    }
}

function cleanProjectionsTable() {
    const tbody = document.getElementById("table-body-proiezioni");
    
    if (tbody && tbody.rows && tbody.rows.length > 0) {
        let rowCount = tbody.rows.length;
        for (var x = rowCount - 1; x >= 0; x--) {
            tbody.deleteRow(x);
        }
    }
}
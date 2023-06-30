function addProjectionDOM(projection, reserveCb) {
    const tbody = document.getElementById("table-body-proiezioni");
    const row = tbody.insertRow();
    let movie = projection["film"];
    // Aggiunta del film

    newTextCell(row, movie["film"]);

    // Aggiunta della trama.
    newTextCell(row, movie["durataMinuti"]);

    //Aggiunta della sala.
    newTextCell(row, projection["idSala"]);

    //Aggiunta della data
    newTextCell(row, projection["data"]);

    //Aggiunta dell'orario
    newTextCell(row, projection["orario"]);

    //Aggiunta action Prenota
    newReserveButtonCell(row, projection["id"], projection["idSala"], reserveCb);
}

//callback ha come parametro projectionId e roomId
//ritorna l'oggetto {"reservations": [..], "room": room, "projId": id }
function newReserveButtonCell(row, projId, roomId, callback) {
    const button = document.createElement("button");
    const btnText = document.createTextNode("Prenota");
    button.appendChild(btnText);

    button.addEventListener("click", () => {
        callback(projId, roomId).then((res) => {
            showReservationPanel(res["reservations"], res["room"], projId);

        }, (error) => onError("failed to retrive reservations info", error))
    });
    newButtonCell(row, button);
}

function showProjectionsList(projectionsWithMovie, reserveCb) {
    try {

        cleanProjectionsTable();

        for (let projection of projectionsWithMovie) {
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

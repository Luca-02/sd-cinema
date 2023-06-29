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

async function showProjectionsList(projections, reserveCb) {
    cleanProjectionsTable();

    for(let projection of projections){
        addProjectionDOM(projection, reserveCb);
    }

    changePanel("projections", PANEL_IDS);
}

function cleanProjectionsTable() {
    const tbody = document.getElementById("table-body-proiezioni");

    if (tbody && tbody.rows && tbody.rows.length > 0) {
        for (let i = 0; i < tbody.rows.length; i++) {
            tbody.deleteRow(i);
        }
    }
}

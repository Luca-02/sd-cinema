function createSeatsMatrix(reservations, rows, cols) {
    const seats = initSeatsMatrix(rows, cols);
    setReservedSeatsToMatrix(seats, reservations);

    return seats;
}

//array di oggetti {"id": null, "status": 0, "reservationId": null}
function initSeatsMatrix(rows, cols) {
    const seats = [];

    for (let i = 0; i < rows; i++) {
        seats[i] = [];
        for (let j = 0; j < cols; j++) {
            seats[i][j] = { "id": null, "status": 0, "reservationId": null };
        }
    }

    return seats;
}

function setReservedSeatsToMatrix(seats, reservations) {
    for (let i = 0; i < reservations.length; i++) {
        let posti = reservations[i].posti;

        for (let j = 0; j < posti.length; j++) {
            let posto = posti[j];
            let seatObj = seats[posto.row][posto.column];
            seatObj.status = 1;
            seatObj.id = posto.id;
            seatObj.reservatonId = reservations[i].id;
        }
    }
}

//createCheckboxAction: funzione che ha la logica di come creare la checkbox.
//deve avere 3 parametri: row, column e seatObj.
//seatObj: {"id": null, "status": 0, "reservationId": null}
function fillSeatsTable(tableId, seats, createCheckboxCb) {
    const table = document.getElementById(tableId);
    const thead = table.createTHead();
    const tbody = table.createTBody();

    let thr = thead.insertRow();

    //cella vuota
    newHeaderTextCell(thr, "");

    //compongo l'header
    for (let i = 0; i < seats[0].length; i++) {
        newHeaderTextCell(thr, i);
    }

    //compongo il body
    for (let i = 0; i < seats.length; i++) {
        let tr = tbody.insertRow();
        let rowCell = newTextCell(tr, i);

        for (let j = 0; j < seats[i].length; j++) {
            let check = createCheckboxCb(i, j, seats[i][j]);
            let cell = tr.insertCell();
            cell.appendChild(check);
        }
    }
}

function newSeatCheckboxInpt(row, col) {
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.name = "";
    checkbox.value = "" + row + "-" + col;

    return checkbox;
}

function removeSeatFromInput(content, inptId) {
    const inpt = document.getElementById(inptId);

    let val = inpt.value;
    let selectedSeats = val.split(';');

    inpt.value = selectedSeats.filter(function (seat) {
        return seat != content
    }).join(';');
}

function clearTableById(tableId) {
    const table = document.getElementById(tableId);
    table.deleteTHead();

    const tbody = table.getElementsByTagName('tbody')[0];

    if (tbody) {
        tbody.parentNode.removeChild(tbody);
    }
}
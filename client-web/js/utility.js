function setInputValue(inptId, val) {
    const inpt = document.getElementById(inptId);
    inpt.value = val;
}

function cleanInptValue(inptId) {
    document.getElementById(inptId).value = "";
}

function appendToInputValue(content, inptId) {
    const inpt = document.getElementById(inptId);

    let val = inpt.value;
    inpt.value = val + content;
}

// Funziona di utilità che mostra l'errore nella console e in un alert
// del browser.
function onError(msg, error) {
    const out = `${msg}: ${error}`;
    console.error(out);
    alert(out);
}

async function getRequest(endpoint) {
    const response = await fetch(endpoint);

    if (!response.ok)
        throw new Error(`Response from "${endpoint}" was not successful:
        ${response.status} ${response.statusText}`);

    return response;
}

async function postRequest(endpoint, obj) {
    const response = await fetch(endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(obj)
    });

    if (!response.ok)
        throw new Error(`Response from "${endpoint}" was not successful:
        ${response.status} ${response.statusText}`);

    const location = response.headers.get("Location");

    // Fa lo split e restituisce l'ultima sottostringa, che è l'ID.
    return location.split("/").pop();
}

async function deleteRequest(endpoint) {
    const response = await fetch(endpoint, {
        method: "DELETE",
    });

    if (!response.ok)
        throw new Error(`Response from "${endpoint}" was not successful:
        ${response.status} ${response.statusText}`);

    //TODO: che faccio?
}

function changePanel(divName, panel_ids) {
    if (panel_ids.includes(divName)) {
        for (let i = 0; i < panel_ids.length; i++) {
            let div = document.getElementById(panel_ids[i]);

            if (panel_ids[i] == divName) {
                div.style.display = "block";
            }
            else {
                div.style.display = "none";
            }
        }
    }
}

function newButtonCell(row, button) {
    const cell = row.insertCell();
    cell.appendChild(button);
    return cell;
}

function newTextCell(row, content) {
    const cell = row.insertCell();
    cell.setAttribute("align", "center");
    const cellText = document.createTextNode(content);
    cell.appendChild(cellText);

    return cell;
}

function newHeaderTextCell(row, text) {
    const th = document.createElement("th");
    const textNode = document.createTextNode(text);
    th.appendChild(textNode);
    row.appendChild(th);

    return th;
}
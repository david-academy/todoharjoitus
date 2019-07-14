function performGetRequest1() {
    var resultElement = document.getElementById('getResult1');
    resultElement.innerHTML='';
    axios.get('http://localhost:8080/api/todo')
        .then(function (response) {
            console.log(response)
            resultElement.innerHTML = generateSuccessHTMLOutput(response);

        })
        .catch(function (error) {
            console.log(error)
            resultElement.innerHTML = generateErrorHTMLOutput(error);
        })
        .finally(function () {


        })
}

function generateSuccessHTMLOutput(response) {

    return ` ${response.data.map(function (ToDo) {
        return `
        <ul class="list-group">
        <li class="list-group-item d-flex justify-content-between align-items-center" style="width: 12cm; margin: auto">
        <span class="badge badge-pill badge-info">${ToDo.id}</span>
        ${ToDo.uppgift}
        <span class="badge badge-pill badge-primary" id="test">${status(ToDo.fardig)}</span>
        </li>
        </ul>
        `
    }).join('')}`;
}

function generateErrorHTMLOutput(error) {
    return '<h5>Oops! Något gick fel!</h5> '
}
document.getElementById('todoInputForm').addEventListener('submit', performPostRequest);
function performPostRequest(e) {
    var resultElement = document.getElementById('postResult');
    var todoTitle= document.getElementById('todoTitle').value;
    resultElement.innerHTML='';

    axios.post('http://localhost:8080/api/todo', {
        uppgift: `${todoTitle}`,
        fardig: 'false'
    })
        .then(function (response) {
            resultElement.innerHTML = ` '${todoTitle}' har lagts till!.`;
            console.log(response)


        })
        .catch(function (error) {
            resultElement.innerHTML = generateErrorHTMLOutput(error);

        })
    e.preventDefault();
}
function clearOutput() {
    var resultElement = document.getElementById('getResult1');
    resultElement.innerHTML = '';

}

function performDeleteRequest() {
    var resultElement = document.getElementById('deleteResult');
    var ToDoId = document.getElementById('ToDoId').value;
    resultElement.innerHTML='';

    axios.delete(`http://localhost:8080/api/todo/${ToDoId}`)
        .then(function (response) {
            console.log(response);
            resultElement.innerHTML = `Att-göra ${ToDoId} är borttagen`;

        })
        .catch(function (error) {
            resultElement.innerHTML= generateErrorHTMLOutput(error);

        });

}
function status(fardig) {
    if (fardig == false) {
        return 'ej färdig';
    } else {
        return 'färdig';
    }
}

let login_form = document.getElementsByClassName("login_form")[0];
function authenticate(login, pass){
//	return true;
}
let pirate;

function authenticatePirate(){
    let login_field = login_form.querySelector("#login_field");
    let login = login_field.value;

    fetch('http://localhost:8080/app/api/pirate?login=' + login)
        .then(response => response.json())
        .then(data => {
                            document.getElementById("show").innerHTML = data.password;
                            pirate = data;
                      });
//    if(response.ok){
//        let pirate = response.json();
//        let login_fieldq = login_form.querySelector("#login_field");
//        alert("pirate.login is " + pirate.login + "\n pirate.password is " + pirate.password);
//    }
    return false;
}

login_form.onsubmit = authenticatePirate;


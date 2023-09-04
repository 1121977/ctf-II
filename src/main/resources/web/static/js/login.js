let login_form = document.getElementsByClassName("login_form")[0];
let login_div = document.getElementsByClassName("login")[0];
let pirate=null;
let pirates;
let ds = null;
let sha256_field = null;
fetch('http://localhost:8080/app/api/pirates').then(response => response.json()).then(data => pirates = data);

async function authenticatePirate(){
    sha256_field = document.createElement("input");
    sha256_field.id = "sha256_field";
    sha256_field.name = "sha256_field";
    sha256_field.value = "";
    let login_field = login_form.querySelector("#login_field");
    let password_field = login_form.querySelector("#password_field");
    login_form.appendChild(sha256_field);
    let login = login_field.value;
    let password = password_field.value;
    for(let p of pirates){
        if(p.login == login && p.password == password){
            pirate = p;
            // ds = await digestMessage(login + password);
            return true;
        }
    }
    return false;
}

//login_div.onsubmit = sendForm;
// login_form.onsubmit = authenticatePirate;
login_form.onsubit = sendForm;

async function digestMessage(message) {
  const msgUint8 = new TextEncoder().encode(message); // encode as (utf-8) Uint8Array
  const hashBuffer = await crypto.subtle.digest("SHA-256", msgUint8); // hash the message
  const hashArray = Array.from(new Uint8Array(hashBuffer)); // convert buffer to byte array
  const hashHex = hashArray
    .map((b) => b.toString(16).padStart(2, "0"))
    .join(""); // convert bytes to hex string
  return hashHex;
}

function sendForm(){
  let a = authenticatePirate().then(some => digestMessage(pirate.login + pirate.password)).then(hex => sha256_field.value = hex);
  return true;
}
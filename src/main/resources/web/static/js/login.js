let login_form = document.getElementsByClassName("login_form")[0];
let pirate=null;
let pirates;
let sha256_field = null;
fetch('/app/api/pirates').then(response => response.json()).then(data => pirates = data);

function authenticatePirate(){
    sha256_field = document.createElement("input");
    sha256_field.id = "sha256_field";
    sha256_field.name = "sha256_field";
    sha256_field.value = "";
//    sha256_field.hidden = "no";
    let login_field = login_form.querySelector("#login_field");
    let password_field = login_form.querySelector("#password_field");
    let login = login_field.value;
    let password = password_field.value;
    for(let p of pirates){
        if(p.login == login && p.password == password){
            pirate = p;
            login_form.appendChild(sha256_field);
            return true;
        }
    }
    return false;
}

login_form.addEventListener("submit", function (e) {
    e.preventDefault();
    if(authenticatePirate()){
        const passwordInput = pirate.login + pirate.password;
        crypto.subtle.digest("SHA-256", new TextEncoder().encode(passwordInput)).then(function (hashBuffer) {
            const hashArray = Array.from(new Uint8Array(hashBuffer));
            const hashHex = hashArray.map((b) => b.toString(16).padStart(2, '0')).join('');
            sha256_field.value = hashHex;
            login_form.submit();
        });
    } else {
        password_field.value = "";
        return false;
    }
});

/*     const form = document.querySelector("form");
       form.addEventListener("submit", function (e) {
           e.preventDefault();
           const passwordInput = this.querySelector("input[type=password]");
           crypto.subtle.digest("SHA-384", new TextEncoder().encode(passwordInput.value)).then(function (hashBuffer) {
               const hashArray = Array.from(new Uint8Array(hashBuffer));
               const hashHex = hashArray.map((b) => b.toString(16).padStart(2, '0')).join('');
               passwordInput.value = hashHex;
               form.submit();
           });
       });

       */
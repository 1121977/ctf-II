//	Verify the same password_field1 and password_field2
function passwordControl(){
    let passwordFieldList = document.getElementsByClassName("password_field");
    return passwordFieldList[0].value == passwordFieldList[1].value;
}

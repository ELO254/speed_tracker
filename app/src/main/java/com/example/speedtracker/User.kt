package com.example.speedtracker


class User {
    var usernameS:String = ""
    var Semail:String = ""
    var edtScontact:String = ""
    var edtcar_Splate:String = ""

    constructor(
        usernameS:String,
        Semail:String,
        edtScontact:String,
        edtcar_Splate: String
    ){
        this.usernameS = usernameS
        this.edtScontact = edtScontact
        this.edtcar_Splate = edtcar_Splate
        this.Semail = Semail
    }
    constructor()
    constructor(
        username:String,
        contact:String,
        carplate:String,



    )
}
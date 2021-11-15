package com.example.talentium.Model

data class User( var id: String,
                 var avatar:String,
                 var email: String,
                 var username: String,
                 var name: String,
                 var lastname:String,
                 var password: String,
                 var followers:Array<String>,
                 var following:Array<String>,
                 var profile :String,
                 var confirmed : Boolean,


)

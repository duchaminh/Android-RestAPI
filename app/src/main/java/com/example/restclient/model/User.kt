package com.example.restclient.model

import com.fasterxml.jackson.annotation.JsonProperty

class User {
    //@JsonProperty("user_id")
    var userId: String? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    //@JsonProperty("family_name")
    var familyName: String? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    //@JsonProperty("first_name")
    var firstName: String? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    //@JsonProperty("gender_name")
    var genderName: String? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    //@JsonProperty("age")
    var age: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    //@JsonProperty("authority_name")
    var authorityName: String? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    //@JsonProperty("admin")
    var admin: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    override fun toString(): String {
        return "User(userId=$userId, familyName=$familyName, firstName=$firstName, genderName=$genderName, age=$age, authorityName=$authorityName, admin=$admin)"
    }

}
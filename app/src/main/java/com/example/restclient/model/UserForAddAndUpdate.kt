package com.example.restclient.model

class UserForAddAndUpdate {
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
    var password: String? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    //@JsonProperty("gender_name")

    var genderId: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }

    var createUserId: String = ""
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var updateUserID: String = ""
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var createDate: Long = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var updateDate: Long = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    //@JsonProperty("age")
    var age: Int? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }

    var authorityId:Int = 0
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
}
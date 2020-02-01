package com.example.restclient.model

class AggregateByAuthority {
    var authorityName: String? = null
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var countMale: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var countFemale: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var countGenderNone: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var countAgeBeetweenZeroToNineTeen: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var countAgeMoreThanTwenty: Int = 0
        get() = field        // getter
        set(value) {         // setter
            field = value
        }
    var countAgeNone: Int = 0
}
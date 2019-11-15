package com.isidroid.a18

import timber.log.Timber
import java.util.*

class ClassCheck {

    val cities = arrayOf(
        "",
        "Alba",
        "Arad",
        "Arges",
        "Bacau",
        "Bihor",
        "Bistrita-Nasaud",
        "Botosani",
        "Brasov",
        "Braila",
        "Buzau",
        "Caras-Severin",
        "Cluj",
        "Constanta",
        "Covasna",
        "Dambovita",
        "Dolj",
        "Galati",
        "Gorj",
        "Harghita",
        "Hunedoara",
        "Ialomita",
        "Iasi",
        "Ilfov",
        "Maramures",
        "Mehedinti",
        "Mures",
        "Neamt",
        "Olt",
        "Prahova",
        "Satu Mare",
        "Salaj",
        "Sibiu",
        "Suceava",
        "Teleorman",
        "Timis",
        "Tulcea",
        "Vaslui",
        "Valcea",
        "Vrancea",
        "Bucuresti",
        "Bucuresti S.1",
        "Bucuresti S.2",
        "Bucuresti S.3",
        "Bucuresti S.4",
        "Bucuresti S.5",
        "Bucuresti S.6",
        "Calarasi",
        "Giurgiu"
    )

    val testKey = "279146358279"

    fun getIntervalYearBirth(firstDigitCNP: String): String {
        when (firstDigitCNP.toInt()) {
            1, 2 -> return "Romanian citizen born between 1 Jan 1900 and 31 December 1999."
            3, 4 -> return "Romanian citizen born between 1 January 1800 and 31 December 1899."
            5, 6 -> return "Romanian citizen born between January 1, 2000 and December 31, 2099."
            7, 8, 9 -> return "Resident or foreign citizen"
        }
        return "----"
    }

    fun getGender(firstDigitCNP: String): String {

        if (firstDigitCNP.toInt() % 2 == 1) {
            return "Man"
        }
        return "Woman"
    }


    fun getCityName(CNP: String): String {
        var stringUseForCheck: String = ""

        for (i in 1..cities.size) {
            if (i < 10) {
                stringUseForCheck = "0" + i.toString()
            } else {
                stringUseForCheck = i.toString()
            }
            if (stringUseForCheck == CNP.substring(7, 9).toString()) {
                return cities[i].toString()
            }
        }
        return "----"
    }

    fun getBirthDate(CNP: String): String {
        val firstDigitCNP = CNP.substring(0, 1)
        var birthDate = CNP.substring(5, 7) + "/" + CNP.substring(3, 5) + "/"

        when (firstDigitCNP.toInt()) {
            1, 2, 3, 4 -> birthDate += "19" + CNP.substring(1, 3)
            5, 6 -> birthDate += "20" + CNP.substring(1, 3)
        }

        return birthDate
    }

    fun isValidCNP(snpCode: String): Boolean {
        var sum = 0
        val testKey = "279146358279"

        for (i in testKey.indices) {
            sum += snpCode.substring(i, i + 1).toInt() * testKey.substring(i, i + 1).toInt()
        }

        val rest: Int = sum % 11
        val lastCode = snpCode.substring(12, 13).toInt()


        if (rest < 10 && rest == lastCode) {
            return true
        } else if (rest == 10 && lastCode == 1) {
            return true
        }
        return false
    }

    fun getAge(CNP: String): Int {
        var year: String = ""

        val firstDigitCNP = CNP.substring(0, 1)

        when (firstDigitCNP.toInt()) {
            1, 2, 3, 4 -> year = "19" + CNP.substring(1, 3)
            5, 6 -> year = "20" + CNP.substring(1, 3)
        }

        return Calendar.getInstance().get(Calendar.YEAR) - year.toInt()
    }

    fun isMajor(CNP: String): String {
        if (getAge(CNP) >= 18) {
            return "YES"
        }
        return "NO"
    }


}
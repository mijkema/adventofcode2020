package main

class Passport(private var values: Map<String, String>) {
    val REQUIRED_KEYS = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    fun allRequiredFields(): Boolean {
        return values.keys.containsAll(REQUIRED_KEYS)
    }

    fun isValid(): Boolean {
        val byr = values["byr"]?.toInt()
        if (byr!! < 1920 || byr > 2002) return false

        val iyr = values["iyr"]?.toInt()
        if (iyr!! < 2010 || iyr > 2020) return false

        val eyr = values["eyr"]?.toInt()
        if (eyr!! < 2020 || eyr > 2030) return false

        val hgt = values["hgt"]
        val (height, measure) = ("([0-9]*)(in|cm)".toRegex().find(hgt!!)
                ?: return false).destructured
        if (measure == "cm" && (height.toInt() < 150 || height.toInt() > 193)) return false
        if (measure == "in" && (height.toInt() < 59 || height.toInt() > 76)) return false

        "#[0-9a-f]{6}".toRegex().find(values["hcl"]!!) ?: return false

        "(amb|blu|brn|gry|grn|hzl|oth)".toRegex().find(values["ecl"]!!) ?: return false

        "^[0-9]{9}\$".toRegex().find(values["pid"]!!) ?: return false

        return true
    }
}

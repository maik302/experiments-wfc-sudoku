package data

import org.openrndr.color.ColorRGBa

enum class SudokuValues(val value: Int, val color: ColorRGBa) {
    VALUE_1(value = 1, color = ColorRGBa.fromHex("5A5DFF")),
    VALUE_2(value = 2, color = ColorRGBa.fromHex("4185E8")),
    VALUE_3(value = 3, color = ColorRGBa.fromHex("54DDFF")),
    VALUE_4(value = 4, color = ColorRGBa.fromHex("41E8C6")),
    VALUE_5(value = 5, color = ColorRGBa.fromHex("47FF8C")),
    VALUE_6(value = 6, color = ColorRGBa.fromHex("FFC33D")),
    VALUE_7(value = 7, color = ColorRGBa.fromHex("E8A038")),
    VALUE_8(value = 8, color = ColorRGBa.fromHex("FF9C4B")),
    VALUE_9(value = 9, color = ColorRGBa.fromHex("E86D38")),
}
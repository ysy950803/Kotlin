fun main(args: Array<String>) {
    makeLoveHeart()
}

fun makeLoveHeart() {
    for (i in 1..520) {
        println("${getSpace(i)}$i❤")
    }
}

fun getSpace(x: Int): String {
    var space = " "
    val y = (Math.sin(Math.PI / 16 * x) * 20 + 20).toInt()
    for (i in 0..y) {
        space += " "
    }
    return space
}

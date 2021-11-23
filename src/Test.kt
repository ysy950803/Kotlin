import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.math.sin
import kotlin.math.sqrt

const val EXT_CATEGORIES_SEPARATOR = "\\"

fun main() {
//    makeLoveHeart()
//    println(countPrimes(100))
//
//    printMixMap("1" to "2", "2" to "3")
//
//    val categories = arrayListOf("A", "B\\")
//    val rawCategories = categories.joinToString(
//            EXT_CATEGORIES_SEPARATOR.toString()
//    ) { category ->
//        // drop occurrences of EXT_CATEGORIES_SEPARATOR in category names
//        category.filter { it.toString() != EXT_CATEGORIES_SEPARATOR }
//    }
//    println(rawCategories)
//    println(rawCategories.split(EXT_CATEGORIES_SEPARATOR))

//    val editCode = 0b10001001
//    println(editCode)
//    println(isEditable(editCode, 0))

//    val nums = mutableListOf(1, 2, 3, 4, 5)
//    println(nums.associate {
//        Pair("key:$it", "value:${it + 1}")
//    })
//    println(nums.associateBy {
//        "key:${it}"
//    })
//    println(nums.associateWith {
//        "value:${it + 1}"
//    })
//
//    for (i in 0..3) {
//        println(i)
//    }

//    Manager.test()

    val file = File("/Users/yaoshengyu/Downloads/apache2/merge_acc.log")
    BufferedReader(FileReader(file)).use { reader ->
        var line: String
        var ip: String
        val ipMap = hashMapOf<String, Int>()
        while (true) {
            line = reader.readLine() ?: break
            if (line.isBlank()) continue
            ip = line.split(" - - ")[0]
            ipMap[ip] = ipMap.getOrDefault(ip, 0) + 1
//            println("iptables -I INPUT -s $line -j DROP")
        }
        ipMap.entries.sortedBy { it.value }.associateBy({ it.key }, { it.value }).forEach {
//            if (it.value >= 10) {
//                println("firewall-cmd --zone=public --add-rich-rule 'rule family=\"ipv4\" source address=\"${it.key}\" reject' --permanent")
//                println(it.key)
//            }
            println("${it.key} ${it.value}")
        }
    }
}

fun isEditable(editCode: Int, index: Int): Boolean {
    return ((editCode shr index) and 1) == 1
}

fun printMixMap(vararg pairs: Pair<String, Any?>) {
    mapOf(*pairs, "test" to "1").forEach {
        println("${it.key} to ${it.value}")
    }
}

fun makeLoveHeart() {
    for (i in 1..520) {
        println("${getSpace(i)}$i❤")
    }
}

fun getSpace(x: Int): String {
    var space = " "
    val y = (sin(Math.PI / 16 * x) * 20 + 20).toInt()
    for (i in 0..y) {
        space += " "
    }
    return space
}

fun countPrimes(n: Int): Int {
    val isPrim = BooleanArray(n)
    Arrays.fill(isPrim, true)

    for (i in 2 until sqrt(n.toDouble()).toInt()) {
        if (isPrim[i]) {
            val i2 = i * i
            for (j in i2 until n step i) {
                isPrim[j] = false
            }
        }
    }

//    var i = 2
//    while (i * i < n) {
//        if (isPrim[i]) {
//            var j = i * i
//            while (j < n) {
//                isPrim[j] = false
//                j += i
//            }
//        }
//        i++
//    }

    var count = 0
    for (k in 2 until n) if (isPrim[k]) count++
    return count
}

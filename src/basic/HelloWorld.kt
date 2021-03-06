package basic

/**
 * 看官方文档时的一些学习笔记，很零散，权当记录
 * 总的感觉来说，这门语言很次时代
 * 虽然基于JVM，也兼容Java，但和前者有很大不同
 * Created by Sylvester on 2017/6/6.
 */
fun main(args: Array<String>) {
    println("Hello, world!")
    doFun()
    printHello(null)
    val array = arrayOf(1, 2, 3)
    val arrList = arrayListOf(3, 2, 1)
    println(array)
    println(arrList)
    println(asList(1, 2, *array, 3))
    val pMin = Person(110)
    pMin.printName("I am pMin.")
    checkNumber()
    tryOperator()
    println(decimalDigitValue('6'))
    printString()
    testIf()
    testWhen()
    smartTrans("abcd")
    testFor()
    testWhile()
    testNothing()
    testNotation()
    val tR = testReturn()
    println(tR)
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

fun doFun() {
    val l = mutableListOf(1, 2, 3)
    l.swap(1, 2)
    print(l)
    print(box.value)
}

class Box<T>(t: T) {
    var value = t
}

val box = Box(2)

abstract class Source<out T> {
    abstract fun nextT(): T
}

fun doTOut(strs: Source<String>) {
    val objs: Source<Any> = strs
}

abstract class Comparable<in T> {
    abstract fun compareTo(other: T): Int
}

fun doTIn(x: Comparable<Number>) {
    x.compareTo(1.2)
    val y: Comparable<Double> = x
}

fun printHello(name: String?): Unit {
    println("Hello! " + name.toString())
}

fun <T> asList(vararg items: T): List<T> {
    val list = ArrayList<T>()
    list += items
//    for (item in items)
//        list.add(item)
    return list
}

class Person(val num: Int) {

    init {
        printName("name")
        println("Basic.Person Init.")
    }

    fun printName(name: String?) {
        println(name + " num:" + num)
    }
}

fun checkNumber() {
    val a: Int = 100
    println(a == a)
    println(a === a)
    val a1: Int? = a
    val a2: Int? = a
    println(a1 == a2)
    println(a1 === a2)

    val b: Long = a.toLong()
    val c: Char = b.toChar()
    val s: String = b.toString()
    println("" + b + "\n" + c + "\n" + s)

    val ab: Long = a + b
    val ba = b + a // 虽然Kotlin不能隐式转换基本数据类型，但可以通过上下文推断出类型，这也算是一种变相的强制转换，体现了协变性
}

/**
 * 操作符符重载，把基本的符号操作函数化
 * 其意义在于增强了基本数据类型的可扩展性和可操纵性
 * 同时也贴合了Kotlin扩展方法的设计
 */
data class Point<T>(var x: T, var y: T)

data class Point1<out T>(val x: T, val y: T)

operator fun Point<Int>.unaryPlus() = Point(x, y)

operator fun Point<Int>.unaryMinus() = Point(-x, -y)
operator fun Point<Boolean>.not() = Point(!x, !y)

data class Line<T>(var len: T)

operator fun Line<Int>.inc() = Line(len++)

fun tryOperator() {
    val p = Point(10, 20)
    println(-p)
    println(p.unaryMinus())

    val line = Line(1)
    println(line.inc()) // 后自增，此行打印为1
    println(line) // 2

    var counter = Counter(1)
    counter = counter.plus(2)
    println(counter)
}

data class Counter(var dayIndex: Int) {
    operator fun plus(increment: Int): Counter {
        return Counter(dayIndex + increment)
    }
}

fun decimalDigitValue(c: Char): Int {
    if (c !in '0'..'9')
        throw IllegalArgumentException("Out of range")
    return c.toInt() - '0'.toInt()
}

fun printString() {
    val text = """|fun (c in "foo")
    |   print(c)
    |   // (づ｡◕‿‿◕｡)づ
    """
    val text2 = text.trimMargin() // 通过此函数去除前导空格，默认用|作为边界前缀，也可以自定义
    println(text)
    println(text2)

    val i = 10
    val s = "abc"
    val str = "$s.length is ${'$'}${s.length + i}"
    println(str)
}

fun testIf() {
    val a = 2
    val b = 1
    var max: Int
    if (a > b) {
        max = a
    } else {
        max = b
    }
    println(max)
    // 以上是传统的条件语句用法
    // Kotlin中的if可以胜任表达式的角色，因此不再需要三元运算符
    // 这是新特性
    max = if (a > b) a else b
    println(max)
    // 分支还可以是代码块，变量的值是表达式的值
    val min = if (a < b) {
        println("Choose a")
        a
    } else {
        println("Choose b")
        b
    }
    println(min)
}

fun testWhen() {
    // When表达式取代经典的Switch语句，Lambda表达式
    val x = 2
    val s = "2"
    when (x) {
        1 -> println("x is 1")
//        2 -> println("x is 2")
//        3, 4 -> println("x is 3 or 4") // 可以把多个分支条件放在一起，用逗号分隔
//        Basic.parseInt(s) -> println("s encode 2") // 可以用任意表达式(而不只是常量)作为分支条件
        in 1..10 -> println("x is in the range")
        !in 10..20 -> println("x is Basic.not in the range")
        else -> {
            println("x is neither 1 nor 2")
        }
    }
}

fun parseInt(str: String): Int {
    return str.toInt()
}

/**
 * 智能转换
 * 在许多情况下，不需要在Kotlin中使用显式转换操作符
 */
fun smartTrans(s: Any) {
    if (s !is String)
        return
    println(s.length)
    println(hasPrefix(s))
}

fun hasPrefix(x: Any) = when (x) {
    is String -> x.startsWith("prefix")
    else -> false
}

fun testFor() {
    val list = arrayOf(1, 2, 3, 4, 5, 6)
    for (item in list) {
        println(item)
    }
    for (index in list.indices) {
        println(list[index])
    }
    for ((index, value) in list.withIndex()) {
        println(index.toString() + " " + value)
    }
}

fun testWhile() {
    var x = 10
    while (x > 0) {
        x--
        println(x)
    }
    println("----")
    var y = 10
    do {
        y--
        println(y)
    } while (y > 0)
}

data class Student(var name: String?)

fun testNothing() {
//    val stu = Basic.Student(null)
    val stu = Student("ysy")
    val str = stu.name ?: fail("Name required")
    println(str)
}

fun fail(msg: String): Nothing {
    throw IllegalArgumentException(msg)
}

fun testNotation() {
    break_1@ for (i in 1..10) {
        break_2@ for (j in 1..20) {
            println("i:" + i.toString() + " j:" + j)
            if (i + j == 24) {
                println("break")
                break@break_1
                // 经典语言中在双层循环中，break语句默认跳出内循环，Kotlin中可以通过标签控制跳至任意层
            }
        }
    }

    continue_1@ for (i in 1..10) {
        continue_2@ for (j in 1..20) {
            println("i:" + i.toString() + " j:" + j)
            if (i + j == 24) {
                println("continue")
                continue@continue_1
                // 经典语言中在双层循环中，continue语句默认继续走内循环，Kotlin中可以通过标签控制走任意层
                // 此处跳到外层，相当于Java中在内层break
            }
        }
    }
}

fun testReturn(): Int {
    val list = arrayOf(1, 2, 3, 4, 5, 6)
    list.forEach lit@ {
        if (it == 3)
            return@lit // 经典语言中在函数内return会直接返回到外部，Kotlin中可以通过标签系统在内部函数中灵活返回
        println(it)
    }
    println("等价写法1")
    list.forEach {
        if (it == 3)
            return@forEach // 使用隐式标签更方便
        println(it)
    }
    println("等价写法2")
    list.forEach(fun(value: Int) {
        if (value == 3)
            return
        println(value)
    })
    return@testReturn 123 // 也可返回一个值
}

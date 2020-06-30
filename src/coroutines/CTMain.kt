package coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun main1() {
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        println("World!") // 在延迟后打印输出
    }
    println("Hello,") // 协程已在等待时主线程还在继续
    Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
}

fun main2() = runBlocking {
    launch { // 在后台启动一个新的协程并继续
        delay(1000L)
        println("World!")
    }
    println("Hello,") // 主协程在这里会立即执行
    delay(2000L)      // 延迟 2 秒来保证 JVM 存活
}

fun main3() = runBlocking {
    val job = launch { // 启动一个新协程并保持对这个作业的引用
        doWorld()
    }
    println("Hello,")
    job.join() // 等待直到子协程执行结束
    // job.cancelAndJoin();
}

// 这是你的第一个挂起函数
suspend fun doWorld() {
    delay(3000L)
    println("World!")
}

// 注意，在这个示例中我们在 `main` 函数的右边没有加上 `runBlocking`
fun main4() = runBlocking {
    val time = measureTimeMillis {
        // 我们可以在协程外面启动异步执行
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        // 但是等待结果必须调用其它的挂起或者阻塞
        // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
        runBlocking {
            println("The answer is ${one.await() + two.await()}")
        }
    }
    println("Completed in $time ms")
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // 假设我们在这里做了一些有用的事
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // 假设我们在这里也做了一些有用的事
    return 29
}

fun main5() = runBlocking<Unit> {
    launch(Dispatchers.Unconfined) { // 非受限的——将和主线程一起工作
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
    launch { // 父协程的上下文，主 runBlocking 协程
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }
}

fun main6() {
    sequence { // 序列构建器
        for (i in 1..3) {
            Thread.sleep(1000) // 假装我们正在计算
            yield(i) // 产生下一个值
        }
    }.forEach { value -> println(value) }
}

fun main() = runBlocking {
    // 启动并发的协程以验证主线程并未阻塞
    launch {
        for (k in 1..3) {
            println("I'm not blocked $k")
            delay(500)
        }
    }
    // 收集这个流
    flow {
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }.collect { value ->
        println(value)
        println("${Thread.currentThread()}")
    }

    (1..4).asFlow()
            .map { req -> performRequest(req) }
            .take(3)
            .collect { resp -> println(resp) }

    val sum = (1..5).asFlow()
            .map { it * it }
            .reduce { lastSum, it -> lastSum + it }
    println(sum)
}

suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}
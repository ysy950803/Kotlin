package coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors
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

fun main7() = runBlocking {
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
        .flowOn(Dispatchers.Default)
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

fun main8() = runBlocking {
    var time = measureTimeMillis {
        foo().buffer().collect { value ->
            if (value == 1) {
                delay(200 * 4) // 假装我们花费 800 毫秒来处理它
            }
            println(value)
        }
    }
    println("Collected in $time ms") // about 1000ms

    time = measureTimeMillis {
        foo().conflate().collect {
            delay(200 * 4)
            println(it)
        }
    }
    println("Collected in $time ms")

    time = measureTimeMillis {
        foo().collectLatest { value -> // 取消并重新发射最后一个值
            println("Collecting $value")
            delay(200 * 4)
            println("Done $value")
        }
    }
    println("Collected in $time ms")
}

fun foo(): Flow<Int> = flow {
    for (i in 1..4) {
        delay(200) // 假装我们异步等待了 200 毫秒
        emit(i) // 发射下一个值
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
fun main9() = runBlocking<Unit> {
    var nums = (1..4).asFlow()
    var strs = flowOf("one", "two", "three", "four")
    nums.zip(strs) { n, s -> "$n is $s" }.collect { println(it) }
    println()

    nums = nums.onEach { delay(300) }
    strs = strs.onEach { delay(400) }
    var startTime = System.currentTimeMillis()
    // 其中，nums 或 strs 流中的每次发射都会打印一行
    nums.combine(strs) { a, b -> "$a -> $b" }.collect {
        println("$it at ${System.currentTimeMillis() - startTime} ms from start")
    }
    println()

    startTime = System.currentTimeMillis()
    nums.onEach { delay(100) }
        .flatMapConcat { requestFlow(it) }
        .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
    println()
    startTime = System.currentTimeMillis()
    nums.onEach { delay(100) }
        .flatMapMerge { requestFlow(it) }
        .collect { println("$it at ${System.currentTimeMillis() - startTime} ms from start") }
    println()

    nums.onEach { check(it <= 2) { "Collected $it" } }
        .map { println(it) }
        .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
        .catch { cause -> println("Caught $cause") }
        .launchIn(this)
}

fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // 等待 500 毫秒
    emit("$i: Second")
}

@ExperimentalCoroutinesApi
fun main10() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) channel.send(x * x)
        channel.close() // 我们结束发送
    }
    println("${channel.receive()} by receive")
    // 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
    for (y in channel) println(y)
    println("Done!")

    produceSquares().consumeEach { println(it) }

    var cur = numbersFrom(2)
    repeat(10) {
        val prime = cur.receive()
        println(prime)
        cur = filter(cur, prime)
    }
}

@ExperimentalCoroutinesApi
fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) send(x * x)
}

@ExperimentalCoroutinesApi
fun CoroutineScope.numbersFrom(start: Int) = produce {
    var x = start
    while (true) send(x++) // infinite stream of integers from start
}

@ExperimentalCoroutinesApi
fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce {
    for (x in numbers) if (x % prime != 0) send(x)
}

data class Ball(var hits: Int)

fun main11() = runBlocking {
    val table = Channel<Ball>() // 一个共享的 table（桌子）
    launch { player("ping", table) }
    launch { player("pong", table) }
    table.send(Ball(0)) // 乒乓球
    delay(3000) // 延迟 1 秒钟
    coroutineContext.cancelChildren() // 游戏结束，取消它们
}

suspend fun player(name: String, table: Channel<Ball>) {
    for (ball in table) { // 在循环中接收球
        ball.hits++
        println("$name $ball")
        delay(300) // 等待一段时间
        table.send(ball) // 将球发送回去
    }
}

fun main() {
//    val scope = CoroutineScope(newSingleThreadContext("TEST-1"))
//    scope.launch {
//        delay(1000)
//        println("1 ${Thread.currentThread()}")
//        delay(2000)
//        println("2")
//    }
//    scope.launch {
//        println("3 ${Thread.currentThread()}")
//    }
//    println("4")

    val executor = Executors.newSingleThreadExecutor()
    executor.execute {
        Thread.sleep(1000)
        println("1 ${Thread.currentThread()}")
        Thread.sleep(2000)
        println("2")
    }
    executor.execute {
        println("3 ${Thread.currentThread()}")
    }
    println("4")

    Thread.sleep(10000)
}

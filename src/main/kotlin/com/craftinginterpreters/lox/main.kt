import com.craftinginterpreters.lox.Klox
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    when(args.size) {
        0 -> Klox.runPrompt()
        1 -> Klox.runFile(args[0])
        else -> {
            println("Usage: klox [script]")
            exitProcess(64)
        }
    }
}

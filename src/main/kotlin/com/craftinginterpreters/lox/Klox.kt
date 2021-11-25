package com.craftinginterpreters.lox

import java.io.File
import java.nio.charset.Charset
import kotlin.system.exitProcess

class Klox {
    companion object {
        var hadError = false;

        fun runFile(path: String) {
            run(File(path).readText(Charset.defaultCharset()));

            if (hadError) exitProcess(65)
        }

        fun runPrompt() {
            while(true) {
                print("> ")
                val line = readLine() ?: break
                run(line)
                hadError = false
            }
        }

        fun run(source: String) {
            val scanner = Scanner(source)
            val tokens = scanner.scanTokens()

            for(token in tokens)
                println(token)
        }

        fun error(lineNo: Int, message: String) {
            report(lineNo, "", message)
        }

        private fun report(lineNo: Int, where: String, message: String) {
            System.err.println("[line $lineNo] Error $where: $message")
            hadError = true;
        }
    }
}

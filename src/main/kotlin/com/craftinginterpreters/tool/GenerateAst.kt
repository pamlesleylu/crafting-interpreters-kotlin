package com.craftinginterpreters.tool

import java.io.File
import kotlin.system.exitProcess

class GenerateAst {
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: generate_ast <output directory>")
        exitProcess(64);
    }
    val outputDir = args[0]
    defineAst(outputDir, "Expr", listOf(
        "Binary(val left: Expr, val operator: Token, val right: Expr)",
        "Grouping(val expression: Expr)",
        "Literal(val value: Any)",
        "Unary(val operator: Token, val right: Expr)"
    ))
}

fun defineAst(outputDir: String, baseName: String, types: List<String>) {
    val path = "$outputDir/$baseName.kt"

    File(path).printWriter().use {
        it.println("package com.craftinginterpreters.lox")
        it.println();
        it.println("abstract class $baseName {")
        it.println("  abstract fun <R> accept(visitor: Visitor<R>): R")
        it.println("}")
        it.println()

        defineVisitor(it, baseName, types)
        it.println()

        for (type in types) {
            val typeName = type.split("(")[0];
            it.println("class $type : $baseName() {")
            it.println("  override fun <R> accept(visitor: Visitor<R>): R = visitor.visit$typeName$baseName(this)")
            it.println("}")
        }
    }
}

fun defineVisitor(writer: java.io.PrintWriter, baseName: String, types: List<String>) {
    writer.println("  interface Visitor<R> {")

    for (type in types) {
        val typeName = type.split("(")[0];
        writer.println("    fun visit$typeName$baseName(${baseName.toLowerCase()}: $typeName): R")
    }

    writer.println("}")
}

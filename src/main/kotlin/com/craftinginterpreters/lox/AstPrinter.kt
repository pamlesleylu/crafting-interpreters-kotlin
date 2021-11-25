package com.craftinginterpreters.lox

import kotlin.system.exitProcess

class AstPrinter : Visitor<String> {

    override fun visitBinaryExpr(expr: Binary): String =
        parenthesize(expr.operator.lexeme, expr.right, expr.left)

    override fun visitGroupingExpr(expr: Grouping): String =
        parenthesize("group", expr.expression)

    override fun visitLiteralExpr(expr: Literal): String =
        when (expr.value) {
            null -> "nil"
            else -> expr.value.toString()
        }

    override fun visitUnaryExpr(expr: Unary): String =
        parenthesize(expr.operator.lexeme, expr.right)

    fun print(expr: Expr): String = expr.accept(this);

    private fun parenthesize(name: String, vararg  exprs: Expr): String {
        var builder = java.lang.StringBuilder();

        builder.append("(").append(name);
        for (expr in exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}

fun main(args: Array<String>) {
    val expr = Binary(
        Unary(
            Token(TokenType.MINUS, "-", null, 1),
            Literal(123)
        ),
        Token(TokenType.STAR, "*", null, 1),
        Grouping(Literal(45.67))
    )

    println(AstPrinter().print(expr))
}

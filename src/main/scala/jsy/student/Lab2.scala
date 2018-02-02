package jsy.student

import jsy.lab2.Lab2Like

object Lab2 extends jsy.util.JsyApplication with Lab2Like {
  import jsy.lab2.Parser
  import jsy.lab2.ast._

  /*
   * CSCI 3155: Lab 2
   * Brandon Zink
   * 
   * Partner: Cameron Connor
   * Collaborators: <Any Collaborators>
   */

  /*
   * Fill in the appropriate portions above by replacing things delimited
   * by '<'... '>'.
   * 
   * Replace the '???' expression with  your code in each function.
   * 
   * Do not make other modifications to this template, such as
   * - adding "extends App" or "extends Application" to your Lab object,
   * - adding a "main" method, and
   * - leaving any failing asserts.
   * 
   * Your lab will not be graded if it does not compile.
   * 
   * This template compiles without error. Before you submit comment out any
   * code that does not compile or causes a failing assert. Simply put in a
   * '???' as needed to get something  that compiles without error. The '???'
   * is a Scala expression that throws the exception scala.NotImplementedError.
   *
   */

  /* We represent a variable environment as a map from a string of the
   * variable name to the value to which it is bound.
   * 
   * You may use the following provided helper functions to manipulate
   * environments, which are just thin wrappers around the Map type
   * in the Scala standard library.  You can use the Scala standard
   * library directly, but these are the only interfaces that you
   * need.
   */



  /* Some useful Scala methods for working with Scala values include:
   * - Double.NaN
   * - s.toDouble (for s: String)
   * - n.isNaN (for n: Double)
   * - n.isWhole (for n: Double)
   * - s (for n: Double)
   * - s format n (for s: String [a format string like for printf], n: Double)
   *
   * You can catch an exception in Scala using:
   * try ... catch { case ... => ... }
   */

  def toNumber(v: Expr): Double = {
    require(isValue(v))
    (v: @unchecked) match {
      case N(n) => n
      case B(b) => if(b) 1.0 else 0.0
      case S(s) => s match {
        case "" => 0.0
        case _ => try {s.toDouble} catch {case _:NumberFormatException => Double.NaN}
      }
      case _ => Double.NaN
    }
  }

  def toBoolean(v: Expr): Boolean = {
    require(isValue(v))
    (v: @unchecked) match {
      case B(b) => b
      case N(n) => if(n == 0 || n == Double.NaN) false else true
      case S(s) => if(s.equals("")) false else true
      case Undefined => false
    }
  }

  def toStr(v: Expr): String = {
    require(isValue(v))
    (v: @unchecked) match {
      case S(s) => s
      case B(b) => b.toString
      case N(n) => prettyNumber(n)
      case Undefined => "undefined"
    }
  }

  def eval(env: Env, e: Expr): Expr = e match {
    /* Base Cases */
    case v if isValue(v) => v
    case Var(s) => lookup(env, s)
    /* Inductive Cases */
    // Unary operators
    case Unary(Neg, e1) => N(-toNumber(eval(env, e1)))
    case Unary(Not, e1) => B(!toBoolean(eval(env, e1)))
    // Arithmetic operators
    case Binary(Minus, e1, e2) => N(toNumber(eval(env, e1)) - toNumber(eval(env, e2)))
    case Binary(Times, e1, e2) => N(toNumber(eval(env, e1)) * toNumber(eval(env, e2)))
    case Binary(Div, e1, e2) => N(toNumber(eval(env, e1)) / toNumber(eval(env, e2)))
    // Extra cases to handle string concatenation
    case Binary(Plus, e1, e2) => (eval(env, e1), eval(env, e2)) match {
      case (S(str), _) => S(str + toStr(e2))
      case (_, S(str)) => S(toStr(e1) + str)
      case (_, _) => N(toNumber(eval(env, e1)) + toNumber(eval(env, e2)))
    }
    // Equalities / Inequalities
    case Binary(Eq, e1, e2) => if(eval(env, e1) == eval(env, e2)) B(true) else B(false)
    case Binary(Ne, e1, e2) => if(eval(env, e1) == eval(env, e2)) B(false) else B(true)
    // FIXME lexicographical comparison of strings
    case Binary(Lt, e1, e2) => if(toNumber(eval(env, e1)) < toNumber(eval(env, e2))) B(true) else B(false)
    case Binary(Le, e1, e2) => if(toNumber(eval(env, e1)) <= toNumber(eval(env, e2))) B(true) else B(false)
    case Binary(Gt, e1, e2) => if(toNumber(eval(env, e1)) > toNumber(eval(env, e2))) B(true) else B(false)
    case Binary(Ge, e1, e2) => if(toNumber(eval(env, e1)) >= toNumber(eval(env, e2))) B(true) else B(false)
    // Boolean Operators
    case Binary(And, e1, e2) => if(toBoolean(eval(env, e1))) eval(env, e2) else eval(env, e1)
    case Binary(Or, e1, e2) => if(toBoolean(eval(env, e1))) eval(env, e1) else eval(env, e2)
    case Binary(Seq, e1, e2) => eval(env, e1); eval(env, e2)
    // Conditional
    case If(e1, e2, e3) => if(toBoolean(eval(env, e1))) eval(env, e2) else eval(env, e3)
    // Print, Constant Declaration
    case Print(e1) => println(pretty(eval(env, e1))); Undefined
    case ConstDecl(x, e1, e2) => eval(extend(env, x, eval(env, e1)), e2)
    case _ => Undefined
  }

  /* Interface to run your interpreter from the command-line.  You can ignore what's below. */
  def processFile(file: java.io.File) {
    if (debug) { println("Parsing ...") }

    val expr = Parser.parseFile(file)

    if (debug) {
      println("\nExpression AST:\n  " + expr)
      println("------------------------------------------------------------")
    }

    if (debug) { println("Evaluating ...") }

    val v = eval(expr)

     println(pretty(v))
  }

}

package com.persist

import Dims._
import Integers._
import scala.reflect.ClassTag

case class U[m1 <: Meters, g1 <: Grams, s1 <: Seconds, a1 <: Amperes, b1 <: Bits, d1 <: Dollars](private val v: Double) {

  type typ = U[m1,g1,s1,a1,b1,d1]

  def apply(v1: Double) = U[m1, g1, s1, a1, b1, d1](v * v1)

  def +(other: U[m1, g1, s1, a1, b1, d1]) = {
    U[m1, g1, s1, a1, b1, d1](v + other.v)
  }

  def -(other: U[m1, g1, s1, a1, b1, d1]) = {
    U[m1, g1, s1, a1, b1, d1](v - other.v)
  }

  def in(other: U[m1, g1, s1, a1, b1, d1]) = {
    v / other.v
  }

  def *[m2 <: Integer, g2 <: Integer, s2 <: Integer, a2 <: Amperes, b2 <: Bits, d2 <: Dollars](other: U[m2, g2, s2, a2, b2, d2]) = {
    U[m2#inc[m1], g2#inc[g1], s2#inc[s1], a2#inc[a1], b2#inc[b1], d2#inc[d1]](v * other.v)
  }

  def /[m2 <: Integer, g2 <: Integer, s2 <: Integer, a2 <: Amperes, b2 <: Bits, d2 <: Dollars](other: U[m2, g2, s2, a2, b2, d2]) = {
    U[m2#dec[m1], g2#dec[g1], s2#dec[s1], a2#dec[a1], b2#dec[b1], d2#dec[d1]](v / other.v)
  }

  private def dim(name: String, s0: String) = {
    val s1 = s0.toString().split("[.]").last
    val s2 = s1 match {
      case "PosTooBig" => "+++"
      case "NegToBig" => "---"
      case "Z" => "0"
      case s: String =>
        val v = s.substring(1)
        if (s.startsWith("P")) v else "-" + v
    }
    s"$name:$s2"
  }

  override def toString() = {
    f"$v:U"
  }

  def show(implicit mt: ClassTag[m1], gt: ClassTag[g1], st: ClassTag[s1], at: ClassTag[a1], bt: ClassTag[b1], dt: ClassTag[d1]) = {
    val dims = Seq(dim("meter", mt.toString), dim("gram", gt.toString), dim("second", st.toString),
      dim("ampere", at.toString()), dim("bit", bt.toString()), dim("dollar", dt.toString()))
    val dims1 = dims.filter(!_.endsWith(":0")).mkString(",")
    f"$v:[$dims1]"
  }

  def print(implicit mt: ClassTag[m1], gt: ClassTag[g1], st: ClassTag[s1], at: ClassTag[a1], bt: ClassTag[b1], dt: ClassTag[d1]) = {
    println(show)
  }

}

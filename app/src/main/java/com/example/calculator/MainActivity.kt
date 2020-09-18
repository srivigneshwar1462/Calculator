package com.example.calculator

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.fragment.app.Fragment as Fragment1


class MainActivity : AppCompatActivity() {
    private var lastDigit=false
    private var lastDot=false
    private var lastOperator=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnBackspace.setOnLongClickListener {
            tvText.text=""
            return@setOnLongClickListener true
        }
    }

    private fun vibe(){
        val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratorService.vibrate(50)
    }
    fun onOperator(view: View){
        vibe()

        if(isOperatorAdded()&&tvText.text.isNotEmpty()) {
            tvText.append((view as Button).text)
            lastOperator = true
            lastDigit = false
            lastDot = false
        }else if(tvText.text.isEmpty()&&((view as Button).text.toString()=="-")){
            tvText.append("-")
            lastOperator=true
            lastDigit = false
            lastDot = false
        }

    }

 
    fun onDigit(view: View){
        vibe()
        checkLength()
        if(tvText.text.isEmpty() && ((view as Button).text) == "0"){
            tvText.text=""
            lastDigit=false
            Toast.makeText(this,"0 Entered first",Toast.LENGTH_LONG).show()
        }else {
            tvText.append((view as Button).text)
            lastDigit = true
            lastDot=false
            lastOperator=false
        }
    }
    fun onDot(view: View){

        vibe()
        if(!lastDot&&!tvText.text.contains('.')){
            tvText.append(".")
            lastDot=true
        }

    }
    fun onClear(view: View){

        vibe()
        tvText.text=""
        lastDot=false
        lastOperator=false
        lastDigit=false

    }
    fun onBackspace(view: View){
        vibe()
        var input=tvText.text
        if (input.isNotEmpty()){
            var newinput=input.substring(0,input.length-1)
            tvText.text=newinput
            if (newinput.isNotEmpty()) {
                var lastindex:Int=newinput.lastIndex
                val lastchar: Char = newinput[lastindex]
                if (lastchar == '+' || lastchar == '-' || lastchar == '*' || lastchar == '/') {
                    lastOperator = true
                    lastDigit=false
                    lastDot=false
                } else if (lastchar == '.') {
                    lastDot = true
                    lastDigit=false
                    lastOperator=false
                } else {
                    lastDigit = true
                    lastDot=false
                    lastOperator=true
                }
            }
        }

    }
    fun onEqual(view: View){
        vibe()
        if (lastDigit){
            var input =tvText.text.toString()
            var prefix=""
            try {
                if(input.startsWith("-")){
                    prefix="-"
                    input=input.substring(1)
                }
                if (input.contains("+")){
                    var split =input.split("+")
                    var one =split[0]
                    var two=split[1]
                    if (prefix.isNotEmpty()){
                        one=prefix+one
                    }
                    var result =one.toDouble()+two.toDouble()
                    tvText.text=removeZero(result.toString())
                }else if (input.contains("-")){
                    var split =input.split("-")
                    var one =split[0]
                    var two=split[1]
                    if (prefix.isNotEmpty()){
                        one=prefix+one
                    }
                    var result =one.toDouble()-two.toDouble()
                    tvText.text=removeZero(result.toString())
                }else if (input.contains("*")){
                    var split =input.split("*")
                    var one =split[0]
                    var two=split[1]
                    if (prefix.isNotEmpty()){
                        one=prefix+one
                    }
                    var result =one.toDouble()*two.toDouble()
                    tvText.text=removeZero(result.toString())
                }else if (input.contains("/")){
                    var split =input.split("/")
                    var one =split[0]
                    var two=split[1]
                    if (prefix.isNotEmpty()){
                        one=prefix+one
                    }
                    var result =one.toDouble()/two.toDouble()
                    tvText.text=removeZero(result.toString())
                }
            }catch (e:ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    private fun checkLength(){
        var input =tvText.length()
        if(input>=11){
            Toast.makeText(this,"Only 12 digits are allowed",Toast.LENGTH_LONG).show()
        }
    }
    private fun isOperatorAdded() :Boolean{

        if (tvText.text.startsWith("-")){
            return true
        }
        if (tvText.text.contains("+")||tvText.text.contains("-")||tvText.text.contains("*")||tvText.text.contains("/")){
            return false
        }
        return true

    }
    private fun removeZero(ans:String): String {
        if (ans.endsWith(".0")) {
            return ans.removeSuffix(".0")
        }
        return ans
    }

}
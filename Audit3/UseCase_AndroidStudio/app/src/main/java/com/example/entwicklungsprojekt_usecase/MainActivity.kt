package com.example.entwicklungsprojekt_usecase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import android.widget.MediaController
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    lateinit var controlView: TextView
    private lateinit var editText: EditText
    private lateinit var button: Button
    companion object {
         var profile: Profile? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView1)
        controlView = findViewById(R.id.controlView)
        editText = findViewById(R.id.editText)
        button = findViewById(R.id.submitButton1)

        main()

    }


    class Profile (private val profileName: String) {

        var goals = mutableListOf<Goal>()

        fun createGoal(textView: TextView, editText: EditText, button: Button, callback: () -> Unit) {
                textView.text = "Bitte gib ein konkretes Ziel ein, das du erreichen möchtest:"
                button.setOnClickListener {
                    var name = editText.text.toString()
                    if (name.isEmpty()) {
                        textView.text = "Ungültige Eingabe. Bitte versuche es erneut."
                    } else {
                        goals.add(Goal(name))
                        editText.text.clear()
                        textView.text = "Super, ${profile?.profileName}! Wir haben dein Ziel gespeichert."
                        callback()
                    }
            }
        }


        companion object {
            fun createProfile(textView: TextView, editText: EditText, button: Button, callback: () -> Unit) {
                textView.text = "Hallo! Willkommen bei App-Name. Wie dürfen wir dich nennen?"
                button.setOnClickListener {
                    val name = editText.text.toString()
                    if (name.isEmpty()) {
                        textView.text = "Ungültige Eingabe. Bitte versuche es erneut."
                    } else {
                        profile = Profile(name)
                        editText.text.clear()
                        textView.text = "Herzlich Willkommen, $name!"
                        callback()
                    }
                }

            }
        }

    }


    class Behavior(val name: String, var efficiency: Int = 0, var ease: Int = 0)

    class Goal(val name: String) {
        val behaviors = mutableListOf<Behavior>()
        var goldenBehaviors = mutableListOf<Behavior>()
        val recipe = createRecipe()

        fun createBehavior(textView: TextView, editText: EditText, button: Button, callback: () -> Unit) {
            var count = 0
            textView.text = "Definiere ein Verhalten und gib ihm einen passenden Namen:"
            editText.hint = ""
            button.setOnClickListener(){
                var name = editText.text.toString()
                if (name.isEmpty()) {
                    textView.text = "Ungültige Eingabe. Bitte versuche es erneut."
                } else {
                    if(count < 5) {
                        behaviors.add(Behavior(name))
                        textView.text = "Das Verhalten wurde hinzugefügt." +
                                "Füge ein weiteres Verhalten hinzu."
                        count++
                        editText.text.clear()
                    } else {
                        callback()
                    }
                }
            }


        }

        /*
        suspend fun setEfficiencyAndEase(textView: TextView, editText: EditText, button: Button, callback: () -> Unit) {
                var i = 0
                while (i < behaviors.size){
                    setEfficiency(i, textView, editText, button)
                    setEase(i, textView, editText, button)
                    i++
                }
            textView.text =
                "Super! Jetzt haben wir alle Eingaben, die wir brauchen und können deine \"Golden Behaviors\" ermitteln."
        }
        *?
         */

        fun setEfficiency(textView: TextView, editText: EditText, button: Button) {
            for (element in behaviors){
                textView.text = "Wie schätzt du die Effektivität des Verhaltens \"${element.name}\" in Bezug auf die Erreichung deines Ziels ein? " +
                        "Gib eine Zahl von 1 bis 10 ein, wobei 1 für \"äußerst ineffektiv\" und 10 für \"sehr effektiv\" steht."
                button.setOnClickListener {
                    val input = editText.text.toString()
                    if (input.isNotEmpty() && input.toInt() in 1..10) {
                        element.efficiency = input.toInt()
                        editText.text.clear()
                    } else {
                        textView.text = "Ungültige Eingabe. Bitte versuche es erneut."
                    }
                }
            }


        }

        private suspend fun setEase(i: Int, textView: TextView, editText: EditText, button: Button) {
            textView.text =
                    "Wie leicht fällt dir das Verhalten \"${behaviors[i].name}\" auf einer Skala von 1 bis 10? " +
                            "1 steht dabei für \"äußerst schwer\" und 10 für \"sehr einfach\"."
                button.setOnClickListener {
                    val input = editText.text.toString()
                    if (input.isNotEmpty() && input.toInt() in 1..10) {
                        behaviors[i].ease = input.toInt()
                    } else {
                        textView.text = "Ungültige Eingabe. Bitte versuche es erneut."
                    }
                }
            }



        fun getGoldenBehaviors(textView: TextView, editText: EditText, button: Button) {
            behaviors.sortByDescending { it.efficiency }
            textView.text =
                "Die effektivsten Verhaltensweisen zur Erreichung deines Ziels sind ${behaviors[0].name}, ${behaviors[1].name} und ${behaviors[2].name}"

            behaviors.sortByDescending { it.ease }
            textView.text =
                "Die für dich leichtesten Verhaltensweisen sind ${behaviors[0].name}, ${behaviors[1].name} und ${behaviors[2].name}"

            //muss noch überarbeitet werden
            //Behavior-Liste soll erhalten bleiben, damit darauf in der Review-Phase zurückgegriffen werden kann
            // das Kriterium >5 entspricht der Fogg'schen Focus Map
            goldenBehaviors = behaviors.filter { it.efficiency > 5 && it.ease > 5 }.toMutableList()
            goldenBehaviors.sortedByDescending { it.ease }
            goldenBehaviors.sortedByDescending { it.efficiency }


            //es werden je nach Verfügbarkeit
            when (goldenBehaviors.size) {
                3 -> textView.text =
                    "Deine Golden Behaviors sind ${goldenBehaviors[0].name}, ${goldenBehaviors[1].name} und ${goldenBehaviors[2].name}."
                2 -> textView.text =
                    "Deine Golden Behaviors sind ${goldenBehaviors[0].name}, ${goldenBehaviors[1].name} und ${goldenBehaviors[2].name}."
                1 -> textView.text =
                    "Wir konnten nur 1 \"Golden Behavior\" identifizieren. Es lautet: ${goldenBehaviors[0].name}."
                0 -> textView.text = "Leider hat keines deiner gesammelten Verhaltensweisen "
            }
        }


        private fun createRecipe() {}




    }


    private fun main() {

            Profile.createProfile(textView, editText, button){
                profile?.createGoal(textView, editText, button){
                    profile?.goals?.get(0)?.createBehavior(textView, editText, button){
                        profile?.goals!![0]?.setEfficiency(textView, editText, button)

                        }
                    }
                }

        }



}















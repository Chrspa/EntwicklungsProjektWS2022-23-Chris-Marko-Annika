
class Profile (val profileName: String){
    //goals werden direkt im angelegten Profil gespeichert und können mit der Funktion createGoal erstellt werden.
    // Es ist möglich mehrere goals anzulegen, die jeweils eigene behaviors, goldenBehaviors und recipes haben
    var goals = mutableListOf<Goal>()

    //hier wird ein goal zu einem Profil erstellt.
    // Durch die while-Schleife und if/else-Abfrage wird sichergestellt, dass der User einen String eingibt
    fun createGoal() {
            println("Bitte gib ein konkretes Ziel ein, das du erreichen möchtest:")
            val name = readLine()
            if (name is String){
                //das neue Ziel wird der zum Profil gehörenden Liste an goals hinzugefügt
                goals.add(Goal(name))
                println("Super, $profileName! Wir haben dein Ziel gespeichert.")
                return
            }
            else {
                println("Ungültige Eingabe. Bitte versuche es erneut.")
            }
    }


    //das companion object sorgt dafür, dass die Funktion "createProfile" zur Klasse "Profile" gehört
    //die Funktion kann auf dem Klassennamen aufgerufen werden und benötigt keine Instanz der Klasse
    companion object {
        fun createProfile() : Profile{
            while (true) {
                println("Hallo! Willkommen bei App-Name. Wie dürfen wir dich nennen?")
                val name = readLine()
                if (name is String){
                    return Profile(name)
                }
                else {
                    println("Ungültige Eingabe. Bitte versuche es erneut.")
                }
            }
        }
    }
}


//ein Recipe besteht aus prompt, behavior und einer celebration. Jedes goal hat eine Liste mit möglichen recipes (Achtung: nicht zu viele (aktive)!)
class Recipe (val behavior: Behavior, var celebration: String?, var prompt: String?){

    //Das companion object ordnet die Funktion "findWayToCelebrate" der Klasse "Recipe" zu und kann ohne Instanz aufgerufen werden (-> Recipe.findWayToCelebrate())
    companion object{
        //Default-Wert gesetzt
        var wayToCelebrate = "Klatsche drei Mal in die Hände und lächle dabei!"
        //Liste mit möglichen Vorschlägen muss noch erstellt werden. Es sollten auch Vorschläge vom User hinzugefügt werden können
        val waysToCelebrate = mutableListOf<String>()

        fun findWayToCelebrate(){
            println("Um das Auftreten des Verhaltens zum Zeitpunkt des Prompts zu festigen, solltest du jeden Erfolg feiern!" +
            "Das kannst du auf viele verschiedene Weisen tun. Ein Weg könnte beispielsweise sein: $wayToCelebrate")
        }
    }
}

class Behavior(val name: String, var efficiency: Int = 0, var ease: Int = 0)

class Goal(val name: String){
    val behaviors = mutableListOf<Behavior>()
    var goldenBehaviors = mutableListOf<Behavior>()
    val recipe = createRecipe()

    //hier wird ein Verhalten erstellt und der Liste an Verhaltensweisen zugeordnet, die zu einem Ziel gehört. Das Ziel gehört wiederum zu einem Profil.
    // Durch die while-Schleife und if/else-Abfrage wird sichergestellt, dass der User einen String eingibt
    fun createBehavior(){
        while (true) {
            println("Definiere ein Verhalten und gib ihm einen passenden Namen:")
            val name = readLine()
            if (name is String){
                //behavior wird der Liste an behaviors hinzugefügt
                this.behaviors.add(Behavior(name))
                return
            }
            else {
                println("Ungültige Eingabe. Bitte versuche es erneut.")
            }
        }
    }


    //Die Bestimmung von Effektivität und Leichtigkeit sind in einzelnen Funktionen ausgelagert
    //Für jedes Verhalten in der Liste werden zunächst beide Elemente bestimmt
    //durch i++ wird die nächste Verhaltensweise in der Liste ausgewählt
    fun setEfficiencyAndEase() {
        var i = 0
        while(i < behaviors.size){
            setEfficiency(i)
            setEase(i)
            i++
        }
        println("Super! Jetzt haben wir alle Eingaben, die wir brauchen und können deine \"Golden Behaviors\" ermitteln.")
    }

    private fun setEfficiency(i: Int){
        while (true) {
            println(
                "Wie schätzt du die Effektivität des Verhaltens \"${behaviors[i].name}\" in Bezug auf die Erreichung deines Ziels ein? " +
                        "Gib eine Zahl von 1 bis 10 ein, wobei 1 für \"äußerst ineffektiv\" und 10 für \"sehr effektiv\" steht."
            )
            val input = readLine()
            if (input != null && input.toInt() in 1..10) {
                behaviors[i].efficiency = input.toInt()
                return
            }
            else {
                println("Ungültige Eingabe. Bitte versuche es erneut.")
            }
        }
    }
        private fun setEase(i: Int){
            while (true) {
                println(
                    "Wie leicht fällt dir das Verhalten \"${behaviors[i].name}\" auf einer Skala von 1 bis 10? " +
                            "1 steht dabei für \"äußerst schwer\" und 10 für \"sehr einfach\"."
                )
                val input = readLine()
                if (input != null && input.toInt() in 1..10) {
                    behaviors[i].ease = input.toInt()
                    return
                }
                else {
                    println("Ungültige Eingabe. Bitte versuche es erneut.")
                }
            }
        }

    fun getGoldenBehaviors(){
        behaviors.sortByDescending { it.efficiency }
        println("Die effektivsten Verhaltensweisen zur Erreichung deines Ziels sind ${behaviors[0].name}, ${behaviors[1].name} und ${behaviors[2].name}")

        behaviors.sortByDescending { it.ease }
        println("Die für dich leichtesten Verhaltensweisen sind ${behaviors[0].name}, ${behaviors[1].name} und ${behaviors[2].name}")

        //muss noch überarbeitet werden
        //Behavior-Liste soll erhalten bleiben, damit darauf in der Review-Phase zurückgegriffen werden kann
        // das Kriterium >5 entspricht der Fogg'schen Focus Map
        goldenBehaviors = behaviors.filter { it.efficiency > 5 && it.ease > 5 }.toMutableList()
        goldenBehaviors.sortedByDescending { it.ease }
        goldenBehaviors.sortedByDescending { it.efficiency }


        //es werden je nach Verfügbarkeit
        when(goldenBehaviors.size) {
            3 -> println("Deine Golden Behaviors sind ${goldenBehaviors[0].name}, ${goldenBehaviors[1].name} und ${goldenBehaviors[2].name}.")
            2 -> println("Deine Golden Behaviors sind ${goldenBehaviors[0].name}, ${goldenBehaviors[1].name} und ${goldenBehaviors[2].name}.")
            1 -> println("Wir konnten nur 1 \"Golden Behavior\" identifizieren. Es lautet: ${goldenBehaviors[0].name}.")
            0 -> println("Leider hat keines deiner gesammelten Verhaltensweisen ")
        }

    }

    fun createRecipe(){}
}




fun main() {

    val newProfile = Profile.createProfile()
    newProfile.createGoal()

    //Testlauf: am Ende sollte der User natürlich selbst bestimmen können, wie viele Verhaltensweisen er festlegen möchte.
    println("Wir möchten nun passende Verhaltensweisen zu deinem Ziel finden! Legen wir los!" +
            "Gib bitte 5 Verhaltensweisen ein, dann geht es weiter.")

    newProfile.goals[0].createBehavior()
    newProfile.goals[0].createBehavior()
    newProfile.goals[0].createBehavior()
    newProfile.goals[0].createBehavior()
    newProfile.goals[0].createBehavior()


    newProfile.goals[0].setEfficiencyAndEase()

    newProfile.goals[0].getGoldenBehaviors()

}
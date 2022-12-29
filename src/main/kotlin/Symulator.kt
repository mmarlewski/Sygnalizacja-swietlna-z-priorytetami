import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

object Symulator
{
    var upLight = Light.Yellow
    var downLight = Light.Yellow
    var sideLight = Light.Yellow

    var upCars = 4
    var downCars = 3
    var sideCars = 5

    var isMainCar = false
    var isSideCar = false

    var areCarsMoving = false

    @JvmStatic
    fun main(args : Array<String>)
    {
        Thread { start() }.start()
    }

    fun start()
    {
        println("Starting symulator...")

        val socket = Socket("127.0.0.1", 9999)
        val output = PrintWriter(socket.getOutputStream(), true)
        val input = BufferedReader(InputStreamReader(socket.inputStream))

        println("Started symulator")

        while (true)
        {
            Thread.sleep(2000)

            val messageFromSterownik = Json.decodeFromString<MessageFromSterownik>(input.readLine())

            areCarsMoving = (upLight == messageFromSterownik.mainLight &&
                             downLight == messageFromSterownik.mainLight &&
                             sideLight == messageFromSterownik.sideLight)

            upLight = messageFromSterownik.mainLight
            downLight = messageFromSterownik.mainLight
            sideLight = messageFromSterownik.sideLight

            if (areCarsMoving)
            {
                changeCars()
            }

            val messageToSterownik = Json.encodeToString(MessageToSterownik(isMainCar, isSideCar))
            output.println(messageToSterownik)

            printMap()
        }

        //socket.close()
    }

    fun changeCars()
    {
        when
        {
            upLight == Light.Green && downLight == Light.Green && sideLight == Light.Red ->
            {
                if (upCars > downCars)
                {
                    if (upCars > 0)
                    {
                        upCars--
                    }
                }
                else
                {
                    if (downCars > 0)
                    {
                        downCars--
                    }
                }
            }
            upLight == Light.Red && downLight == Light.Red && sideLight == Light.Green   ->
            {
                if (sideCars > 0)
                {
                    sideCars--
                }
            }
        }

        isMainCar = (upCars > 0 || downCars > 0)

        isSideCar = (sideCars > 0)
    }

    fun printMap()
    {
        val upCars1 = if (upCars >= 1) "O" else " "
        val upCars2 = if (upCars >= 2) "O" else " "
        val upCars3 = if (upCars >= 3) "O" else " "
        val upCars4 = if (upCars >= 4) "O" else " "
        val upCars5 = if (upCars >= 5) "O" else " "

        val downCars1 = if (downCars >= 1) "O" else " "
        val downCars2 = if (downCars >= 2) "O" else " "
        val downCars3 = if (downCars >= 3) "O" else " "
        val downCars4 = if (downCars >= 4) "O" else " "
        val downCars5 = if (downCars >= 5) "O" else " "

        val sideCars1 = if (sideCars >= 1) "O" else " "
        val sideCars2 = if (sideCars >= 2) "O" else " "
        val sideCars3 = if (sideCars >= 3) "O" else " "
        val sideCars4 = if (sideCars >= 4) "O" else " "
        val sideCars5 = if (sideCars >= 5) "O" else " "

        val upLightRed = if (upLight == Light.Red || upLight == Light.RedYellow) "X" else " "
        val upLightYellow = if (upLight == Light.Yellow || upLight == Light.RedYellow) "X" else " "
        val upLightGreen = if (upLight == Light.Green) "X" else " "

        val downLightRed = if (downLight == Light.Red || downLight == Light.RedYellow) "X" else " "
        val downLightYellow = if (downLight == Light.Yellow || downLight == Light.RedYellow) "X" else " "
        val downLightGreen = if (downLight == Light.Green) "X" else " "

        val sideLightRed = if (sideLight == Light.Red || sideLight == Light.RedYellow) "X" else " "
        val sideLightYellow = if (sideLight == Light.Yellow || sideLight == Light.RedYellow) "X" else " "
        val sideLightGreen = if (sideLight == Light.Green) "X" else " "

        println("##################")
        println(".┌─┐......║ $upCars5 ║.┌┴┐.")
        println(".│$sideLightRed│......║ $upCars4 ║.│$upLightRed│.")
        println(".│$sideLightYellow│......║ $upCars3 ║.│$upLightYellow│.")
        println(".│$sideLightGreen│......║ $upCars2 ║.│$upLightGreen│.")
        println(".└┬┘......║ $upCars1 ║.└─┘.")
        println("──────────╜   ║.....")
        println("$sideCars5 $sideCars4 $sideCars3 $sideCars2 $sideCars1 │   ║.....")
        println("──────────╖   ║.....")
        println("..........║ $downCars1 ║.┌─┐.")
        println("..........║ $downCars2 ║.│$downLightRed│.")
        println("..........║ $downCars3 ║.│$downLightYellow│.")
        println("..........║ $downCars4 ║.│$downLightGreen│.")
        println("..........║ $downCars5 ║.└┬┘.")
        println("##################")
    }
}

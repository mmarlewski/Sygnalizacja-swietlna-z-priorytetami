import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

object Sterownik
{
    var mainLight = Light.Red
    var sideLight = Light.Green

    var isLightChanging = false
    var isMainLightChanging = false

    var isMainCar = false
    var isSideCar = false

    var wasMainCarOnce = false
    var wasMainCarTwice = false
    var wasSideCarOnce = false

    @JvmStatic
    fun main(args : Array<String>)
    {
        Thread { start() }.start()
    }

    fun start()
    {
        println("Starting sterownik...")

        val server = ServerSocket(9999)
        val client = server.accept()
        val output = PrintWriter(client.getOutputStream(), true)
        val input = BufferedReader(InputStreamReader(client.inputStream))

        println("Started sterownik")

        while (true)
        {
            Thread.sleep(2000)

            if (!isLightChanging)
            {
                changeCars()
            }

            if (isLightChanging)
            {
                changeLight()
            }

            val messageFromSterownik = Json.encodeToString(MessageFromSterownik(mainLight, sideLight))
            output.println(messageFromSterownik)

            val messageToSterownik = Json.decodeFromString<MessageToSterownik>(input.readLine())

            isMainCar = messageToSterownik.isMainCar
            isSideCar = messageToSterownik.isSideCar
        }
    }

    fun changeLight()
    {
        if (isMainLightChanging)
        {
            when (mainLight)
            {
                Light.Red       ->
                {
                    mainLight = Light.RedYellow
                }

                Light.RedYellow ->
                {
                    mainLight = Light.Green
                    isLightChanging = false
                }

                Light.Yellow    ->
                {
                    mainLight = Light.Red
                    isLightChanging = false
                }

                Light.Green     ->
                {
                    mainLight = Light.Yellow
                }
            }
        }
        else
        {
            when (sideLight)
            {
                Light.Red       ->
                {
                    sideLight = Light.RedYellow
                }

                Light.RedYellow ->
                {
                    sideLight = Light.Green
                    isLightChanging = false
                }

                Light.Yellow    ->
                {
                    sideLight = Light.Red
                    isLightChanging = false
                }

                Light.Green     ->
                {
                    sideLight = Light.Yellow
                }
            }
        }
    }

    fun changeCars()
    {
        when
        {
            !wasMainCarOnce && !wasMainCarTwice ->
            {
                when
                {
                    isMainCar ->
                    {
                        when
                        {
                            sideLight == Light.Green ->
                            {
                                isLightChanging = true
                                isMainLightChanging = false
                            }
                            mainLight == Light.Red   ->
                            {
                                isLightChanging = true
                                isMainLightChanging = true
                            }
                            else                     ->
                            {
                                wasMainCarOnce = true
                                //wasMainCarTwice = false
                                wasSideCarOnce = false
                            }
                        }
                    }
                    else      ->
                    {
                        when
                        {
                            mainLight == Light.Green ->
                            {
                                isLightChanging = true
                                isMainLightChanging = true
                            }
                            sideLight == Light.Red   ->
                            {
                                isLightChanging = true
                                isMainLightChanging = false
                            }
                            else                     ->
                            {
                                wasSideCarOnce = true
                                //wasMainCarTwice = false
                                //wasSideCarOnce = false
                            }
                        }
                    }
                }
            }
            wasMainCarOnce && !wasMainCarTwice  ->
            {
                when
                {
                    isMainCar ->
                    {
                        when
                        {
                            sideLight == Light.Green ->
                            {
                                isLightChanging = true
                                isMainLightChanging = false
                            }
                            mainLight == Light.Red   ->
                            {
                                isLightChanging = true
                                isMainLightChanging = true
                            }
                            else                     ->
                            {
                                //wasMainCarOnce = false
                                wasMainCarTwice = true
                                wasSideCarOnce = false
                            }
                        }
                    }
                    else      ->
                    {
                        when
                        {
                            mainLight == Light.Green ->
                            {
                                isLightChanging = true
                                isMainLightChanging = true
                            }
                            sideLight == Light.Red   ->
                            {
                                isLightChanging = true
                                isMainLightChanging = false
                            }
                            else                     ->
                            {
                                wasMainCarOnce = false
                                //wasMainCarTwice = false
                                wasSideCarOnce = true
                            }
                        }
                    }
                }
            }
            wasMainCarOnce && wasMainCarTwice && !wasSideCarOnce   ->
            {
                when
                {

                    isSideCar      ->
                    {
                        when
                        {
                            mainLight == Light.Green ->
                            {
                                isLightChanging = true
                                isMainLightChanging = true
                            }
                            sideLight == Light.Red   ->
                            {
                                isLightChanging = true
                                isMainLightChanging = false
                            }
                            else                     ->
                            {
                                wasMainCarOnce = false
                                wasMainCarTwice = false
                                //wasSideCarOnce = true
                            }
                        }
                    }
                    else ->
                    {
                        when
                        {
                            sideLight == Light.Green ->
                            {
                                isLightChanging = true
                                isMainLightChanging = false
                            }
                            mainLight == Light.Red   ->
                            {
                                isLightChanging = true
                                isMainLightChanging = true
                            }
                            else                     ->
                            {
                                //wasMainCarOnce = false
                                //wasMainCarTwice = false
                                wasSideCarOnce = false
                            }
                        }
                    }
                }
            }
            else                                                   ->
            {
                println("ELSE!!!")
            }
        }
    }
}

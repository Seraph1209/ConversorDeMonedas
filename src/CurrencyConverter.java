import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class CurrencyConverter {
    private static final String API_KEY = "c6b9bddfb12c345cc3afd919";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("Sea bienvenido/a al Conversor de Moneda =]");
            System.out.println("1. Dólar =>> Peso Argentino");
            System.out.println("2. Peso Argentino =>> Dólar");
            System.out.println("3. Dollar =>>  Real Brazileño");
            System.out.println("4. Real Brazileño =>> Dollar");
            System.out.println("5. Dollar  =>> Peso Colombiano");
            System.out.println("6. Peso Colombiano =>> Dollar");
            System.out.println("7. Dólar =>> Peso Mexicano");
            System.out.println("8. Peso Mexicano =>> Dólar");
            System.out.println("9. Salir");
            System.out.print("Elija una opcion valida: ");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    convertCurrency("USD", "ARS", scanner);
                    break;
                case 2:
                    convertCurrency("ARS", "USD", scanner);
                    break;
                case 3:
                    convertCurrency("USD", "BRL", scanner);
                    break;
                case 4:
                    convertCurrency("BRL", "USD", scanner);
                    break;
                case 5:
                    convertCurrency("USD", "COP", scanner);
                    break;
                case 6:
                    convertCurrency("COP", "USD", scanner);
                    break;
                case 7:
                    convertCurrency("USD", "MXN", scanner);
                    break;
                case 8:
                    convertCurrency("MXN", "USD", scanner);
                    break;
                case 9:
                    System.out.println("¡Gracias por su preferencia!");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, elija una opción válida.");
            }
        } while (option != 9);

        scanner.close();
    }

    private static void convertCurrency(String fromCurrency, String toCurrency, Scanner scanner) throws IOException {
        String urlString = BASE_URL + API_KEY + "/latest/" + fromCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            System.out.println("Error: HTTP request failed");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        double conversionRate = parseExchangeRate(response.toString(), toCurrency);

        System.out.print("Ingrese la cantidad de " + fromCurrency + " a convertir: ");
        double amount = scanner.nextDouble();
        double convertedAmount = amount * conversionRate;

        System.out.println(amount + " " + fromCurrency + " equivalen a " + convertedAmount + " " + toCurrency + ".");
    }

    private static double parseExchangeRate(String jsonResponse, String targetCurrency) throws JsonSyntaxException {
        Gson gson = new Gson();
        Map<String, Map<String, Double>> exchangeRates = gson.fromJson(jsonResponse, Map.class);

        // checa si funciono
        if (exchangeRates.containsKey("success") && !exchangeRates.get("success").containsKey(targetCurrency)) {
            throw new RuntimeException("Error retrieving conversion rate for " + targetCurrency);
        }

        // regresa la conversion
        return exchangeRates.get("conversion_rates").get(targetCurrency);
    }
}
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        // специальная конструкция try, которая автоматически закрывает ресурсы после завершения работы с ними
        try(BufferedReader reader = new BufferedReader(new FileReader("Source_text.txt")); // поток чтения файла
            BufferedWriter writer = new BufferedWriter(new FileWriter("Result.svc"))) // поток записи файла
        {
            String str;
            StringBuilder sbFile = new StringBuilder();

            int countLine = 0;
            // построчно объединяем строку для поиска совпадений в ней, при этом считаем количество строк
            while ((str = reader.readLine()) != null){
                sbFile.append(str).append("\n");
                countLine++;
            }

            // переделываем полученную строку из StringBuilder в String для дальнейшей работы
            str = sbFile.toString();

            // Получение количества совпадений с использованием общего метода, использующего регулярные выражения
            long countNotSpace = countMatches(str, "\\S", 0);
            long countWords = countMatches(str, "(^|\\b)\\S+(\\b|$)", 0);
            long countParagraph = countMatches(str, "^ {4}\\S", Pattern.MULTILINE);
            long countPage = countMatches(str, "\\\\f", 0) + 1;// прибавляем 1 страницу, т.к. их изначально не может быть 0

            String result = "Статистика:" +
                    "\nСтраниц: "+ countPage +
                    "\nСлов: " + countWords +
                    "\nЗнаков (с пробелами): " + str.length() +
                    "\nЗнаков (без пробелов): " + countNotSpace +
                    "\nАбзацев: " + countParagraph +
                    "\nСтрок: " + countLine;

            // вывод статистики на консоль
            System.out.println(result);

            // запись результата в файл svc
            writer.write(result);

            System.out.println("\nСтатистика записана в файл Result.svc");
        }
        // обработка всевозможных ошибок
        catch (FileNotFoundException ex) {
            System.out.println("Ошибка: Файл не найден. Сообщение: " + ex.getMessage());
        } catch (SecurityException ex) {
            System.out.println("Ошибка прав для доступа к файлу. Сообщение: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Ошибка ввода-вывода. Сообщение: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Произошла ошибка. Сообщение: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    // метод для подсчета совпадений
    public static long countMatches(String input, String regex, int flags) {
        Pattern pattern = Pattern.compile(regex, flags);
        Matcher matcher = pattern.matcher(input);
        return matcher.results().count();
    }
}
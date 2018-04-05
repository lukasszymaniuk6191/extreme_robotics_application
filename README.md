# extreme_robotics_application

Odnośnie aplikacji próbowałem zaimplementować metodę do pobierania danych historycznych z ostatnich 30 dni przy każdym 
uruchomieniu aplikacji, jednak api NBP dla kilku tabel nie pozwoliło na pobranie danych, zatem dane umieściłem w pliku 
sql (daty pobrania danych od 2018-03-22 do 2018-04-05)  które są generowane przy każdym uruchomieniu aplikacji.  
W cześci frontendowej przygotowałem dwa pliki do rysowania wykresów chart.js i chart2.js, Porównując dwa wykresy uznałem że 
chart2.js jest bardziej czytelny. Na wykresach znajdują się dane historyczne kursów walut wraz z prognozami na określoną liczbę
dni (usawioną w pliku application.properties) po najechaniu kursorem na wybrany wykres pojawia się w markerze data oraz wartość
kursu waluty. Do prognozowania kursów walut wykorzystałem regresję liniową. Pobrane pliki znajdują się w folderze resources/file.
Logi znajdują się w folderze resources/logs. Przed uruchomieniem aplikacji ścieżki należy ustawić w pliku application.properties.

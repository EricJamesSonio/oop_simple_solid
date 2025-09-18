$files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }
>> javac -d out $files

java -cp out POS.Main

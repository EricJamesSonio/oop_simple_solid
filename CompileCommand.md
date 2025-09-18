$files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }
>> javac -d out $files

java -cp out POS.Main





# Remove old compiled files (if out folder exists)
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue

# Create new out folder
New-Item -ItemType Directory -Path out

# Collect all .java files
$files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }

# Compile targeting Java 17
javac --release 17 -d out $files

# Run the program
java -cp out POS.Main

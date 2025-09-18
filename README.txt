Simple Restaurant POS (TUI) - Java
---------------------------------
This is a simple console-based (TUI) POS built for a school exam.
- Role-based: admin and cashier dashboards.
- Uses file-based storage in resources/data/*.txt
- GUI folder exists but empty (for future GUI).
- Switching between TUI and GUI is done by changing one line in Main.java (hard-coded).

Compile:
    javac -d out $(find . -name "*.java")

Run:
    java -cp out POS.Main

Or (Windows):
    javac -d out $(for /r %i in (*.java) do @echo %i)
    java -cp out POS.Main

Notes:
- Keep resources/data files relative to the working directory when running.
- This project purposefully keeps logic in Services/Repositories; Main only boots the UI.

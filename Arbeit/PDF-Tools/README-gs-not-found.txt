1. Install Ghostscript: https://ghostscript.com/releases/gsdnld.html
2. Navigate to the Ghostscript installation directory, e.g. "C:\Program Files\gs\gs10.00.0\bin"
3. Open an elevated Command Prompt there
4. Create a symlink: "mklink gs.exe gswin64c.exe"

-> Enables the user to call Ghostscript by typing 'gs' in a command prompt.
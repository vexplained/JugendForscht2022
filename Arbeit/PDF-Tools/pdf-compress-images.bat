@echo off
:inputSetting
echo Available settings:
echo   screen   (screen-view-only quality, 72 dpi images)
echo   ebook    (low quality, 150 dpi images)			^<-almost not usable
echo * printer  (high quality, 300 dpi images)			^<- very good option
echo   prepress (high quality, color preserving, 300 dpi imgs)
echo   default  (almost identical to /screen)
echo.	 & :: Blank line
set /p chosenSetting=Enter setting ^> 
for %%A in (screen ebook printer prepress default) Do (
if /i '%chosenSetting%'=='' (
set setting=printer
echo Chosen setting: (Default selection)
goto continue
)

if /i '%chosenSetting%'=='%%A' (
set setting=%%A
goto continue
)
)
echo Invalid selection. Retry.
goto inputSetting

:continue
echo Chosen setting: %setting%
set /p pdf=Enter PDF file location (C:\**\*.pdf) ^> 
echo PDF file location: '%pdf%'
set /p outfilename=Enter PDF output file name (without extension) ^> 
echo Output file name: '%outfilename%' (.pdf)

gs -sDEVICE=pdfwrite -dCompatibilityLevel=1.5 -dPDFSETTINGS=/%setting% -dNOPAUSE -dQUIET -dBATCH -sOutputFile=%outfilename%.pdf %pdf%

echo Done.

pause
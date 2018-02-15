@ECHO OFF

SET BASE_DIR=%~dp0

rem set JAVA_HOME here JDK - PATH Like Eg :- C:\Program Files\Java\jdk1.6.0_31
set JAVA=C:\Program Files\Java\jdk1.8.0_144

if "%JAVA%" == "" goto noJavaHome
goto execute

:noJavaHome
echo JAVA_HOME is not set
goto end

:execute

echo Start

REM set /p BATCH_TYPE= Enter Selection:


SET CLASS_PATH="E:\Documents\Machine_Learning-0.0.1-SNAPSHOT-jar-with-dependencies.jar"

REM Run_Tesseract_OCR
"C:\Program Files\Java\jdk1.8.0_144\bin\java" -cp "%CLASS_PATH%" com.machinelearning.parser.ResumeProcessorUtility
REM %BATCH_TYPE%

REM Apply_ML
echo Apply Machine Learning on processed text
python E:\\Documents\\python\\multithread_pythonscript.py


"C:\Program Files\Java\jdk1.8.0_144\bin\java" -cp "%CLASS_PATH%" com.machinelearning.mongo.MongoDBConnectionUtil

:end

pause

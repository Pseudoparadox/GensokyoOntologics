@echo off
setlocal enabledelayedexpansion

:INPUT
set "folder="
set "content="

echo 请输入目标文件夹路径：
set /p "folder=>> "
if not exist "!folder!\" (
    echo 错误：文件夹不存在或路径无效
    goto :ERROR
)

echo 请输入要写入的文本内容（支持特殊字符）：
set /p "content=>> "

set "confirmed="
echo.
echo 即将用以下文本覆盖 [!folder!] 内所有txt文件：
echo [!content!]
echo.
choice /c YN /m "确认执行操作？(Y/N)"
if errorlevel 2 goto :CANCEL

:PROCESS
set /a count = 0
echo 正在处理...
cd /d "!folder!" || goto :ERROR
for %%i in (*.txt) do (
    set "add_file_name=!content!%%~ni"
    set "json=!add_file_name!^"}}"
    > "%%i" (
        echo(!json!
        set /a count+=1
    )
)
echo 操作完成！共处理文件数量： 
dir /b *.txt 2>nul | find /c /v ""
goto :EXIT

:ERROR
echo 处理过程中发生错误
exit /b 1

:CANCEL
echo 操作已取消
exit /b 0

:EXIT
endlocal
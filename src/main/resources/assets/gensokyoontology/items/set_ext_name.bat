@echo off
setlocal enabledelayedexpansion

:MAIN
cls
echo 文件后缀批量替换工具
echo ------------------------

:INPUT_EXT
set "ext1="
set "ext2="

rem 获取第一个后缀输入
echo 请输入原始文件后缀（例如 txt）： 
set /p "ext1=>> "
if "!ext1!"=="" (
    echo 错误：后缀不能为空
    timeout /t 2 >nul
    goto INPUT_EXT
)

rem 规范化处理输入
set "ext1=!ext1:.=!"
if "!ext1!"=="" (
    echo 错误：无效的后缀格式
    timeout /t 2 >nul
    goto INPUT_EXT
)

:INPUT_NEW_EXT
rem 获取新后缀输入
echo 请输入新文件后缀（例如 bak）： 
set /p "ext2=>> "
if "!ext2!"=="" (
    echo 错误：新后缀不能为空
    timeout /t 2 >nul
    goto INPUT_NEW_EXT
)

rem 校验非法字符
echo !ext2! | findstr /r "[\\/:*?\"<>|]" >nul
if %errorlevel% equ 0 (
    echo 错误：新后缀包含非法字符
    timeout /t 2 >nul
    goto INPUT_NEW_EXT
)

rem 规范化新后缀
set "ext2=!ext2:.=!"

rem 检查后缀是否相同
if /i "!ext1!"=="!ext2!" (
    echo 错误：新旧后缀不能相同
    timeout /t 2 >nul
    goto INPUT_EXT
)

:CONFIRM
echo.
echo 即将执行操作：
echo 将 [!ext1!] 后缀文件重命名为 [!ext2!] 后缀
echo 在目录：%cd%
echo.
choice /c YN /m "确认执行操作？(Y/N)"
if errorlevel 2 (
    echo 操作已取消
    timeout /t 2 >nul
    exit /b 0
)

:PROCESS
set "success=0"
set "fail=0"

for %%f in (*.!ext1!) do (
    set "old=%%f"
    set "new=%%~nf.!ext2!"
    
    if exist "!new!" (
        echo 错误：!new! 已存在，跳过重命名
        set /a fail+=1
    ) else (
        ren "%%f" "!new!" 2>nul
        if errorlevel 1 (
            echo 失败：%%f
            set /a fail+=1
        ) else (
            echo 成功：%%f → !new!
            set /a success+=1
        )
    )
)

:SUMMARY
echo.
echo 操作完成！
echo 成功：!success! 个文件
echo 失败：!fail! 个文件
echo.

:EXIT
pause
exit /b 0
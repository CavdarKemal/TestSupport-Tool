@ECHO OFF
del mappings\*.json
copy configinfo.json mappings\configinfo.json
FOR %%X IN (__files\*.xml) DO call :body %%X
goto :eof

:body
set xml_file=%~n1
(
    echo {
    echo     "request": {
    echo         "method": "GET",
    echo         "url": "/cte_tesun_service/tesun/xmlaccess/%xml_file:~0,10%"
    echo     },
    echo     "response": {
    echo         "status": 200,
    echo         "bodyFileName": "/%xml_file%.xml"
    echo     }
    echo }
) > mappings\%xml_file%.json
goto :eof

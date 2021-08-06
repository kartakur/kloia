@echo off
setlocal
FOR /F "tokens=*" %%i in ('type kloiaEnv.env') do SET %%i
docker-compose -f mysql/docker-compose.yml up -d
docker-compose -f Packages/docker-compose.yml up -d
endlocal